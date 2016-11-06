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
package org.publo.controller.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A {@code TreeCell} allowing the creation and renaming of {@code TreeItem}s
 * and the respective underlying binary files.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public final class EditableTreeCell extends TreeCell<String> {

    /**
     * The {@code EditableTreeCell} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(EditableTreeCell.class.getName());

    /**
     * The {@code TextField} to place in the {@code TreeItem} whilst amending.
     */
    private final TextField textField;

    /**
     * Constructs an {@code EditableTreeCell} for a {@code MovableResource}.
     *
     * @param movableResource to relocate
     */
    public EditableTreeCell(final Movable movableResource) {
        this.textField = new TextField() {
            {
                setOnKeyReleased((KeyEvent event) -> {
                    if (KeyCode.CANCEL == event.getCode()) {
                        LOGGER.info("Cancelling rename.");
                        cancelEdit();
                    } else if (KeyCode.ENTER == event.getCode()) {
                        final String cellContent = getText();
                        LOGGER.log(Level.INFO, "Renaming to {0}", cellContent);
                        commitEdit(cellContent);
                        final Path newPath = 
                                movableResource.move(Paths.get(cellContent));
                        PathTreeItem treeItem
                                = (PathTreeItem) getTreeItem();
                        treeItem.setPath(newPath);
                    }
                });
            }
        };
    }

    @Override
    public void startEdit() {
        super.startEdit();
        final String cellText = getText();
        LOGGER.log(Level.INFO, "Editing {0}", cellText);
        textField.setText(cellText);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        LOGGER.log(Level.INFO, "Cancelled.");
        setText(getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(String content, boolean isEmpty) {
        super.updateItem(content, isEmpty);
        if (isEmpty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            textField.setText(content);
            setGraphic(textField);
        } else {
            setText(content);
            setGraphic(getTreeItem().getGraphic());
        }
    }
}
