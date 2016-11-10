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
import org.publo.controller.utils.Updatable;

/**
 * PageTemplate core model. Holds state concerning the template markup rendered
 * to apply on preview.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public final class PageTemplate implements Updatable<String> {

    /**
     * The {@code PageMarkup} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PageTemplate.class.getName());

    /**
     * The template file name to be applied on rendering.
     */
    private final StringProperty template = new SimpleStringProperty("Default");

    public StringProperty getTemplate() {
        return template;
    }

    @Override
    public void update(final String newTemplate) {
        LOGGER.log(Level.INFO, "Updating template to {0}", newTemplate);
        template.setValue(newTemplate);
    }
}
