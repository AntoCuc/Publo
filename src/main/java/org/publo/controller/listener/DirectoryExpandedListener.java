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
package org.publo.controller.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.publo.controller.ProjectBrowserController;
import org.publo.controller.utils.FileSystemWatcher;
import org.publo.controller.utils.PathTreeItem;

/**
 * Listener concerning the expansion of directory-representing TreeItems. On
 * expansion of such nodes it will clear the "holding" value and populate the
 * sub-tree.
 *
 * On selection of a file it is loaded in the editor.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class DirectoryExpandedListener implements ChangeListener<Boolean> {
    
    private static final FileSystemWatcher WATCHER = new FileSystemWatcher();
    
    static {
        WATCHER.start();
    }

    /**
     * The {@code DirectoryExpendedListener} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(ProjectBrowserController.class.getName());

    @Override
    public void changed(
            final ObservableValue<? extends Boolean> observable,
            final Boolean oldValue,
            final Boolean newValue) {
        final BooleanProperty bb = (BooleanProperty) observable;
        final PathTreeItem expandedItem = (PathTreeItem) bb.getBean();
        LOGGER.log(Level.INFO, "Expanded: {0}", expandedItem);
        final List<PathTreeItem> children = expandedItem.getChildren();
        children.clear();
        try {
            Files.list(expandedItem.getPath()).forEach((Path path) -> {
                final String label = path.getFileName().toString();
                final PathTreeItem treeItem = new PathTreeItem(label, path);
                children.add(treeItem);
                if (Files.isDirectory(path)) {
                    treeItem.expandedProperty().addListener(DirectoryExpandedListener.this);
                    WATCHER.register(treeItem, ENTRY_CREATE, ENTRY_DELETE);
                }
            });
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
