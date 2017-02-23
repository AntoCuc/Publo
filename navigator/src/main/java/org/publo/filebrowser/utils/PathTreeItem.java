/*
 * The MIT License
 *
 * Copyright 2016-2017 Antonino Cucchiara.
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.publo.filebrowser.listener.DirectoryExpandedListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.publo.filebrowser.FileBrowserPane;

/**
 * Defines a TreeItem representing a path in the file-system.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class PathTreeItem extends TreeItem {

    /**
     * The {@code PathTreeItem} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PathTreeItem.class.getName());

    private static final PathTreeItem DEFAULT_TREE_ITEM
            = new PathTreeItem("...", Paths.get(FileBrowserPane.BROWSER_ROOT));

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
            FileSystemWatcher.getInstance()
                    .register(this, ENTRY_CREATE, ENTRY_DELETE);
        } else {
            final File f = path.toFile();
            try (InputStream inputStream
                    = new BufferedInputStream(new FileInputStream(f))) {
                final Metadata metadata = new Metadata();
                metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
                final Detector detector = new DefaultDetector();
                final MediaType mime = detector.detect(inputStream, metadata);
                switch (mime.toString()) {
                    case "text/x-web-markdown":
                        setGraphic(ResourceFactory.buildImageView("/media/markdown.png"));
                        break;
                    case "text/plain":
                        setGraphic(ResourceFactory.buildImageView("/media/page_white.png"));
                        break;
                    case "text/html":
                        setGraphic(ResourceFactory.buildImageView("/media/xhtml.png"));
                        break;
                    case "text/css":
                        setGraphic(ResourceFactory.buildImageView("/media/css.png"));
                        break;
                    case "image/png":
                    case "image/gif":
                    case "image/jpeg":
                        setGraphic(ResourceFactory.buildImageView("/media/image.png"));
                        break;
                    case "text/x-java-properties":
                        setGraphic(ResourceFactory.buildImageView("/media/page_white_wrench.png"));
                        break;
                    default:
                        LOGGER.log(Level.INFO, "Default icon for {0}", mime);
                        setGraphic(ResourceFactory.buildImageView("/media/page_white.png"));
                        break;
                }
            } catch (final IOException ex) {
                LOGGER.log(Level.SEVERE, "Could not detect mediatype", ex);
            }
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PathTreeItem other = (PathTreeItem) obj;
        return !(!Objects.equals(this.path, other.path)
                && !Objects.equals(super.getValue(), other.getValue()));
    }

}
