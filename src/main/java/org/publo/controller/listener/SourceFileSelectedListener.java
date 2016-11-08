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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import org.publo.controller.utils.PathTreeItem;
import org.publo.model.PageSource;

/**
 * Listener concerning the selection of markdown files represented by
 * {@code ProjectBrowser} TreeItems.
 *
 * On selection of a file it is loaded in the editor.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public final class SourceFileSelectedListener implements ChangeListener<TreeItem> {

    /**
     * OS-independent line separator.
     */
    private static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * The {@code PageSource} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(SourceFileSelectedListener.class.getName());

    /**
     * The {@code PageSource} model allowing markdown sharing amongst components.
     */
    private final PageSource page;

    public SourceFileSelectedListener(final PageSource page) {
        this.page = page;
    }

    @Override
    public void changed(
            final ObservableValue<? extends TreeItem> observable,
            final TreeItem oldValue,
            final TreeItem newValue) {
        final PathTreeItem selectedTreeItem = (PathTreeItem) newValue;
        final Path selectedPath = selectedTreeItem.getPath();
        if (Files.isRegularFile(selectedPath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                page.getMarkdown().setValue(Files.readAllLines(selectedPath)
                        .stream().collect(Collectors.joining(LINE_SEP)));
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }
}
