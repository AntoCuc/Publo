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

import java.nio.file.Path;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.publo.controller.utils.MarkdownParser;
import org.publo.controller.utils.TemplateRenderer;

/**
 * Publo core model. Holds state concerning the markdown being produced and the
 * template to apply on preview.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class Page {

    /**
     * The markdown text being edited in the TextArea
     */
    final StringProperty markdown = new SimpleStringProperty();

    /**
     * The template file name to be applied on rendering.
     */
    final StringProperty template = new SimpleStringProperty("Default");

    /**
     * The page's file path.
     */
    private Path filePath;

    public StringProperty getMarkdown() {
        return markdown;
    }

    public StringProperty getTemplate() {
        return template;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String renderMarkup() {
        final String contentMarkup = MarkdownParser.parse(markdown.getValue());
        return TemplateRenderer.render(template.getValue(), contentMarkup);
    }

}
