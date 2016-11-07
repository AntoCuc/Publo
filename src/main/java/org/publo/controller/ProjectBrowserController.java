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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import org.publo.model.Asset;
import org.publo.controller.utils.EditableTreeCell;
import org.publo.controller.utils.PathTreeItem;
import org.publo.controller.listener.MdFileSelectedListener;
import org.publo.controller.listener.SelectedAssetListener;
import org.publo.model.PageSource;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class ProjectBrowserController implements Initializable {

    private static final Logger LOGGER
            = Logger.getLogger(ProjectBrowserController.class.getName());

    private static final String USER_DIR = System.getProperty("user.home");

    private static final String PROJ_DIR_NAME = "publo-projects";

    public static final Path PROJECTS_PATH = Paths.get(USER_DIR, PROJ_DIR_NAME);

    private static final PathTreeItem TREE_ROOT = new PathTreeItem(PROJ_DIR_NAME, PROJECTS_PATH);

    @FXML
    private TreeView<String> treeView;

    /**
     * Initialises the controller class.
     *
     * Creates the projects directory if it doesn't exist.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        if (!Files.exists(PROJECTS_PATH, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(PROJECTS_PATH);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Initialise the project browser TreeItems using the projects directory as
     * a tree root.
     *
     * Attach listeners to the selection model to (1) open files as they are
     * selected (2) hold the location of the selected asset.
     *
     * Register the with the file-system watcher.
     *
     * Set a {@code CellFactory} facilitating the renaming of {@code TreeItems}.
     */
    void initProjectBrowser(final PageSource page, final Asset asset) {
        final MdFileSelectedListener mdFileSelectedListener
                = new MdFileSelectedListener(page);
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(mdFileSelectedListener);
        final SelectedAssetListener selectedAssetListener
                = new SelectedAssetListener(asset);
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(selectedAssetListener);
        treeView.setRoot(TREE_ROOT);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.setCellFactory((TreeView<String> param) -> new EditableTreeCell(asset));
    }
}
