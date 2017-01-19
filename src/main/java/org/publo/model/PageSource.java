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
package org.publo.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.publo.filebrowser.utils.PathTreeItem;
import org.publo.controller.utils.Updatable;

/**
 * PageSource core model. Holds state concerning the markdown being produced and
 * the template to apply on preview.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class PageSource implements Updatable<TreeItem> {

    /**
     * The {@code PageSource} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PageSource.class.getName());

    /**
     * OS-independent line separator.
     */
    private static final String LINE_SEP
            = System.getProperty("line.separator");

    /**
     * The markdown text being edited in the TextArea.
     */
    private final StringProperty markdown = new SimpleStringProperty("");

    public StringProperty getMarkdown() {
        return markdown;
    }

    /**
     * Updates the <code>markdown</code> content based on the selected
     * <code>TreeItem</code>.
     *
     * @param selTreeItem
     */
    @Override
    public void update(TreeItem selTreeItem) {
        LOGGER.log(Level.INFO, "Selected item {0}", selTreeItem.getValue());
        final PathTreeItem selPathTreeItem = (PathTreeItem) selTreeItem;
        final Path selectedPath = selPathTreeItem.getPath();
        if (Files.isRegularFile(selectedPath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                markdown.setValue(Files.readAllLines(selectedPath)
                        .stream().collect(Collectors.joining(LINE_SEP)));
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
}
