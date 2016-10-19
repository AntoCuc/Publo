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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.publo.controller.utils.MarkdownParser;
import org.publo.controller.utils.TemplateRenderer;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class MainViewController implements Initializable {

    @FXML
    private MenubarController menubarPaneController;

    @FXML
    private TextAreaController textAreaPaneController;

    @FXML
    private WebViewController webViewPaneController;

    final StringProperty markdown = new SimpleStringProperty();
    
    final StringProperty template = new SimpleStringProperty("Default");

    /**
     * Initialises the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textAreaPaneController.initMarkDown(markdown);
        markdown.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            final String contentMarkup = MarkdownParser.parse(newValue);
            final String pageMarkup = TemplateRenderer.render(template.getValue(), contentMarkup);
            webViewPaneController.updateWebView(pageMarkup);
        });
        template.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            final String contentMarkup = MarkdownParser.parse(markdown.getValue());
            final String pageMarkup = TemplateRenderer.render(template.getValue(), contentMarkup);
            webViewPaneController.updateWebView(pageMarkup);
        });
        menubarPaneController.initMarkdown(markdown);
        menubarPaneController.initTemplate(template);
        menubarPaneController.initTextAreaController(textAreaPaneController);
    }
}
