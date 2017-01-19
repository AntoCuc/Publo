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
package org.publo.filebrowser.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.publo.model.PageFile;
import org.publo.filebrowser.controller.listener.EditableTreeCell;
import org.publo.filebrowser.controller.utils.PathTreeItem;
import org.publo.controller.listener.ResourceChangedListener;
import org.publo.controller.utils.TemplateRenderer;
import org.publo.model.PageSource;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class FileBrowserController {

    private static final String USER_DIR = System.getProperty("user.home");

    private static final String PROJ_DIR_NAME = "publo-projects";

    public static final Path PROJECTS_PATH = Paths.get(USER_DIR, PROJ_DIR_NAME);

    public static final Path TEMPLATES_PATH = Paths.get(TemplateRenderer.TEMPLATES_DIR);

    private static final PathTreeItem TREE_ROOT = new PathTreeItem(PROJ_DIR_NAME, PROJECTS_PATH);

    @FXML
    private TreeView<String> treeView;

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
     * @param source of the page
     * @param file holding the state
     */
    public void init(final PageSource source, final PageFile file) {
        final ResourceChangedListener<TreeItem> sourceFileSelectedListener
                = new ResourceChangedListener<>(source);
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(sourceFileSelectedListener);
        final ResourceChangedListener<TreeItem> treeItemChangeListener
                = new ResourceChangedListener<>(file);
        treeView.getSelectionModel().selectedItemProperty()
                .addListener(treeItemChangeListener);
        treeView.setRoot(TREE_ROOT);
        treeView.setShowRoot(false);
        treeView.setEditable(true);
        treeView.setCellFactory((TreeView<String> param) -> new EditableTreeCell(file));
    }
}
