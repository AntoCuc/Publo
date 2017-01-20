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
package org.publo.filebrowser.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * A {@code TreeView} representing the file {@code FileSystem}.
 *
 * @author Antonio Cucchiara
 * @param <T> The type of the item contained within the {@link TreeItem} value.
 * @since 0.3
 */
public class FileTreeView<T> extends TreeView<T> {

    private static final Logger LOGGER
            = Logger.getLogger(FileTreeView.class.getName());

    private Path currentPath;

    public FileTreeView(PathTreeItem root) {
        super(root);
        this.currentPath = root.getPath();
        this.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TreeItem<T>> observable,
                        TreeItem<T> oldValue,
                        TreeItem<T> selectedTreeItem) -> {
                    PathTreeItem selPathTreeItem = (PathTreeItem) selectedTreeItem;
                    final Path selectedPath = selPathTreeItem.getPath();
                    LOGGER.log(Level.INFO, "Selected file {0}", selectedPath);
                    currentPath = selectedPath;
                });

        final MenuItem newFileMenuItem = new MenuItem("New");
        newFileMenuItem.addEventHandler(EventType.ROOT, (Event event) -> {
            final Path newFilePath = Paths.get(
                    this.currentPath.toString(),
                    "New File.md"
            );
            try {
                Files.createFile(newFilePath);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to create new file", ex);
            }
        });
        final MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.addEventHandler(EventType.ROOT, (Event event) -> {
            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm file deletion");
            confirmationAlert.setHeaderText("Warning!");
            confirmationAlert.setContentText(
                    "Are you sure you want to permanently delete "
                    + currentPath.getFileName()
                    + "?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && ButtonType.OK == result.get()) {
                try {
                    Files.deleteIfExists(currentPath);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE,
                            "Unable to delete " + currentPath, ex);
                }
            }
        });
        final ContextMenu contextMenu = new ContextMenu(
                newFileMenuItem,
                deleteMenuItem
        );
        this.setContextMenu(contextMenu);
    }

    /**
     * Moves the selected file to the {@code Path}.
     *
     * @param to
     * @return
     */
    public Path moveSelectedFile(Path to) {
        final Path newFilePath = this.currentPath.getParent().resolve(to);
        LOGGER.log(Level.INFO, "Moving {0} to {1}",
                new Object[]{this.currentPath, newFilePath});
        try {
            this.currentPath
                    = Files.move(currentPath, newFilePath, REPLACE_EXISTING);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not move the file.", ex);
        }
        return newFilePath;
    }
}
