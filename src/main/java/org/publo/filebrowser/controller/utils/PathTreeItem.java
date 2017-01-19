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
package org.publo.filebrowser.controller.utils;

import org.publo.filebrowser.controller.listener.DirectoryExpandedListener;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.control.TreeItem;
import static org.publo.Launcher.PROJECTS_PATH;

/**
 * Defines a TreeItem representing a path in the file-system.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class PathTreeItem extends TreeItem {

    private static final PathTreeItem DEFAULT_TREE_ITEM
            = new PathTreeItem("...", PROJECTS_PATH);

    private Path path;
    
    public PathTreeItem(final Path path) {
        this(path.getFileName().toString(), path);
    }

    public PathTreeItem(String label, Path path) {
        super(label);
        this.path = path;
        if (Files.isDirectory(path)) {
            setGraphic(ResourceFactory.buildImageView("/media/folder.png"));
            getChildren().add(DEFAULT_TREE_ITEM);
            final DirectoryExpandedListener listener
                    = new DirectoryExpandedListener();
            expandedProperty().addListener(listener);
        } else {
            setGraphic(ResourceFactory.buildImageView("/media/page_white.png"));
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
