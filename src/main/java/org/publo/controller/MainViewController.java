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
import org.publo.Launcher;
import org.publo.controller.listener.ResourceChangedListener;
import org.publo.filebrowser.FileBrowser;
import org.publo.model.PageFile;
import org.publo.model.PageMarkup;
import org.publo.model.PageSource;
import org.publo.model.PageTemplate;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class MainViewController implements Initializable {

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
    private FileBrowser fileBrowser;

    /**
     * Initialises the controller class.
     *
     * Bootstraps the <code>ChangeListener</code> chain.
     *
     * Listeners Chain:
     * File Selection Change -> Source Change -> TextArea Change ...
     * TextArea Change -> Source Change -> Markup Change ...
     * Template Change -> Markup Change ...
     * Markup Change -> WebView Update
     *
     * @param url
     * @param rb
     */
    @Override
    public final void initialize(final URL url, final ResourceBundle rb) {
        LOGGER.info("Initialising the Main View");
        final PageSource source = new PageSource();
        textAreaPaneController.init(source.getMarkdown());

        final PageTemplate pageTemplate = new PageTemplate();
        final PageMarkup markup = new PageMarkup(pageTemplate);

        source.getMarkdown().addListener(new ResourceChangedListener<>(markup));
        source.getMarkdown().addListener(new ResourceChangedListener<>(textAreaPaneController));
        markup.getMarkup().addListener(new ResourceChangedListener<>(webViewPaneController));
        pageTemplate.getTemplate().addListener(
                (ObservableValue<? extends String> observable, 
                        String oldValue, 
                        String newValue) -> {
            markup.update(source.getMarkdown().getValue());
        });

        final PageFile file = new PageFile();
        menubarPaneController.init(source, pageTemplate, file);
        fileBrowser.addSelectionListener(new ResourceChangedListener<>(source));
        fileBrowser.addSelectionListener(new ResourceChangedListener<>(file));
    }
}
