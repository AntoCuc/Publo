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

import org.publo.model.Asset;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import org.publo.controller.utils.PathTreeItem;

/**
 * Listener concerning the selection of a resource represented by
 * {@code ProjectBrowser} TreeItems.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public final class SelectedAssetListener implements ChangeListener<TreeItem> {

    /**
     * The {@code SelectedAssetListener} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(SelectedAssetListener.class.getName());

    private final Asset asset;

    public SelectedAssetListener(final Asset asset) {
        this.asset = asset;
    }

    @Override
    public void changed(
            ObservableValue<? extends TreeItem> observable,
            TreeItem oldValue,
            TreeItem newValue) {
        final PathTreeItem selectedTreeItem = (PathTreeItem) newValue;
        final Path selectedPath = selectedTreeItem.getPath();
        LOGGER.log(Level.INFO, "Selected asset path {0}", selectedPath);
        asset.setLocation(selectedPath);
    }
}
