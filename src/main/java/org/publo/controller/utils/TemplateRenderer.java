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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.publo.controller.ProjectBrowserController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
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

    public static final String TEMPLATES_DIR
            = ProjectBrowserController.PROJECTS_PATH + "/templates/";
    private static final String TEMPLATE_SUFFIX = ".html";

    /**
     * Renders the content in the default template.
     *
     * @param template
     * @param content of the main
     * @return the markup
     */
    public static String render(String template, String content) {
        if (template == null || "".equals(template)) {
            return content;
        }
        LOGGER.log(Level.INFO, "Rendering content on template {0}", template);
        final FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(TEMPLATES_DIR);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setSuffix(TEMPLATE_SUFFIX);
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        final Context context = new Context();
        context.setVariable("main", content);
        return templateEngine.process(template, context);
    }

}
