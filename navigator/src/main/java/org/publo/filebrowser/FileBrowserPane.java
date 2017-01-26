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
package org.publo.filebrowser;

import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import org.publo.filebrowser.listener.EditableTreeCell;
import org.publo.filebrowser.utils.FileTreeView;
import org.publo.filebrowser.utils.PathTreeItem;

/**
 * A {@code BorderPane} with file browsing capabilities. It allows the addition
 * of selection {@code ChangeListener}s so to maintain models relevant and
 * provides basic file manipulation facilities.
 *
 * Creating a standalone FileBrowserPane would allow for its abstraction in a
 * reusable JavaFX {@code Pane}.
 *
 * The only TODO is to work out a sensible API that could allow the delay in
 * creation and initialisation of the {@code TreeView} in a custom {@code Path}.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public final class FileBrowserPane extends BorderPane {

    /**
     * The current user home directory.
     */
    private static final String USER_HOME = System.getProperty("user.home");

    /**
     * The custom browser root system property key.
     */
    public static final String BROWSER_ROOT_KEY = "browser.root.path";

    /**
     * The browser root. If a root is defined in system properties use that one
     * else user the user home directory.
     */
    public static final String BROWSER_ROOT
            = System.getProperty(BROWSER_ROOT_KEY, USER_HOME);

    private final TreeView<String> treeView;

    /**
     * The ChangeListener converting a private API to a standard {@link Path}
     * based listener.
     */
    private final PathTreeItemListener pathTreeItemChangeListener
            = new PathTreeItemListener();

    private final PathTreeItemInvalidationListener pathTreeItemInvalidListener
            = new PathTreeItemInvalidationListener();

    /**
     * Creates a default {@link FileBrowserPane}.
     */
    public FileBrowserPane() {
        this(BROWSER_ROOT);
    }

    /**
     * Allows the definition of the root path. There is no validation of whether
     * the path is valid. Using this constructor without appropriate checking
     * may break things.
     *
     * Hence private.
     *
     * @param rootPathString string representing the root path
     */
    private FileBrowserPane(final String rootPathString) {
        this(Paths.get(rootPathString));
    }

    /**
     * Allows the definition of the root {@link Path}. There is no validation of
     * whether the path is valid. Using this constructor without appropriate
     * checking may break things.
     *
     * Hence private.
     *
     * @param rootPath the root path
     */
    private FileBrowserPane(final Path rootPath) {
        this.treeView = new FileTreeView<>(new PathTreeItem(rootPath));
        this.treeView.setEditable(true);
        this.treeView.setShowRoot(false);
        this.treeView.setCellFactory(
                (TreeView<String> tree)
                -> new EditableTreeCell((FileTreeView<String>) tree)
        );
        this.treeView.getSelectionModel()
                .selectedItemProperty().addListener(pathTreeItemChangeListener);
        this.treeView.getSelectionModel()
                .selectedItemProperty().addListener(pathTreeItemInvalidListener);
        this.setCenter(this.treeView);
    }

    public void setRoot(final Path newRoot) {
        this.treeView.setRoot(new PathTreeItem(newRoot));
    }

    public final void addTreeItemSelectionListener(
            final ChangeListener<Path> listener) {
        this.pathTreeItemChangeListener.getPathProperty().addListener(listener);
    }

    public final void addTreeItemInvalidationListener(
            final InvalidationListener listener) {
        this.pathTreeItemInvalidListener.getPathInvalidatedProperty()
                .addListener(listener);
    }

    /**
     * Listeners aimed at encapsulating the {@code PathTreeItem} API to the
     * navigator package and exposing a dry {@code Path}.
     *
     */
    private final class PathTreeItemListener
            implements ChangeListener<TreeItem> {

        private final ObjectProperty<Path> pathProperty
                = new SimpleObjectProperty<>();

        private ObjectProperty<Path> getPathProperty() {
            return this.pathProperty;
        }

        @Override
        public final void changed(
                final ObservableValue<? extends TreeItem> observable,
                final TreeItem oldValue,
                final TreeItem newValue) {
            final PathTreeItem pathTreeItem = (PathTreeItem) newValue;
            pathProperty.setValue(pathTreeItem.getPath());
        }

    }

    private final class PathTreeItemInvalidationListener
            implements InvalidationListener {

        private final ObjectProperty<Path> pathInvalidatedProperty
                = new SimpleObjectProperty<>();

        private ObjectProperty<Path> getPathInvalidatedProperty() {
            return this.pathInvalidatedProperty;
        }

        @Override
        public void invalidated(Observable observable) {
            final ReadOnlyObjectProperty property
                    = (ReadOnlyObjectProperty) observable;
            final PathTreeItem pathTreeItem = (PathTreeItem) property.getValue();
            this.pathInvalidatedProperty.setValue(pathTreeItem.getPath());
        }
    }
}
