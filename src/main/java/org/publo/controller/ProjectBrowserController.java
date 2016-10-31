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
import java.nio.file.attribute.FileAttribute;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.publo.controller.utils.PathTreeItem;
import org.publo.model.Page;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class ProjectBrowserController implements Initializable {

    private static final Logger LOGGER
            = Logger.getLogger(ProjectBrowserController.class.getName());

    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final String USER_DIR = System.getProperty("user.home");

    private static final String PROJ_DIR_NAME = "publo-projects";

    private static final Path PROJECTS_PATH = Paths.get(USER_DIR, PROJ_DIR_NAME);

    private static final TreeItem DEFAULT_TREE_ITEM = new TreeItem("...");

    @FXML
    private TreeView<String> treeView;

    /**
     * Initialises the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!Files.exists(PROJECTS_PATH, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(PROJECTS_PATH);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Initialises the project browser TreeItems using the user directory as a
     * tree root.
     *
     * @throws IOException
     */
    void initProjectBrowser(Page page) {
        final PathTreeItem rootTreeItem =
                new PathTreeItem(PROJ_DIR_NAME, PROJECTS_PATH);
        initialise(rootTreeItem);
        final FileSelectedListener listener = new FileSelectedListener(page);
        treeView.getSelectionModel().selectedItemProperty().addListener(listener);
        treeView.setRoot(rootTreeItem);
        treeView.setShowRoot(false);
    }

    /**
     * Listener concerning the expansion of directory-representing TreeItems. On
     * expansion of such nodes it will clear the "holding" value and populate
     * the sub-tree.
     */
    private class DirectoryExpandedListener implements ChangeListener<Boolean> {

        @Override
        public void changed(
                ObservableValue<? extends Boolean> observable,
                Boolean oldValue,
                Boolean newValue) {
            final BooleanProperty bb = (BooleanProperty) observable;
            final PathTreeItem expandedItem = (PathTreeItem) bb.getBean();
            expandedItem.getChildren().clear();
            initialise(expandedItem);
        }
    }

    private void initialise(PathTreeItem directoryNode) {
        try {
            Files.list(directoryNode.getPath()).forEach(path -> {
                final String label = path.getFileName().toString();
                final PathTreeItem fileTreeItem = new PathTreeItem(label, path);
                directoryNode.getChildren().add(fileTreeItem);
                if (Files.isDirectory(path)) {
                    fileTreeItem.getChildren().add(DEFAULT_TREE_ITEM);
                    final DirectoryExpandedListener listener
                            = new DirectoryExpandedListener();
                    fileTreeItem.expandedProperty().addListener(listener);
                }
            });
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private class FileSelectedListener implements ChangeListener<TreeItem> {

        private final Page page;

        private FileSelectedListener(Page page) {
            this.page = page;
        }

        @Override
        public void changed(ObservableValue<? extends TreeItem> observable,
                TreeItem oldValue,
                TreeItem newValue) {
            PathTreeItem selectedTreeItem = (PathTreeItem) newValue;
            Path selectedPath = selectedTreeItem.getPath();
            if (Files.isRegularFile(selectedPath, LinkOption.NOFOLLOW_LINKS)) {
                LOGGER.info("File selected.");
                try {
                    page.getMarkdown().setValue(Files.readAllLines(selectedPath)
                            .stream().collect(Collectors.joining(LINE_SEP)));
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
