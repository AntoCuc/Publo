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
package org.publo.model;

import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.publo.controller.utils.MarkdownParser;
import org.publo.controller.utils.TemplateRenderer;

/**
 * PageMarkup core model. Holds state concerning the markup rendered and the
 * template to apply on preview.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class PageMarkup {

    /**
     * The {@code PageMarkup} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PageMarkup.class.getName());

    /**
     * The template file name to be applied on rendering.
     */
    private final StringProperty template = new SimpleStringProperty("Default");
    
    private String markdownCache = "";

    public StringProperty getTemplate() {
        return template;
    }

    /**
     * Parses the markdown and renders it to a template.
     *
     * @param markdown to render
     * @return the populated, rendered template
     */
    public String render(final StringProperty markdown) {
        LOGGER.info("Caching the markdown.");
        final String markdownValue = markdown.getValue();
        this.markdownCache = markdownValue;
        LOGGER.info("Rendering the markup.");
        final String contentMarkup = MarkdownParser.parse(markdownValue);
        return TemplateRenderer.render(template.getValue(), contentMarkup);
    }
    
    /**
     * Renders the markup applying a template.
     * 
     * @param template to render to
     * @return the rendered markup
     */
    public String updateTemplate(final StringProperty template) {
        LOGGER.info("Updating template.");
        final String contentMarkup = MarkdownParser.parse(markdownCache);
        return TemplateRenderer.render(template.getValue(), contentMarkup);
    }
}
