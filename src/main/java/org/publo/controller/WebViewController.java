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
package org.publo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.publo.controller.utils.Updatable;
import org.publo.model.PageMarkup;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class WebViewController implements Initializable, Updatable<String> {

    @FXML
    private WebView webView;

    private PageMarkup markup;

    /**
     * Update the {@code WebView} with the markup.
     *
     * @param markup to update to
     */
    public void updateWebView(final String markup) {
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(markup);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Update the {@code WebView} rendering the markdown and updating the UI.
     *
     * @param to
     * @return the rendered markup
     */
    @Override
    public String update(String to) {
        final String rendered = markup.render(to);
        updateWebView(rendered);
        return rendered;
    }

    /**
     * Initialise the {@code PageMarkup}
     *
     * @param markup to initialise
     */
    void init(PageMarkup markup) {
        this.markup = markup;
    }
}
