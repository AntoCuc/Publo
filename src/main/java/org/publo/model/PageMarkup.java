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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.publo.controller.utils.TemplateRenderer;

/**
 * PageMarkup core model. Holds state concerning the markupProperty rendered and
 * the template to apply on preview.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class PageMarkup implements ChangeListener {

    /**
     * The {@code PageMarkup} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PageMarkup.class.getName());

    private final StringProperty templateProperty = new SimpleStringProperty("Default");

    private final StringProperty markupProperty = new SimpleStringProperty("");

    public StringProperty getMarkupProperty() {
        return markupProperty;
    }

    public StringProperty getTemplateProperty() {
        return templateProperty;
    }

    /**
     * Parses the changed markdown and renders it to a template.
     */
    @Override
    public void changed(ObservableValue observable,
            Object oldValue,
            Object newValue) {
        LOGGER.log(Level.INFO, "Updating markup");
        final String renderedMarkup = TemplateRenderer.render(
                templateProperty.getValue(), newValue.toString());
        markupProperty.setValue(renderedMarkup);
    }
}
