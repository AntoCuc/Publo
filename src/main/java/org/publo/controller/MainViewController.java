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
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.publo.controller.listener.ResourceChangedListener;
import org.publo.filebrowser.FileBrowserPane;
import org.publo.model.PageFile;
import org.publo.model.PageMarkup;
import org.publo.model.PageTemplate;
import org.publo.textarea.TextAreaPane;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class MainViewController {

    /**
     * The {@code MainViewController} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(MainViewController.class.getName());

    @FXML
    private MenubarController menubarPaneController;

    @FXML
    private TextAreaPane textAreaPane;

    @FXML
    private WebViewController webViewPaneController;

    @FXML
    private FileBrowserPane fileBrowserPane;

    /**
     * Initialises the controller class.
     *
     * Bootstraps the <code>ChangeListener</code> chain.
     *
     * Listeners Chain: File Selection Change -> Source Change -> TextArea
     * Change ... TextArea Change -> Source Change -> Markup Change ... Template
     * Change -> Markup Change ... Markup Change -> WebView Update
     *
     */
    @FXML
    public final void initialize() {
        LOGGER.info("Initialising the Main View");

        final PageTemplate pageTemplate = new PageTemplate();
        final PageMarkup markup = new PageMarkup(pageTemplate);

        textAreaPane.addTextChangeListener(new ResourceChangedListener<>(markup));
        markup.getMarkup().addListener(new ResourceChangedListener<>(webViewPaneController));
        pageTemplate.getTemplate().addListener(
                (ObservableValue<? extends String> observable,
                        String oldValue,
                        String newValue) -> {
                    markup.update(textAreaPane.getText());
                });

        final PageFile file = new PageFile();
        menubarPaneController.init(textAreaPane, pageTemplate, file);
        fileBrowserPane.addTreeItemSelectionListener(textAreaPane);
        fileBrowserPane.addTreeItemSelectionListener(new ResourceChangedListener<>(file));
    }
}
