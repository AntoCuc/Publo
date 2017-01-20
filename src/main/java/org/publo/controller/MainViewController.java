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

import java.util.logging.Logger;
import javafx.fxml.FXML;
import org.publo.filebrowser.FileBrowserPane;
import org.publo.preview.PreviewPane;
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
    private PreviewPane previewPane;

    @FXML
    private FileBrowserPane fileBrowserPane;

    /**
     * Initialises the controller class.
     *
     * Bootstraps the <code>ChangeListener</code> chain.
     *
     */
    @FXML
    public final void initialize() {
        LOGGER.info("Initialising the Main View");
        fileBrowserPane.addTreeItemSelectionListener(textAreaPane);
        menubarPaneController.init(textAreaPane);
        fileBrowserPane.addTreeItemSelectionListener(menubarPaneController);
        textAreaPane.addTextChangeListener(previewPane);
    }
}
