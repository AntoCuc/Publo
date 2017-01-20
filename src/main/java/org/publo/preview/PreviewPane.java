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
package org.publo.preview;

import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.publo.controller.utils.TemplateRenderer;

/**
 * A {@code BorderPane} {@code WebView}.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public class PreviewPane extends BorderPane implements ChangeListener {

    private static final Logger LOGGER
            = Logger.getLogger(PreviewPane.class.getName());

    private final WebView webView;

    public PreviewPane() {
        this.webView = new WebView();
        this.setCenter(this.webView);
    }

    /**
     * On change of the {@code TextArea} update the {@code WebView}.
     *
     * @param observable not used
     * @param oldValue used to verify the presence of changes
     * @param newValue populate the area
     */
    @Override
    public void changed(
            ObservableValue observable,
            Object oldValue,
            Object newValue) {
        LOGGER.info("Updating the Preview Pane.");
        final String renderedMarkup = TemplateRenderer.render(newValue.toString());
        final WebEngine webEngine = this.webView.getEngine();
        webEngine.loadContent(renderedMarkup);
    }
}
