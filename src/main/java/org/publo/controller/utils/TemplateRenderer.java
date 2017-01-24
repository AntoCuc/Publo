/*
 * The MIT License
 *
 * Copyright 2016 Antonino Cucchiara.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.publo.controller.utils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.publo.Launcher.PROJECTS_PATH;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

/**
 * Template Rendering Library Wrapper.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class TemplateRenderer {

    private static final Logger LOGGER
            = Logger.getLogger(TemplateRenderer.class.getName());

    private static final String DEFAULT_TEMPLATE_NAME = "default-template";

    public static final String TEMPLATES_DIR
            = PROJECTS_PATH + "/templates/";
    private static final String TEMPLATE_SUFFIX = ".html";

    /**
     * Renders the content for export. No preview scrolling is available when
     * using this method.
     *
     * @param markdown of the main
     * @return the page markup
     */
    public static String render(final String markdown) {
        return render(markdown, null);
    }

    /**
     * Renders the content. Provides the option to the preview facility by
     * injecting a window scrolling java-script function in the markup and a
     * base path for the retrieval of media assets.
     *
     * If the basepath is not defined it will assume the markup generated is not
     * going to be used in a {@code WebView} preview.
     *
     * @param markdown of the main
     * @param basePath for preview media loading
     * @return the page markup
     */
    public static String render(
            final String markdown,
            final Path basePath) {
        final List<Extension> extensions
                = Arrays.asList(YamlFrontMatterExtension.create());
        final Parser parser = Parser.builder().extensions(extensions).build();
        final Node document = parser.parse(markdown);
        final YamlFrontMatterVisitor frontMatterVisitor
                = new YamlFrontMatterVisitor();
        document.accept(frontMatterVisitor);
        final HtmlRenderer renderer = HtmlRenderer.builder().build();
        final String markup = renderer.render(document);
        final Context context = new Context();
        context.setVariable("main", markup);
        final Map<String, List<String>> data = frontMatterVisitor.getData();
        data.keySet().stream().forEach((key) -> {
            final StringBuilder valueBuilder = new StringBuilder();
            data.get(key).forEach((item) -> {
                valueBuilder.append(item);
            });
            final String value = valueBuilder.toString();
            context.setVariable(key, value);
        });
        final String html = render(context);
        Document htmlDoc = Jsoup.parse(html);
        if (basePath != null) {
            Element headElement = htmlDoc.head();
            headElement.append("<script>function scrollWin(value){ window.scrollTo(0, (document.body.scrollHeight - window.innerHeight) * value); }</script>");
            headElement.append("<base href=\"" + basePath.getParent().toUri() + "\" />");
        }
        return htmlDoc.toString();
    }

    private static String render(final Context context) {
        String output;
        try {
            final FileTemplateResolver fileTemplateResover
                    = new FileTemplateResolver();
            fileTemplateResover.setPrefix(TEMPLATES_DIR);
            fileTemplateResover.setTemplateMode(TemplateMode.HTML);
            fileTemplateResover.setSuffix(TEMPLATE_SUFFIX);
            final TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(fileTemplateResover);
            String template = "" + context.getVariable("template");
            LOGGER.log(Level.INFO, "Rendering {0}", template);
            output = templateEngine.process(template, context);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Using default template.", ex);
            final ClassLoaderTemplateResolver fileTemplateResover
                    = new ClassLoaderTemplateResolver();
            fileTemplateResover.setTemplateMode(TemplateMode.HTML);
            fileTemplateResover.setSuffix(TEMPLATE_SUFFIX);
            final TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(fileTemplateResover);
            output = templateEngine.process(DEFAULT_TEMPLATE_NAME, context);
        }
        return output;
    }
}
