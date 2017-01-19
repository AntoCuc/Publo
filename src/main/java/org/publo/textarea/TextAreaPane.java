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
package org.publo.textarea;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.publo.controller.utils.FileUtils;
import org.publo.filebrowser.utils.PathTreeItem;

/**
 * A {@code BorderPane} {@code TextArea}.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public class TextAreaPane extends BorderPane implements ChangeListener {

    private final TextArea textArea;

    public TextAreaPane() {
        this.textArea = new TextArea();
        this.textArea.setWrapText(true);
        this.setCenter(this.textArea);
    }

    public void addTextChangeListener(ChangeListener<String> listener) {
        this.textArea.textProperty().addListener(listener);
    }

    public String getText() {
        return this.textArea.textProperty().getValue();
    }

    /**
     * On Selection of a new {@code PathTreeItem} on the file browser. Reload
     * the content of the {@code TextArea}.
     *
     * @param observable not used
     * @param oldValue used to verify the presence of changes
     * @param newValue populate the area
     */
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (!oldValue.equals(newValue)) {
            PathTreeItem pathTreeItem = (PathTreeItem) newValue;
            final String fileContent = FileUtils.readFileContent(pathTreeItem.getPath());
            textArea.textProperty().setValue(fileContent);
        }
    }
}
