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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.publo.controller.listener.SourceChangeListener;
import org.publo.model.Asset;
import org.publo.model.PageMarkup;
import org.publo.model.PageSource;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class MainViewController implements Initializable {

    /**
     * The {@code MainViewController} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(MainViewController.class.getName());

    @FXML
    private MenubarController menubarPaneController;

    @FXML
    private TextAreaController textAreaPaneController;

    @FXML
    private WebViewController webViewPaneController;

    @FXML
    private ProjectBrowserController projectPaneController;

    /**
     * Initialises the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final PageSource source = new PageSource();
        final PageMarkup markup = new PageMarkup();
        webViewPaneController.init(markup);
        final Asset asset = new Asset();
        textAreaPaneController.initMarkDown(source.getMarkdown());
        source.getMarkdown().addListener(
                (ObservableValue<? extends String> observable,
                        final String oldValue,
                        final String newValue) -> {
                    final TextArea textArea = textAreaPaneController.getTextArea();
                    if (!newValue.equals(textArea.getText())) {
                        textArea.setText(newValue);
                    }
                });
        final SourceChangeListener<String> sourceChangeListener
                = new SourceChangeListener<>(webViewPaneController);
        source.getMarkdown().addListener(sourceChangeListener);
        markup.getTemplate().addListener(
                (ObservableValue<? extends String> observable,
                        final String oldValue,
                        final String newValue) -> {
                    LOGGER.log(Level.INFO, "Template changed from {0} to {1}",
                            new Object[]{oldValue, newValue});
                    final StringProperty template = (StringProperty) observable;
                    final String renderedMarkup = markup.updateTemplate(template);
                    webViewPaneController.updateWebView(renderedMarkup);
                });
        menubarPaneController.initMenubar(source, markup, asset);
        projectPaneController.initProjectBrowser(source, asset);
    }
}
