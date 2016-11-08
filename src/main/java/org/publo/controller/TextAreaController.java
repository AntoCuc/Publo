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
package org.publo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.publo.controller.utils.Updatable;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class TextAreaController implements Initializable, Updatable<String> {

    private StringProperty markdown;

    @FXML
    private TextArea textArea;

    public TextArea getTextArea() {
        return textArea;
    }

    /**
     * Initialises the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initMarkDown(StringProperty md) {
        markdown = md;
    }

    public void updateMarkdown() {
        markdown.setValue(textArea.getText());
    }

    public void updateTextArea() {
        textArea.setText(markdown.getValue());
    }

    /**
     * Updates if necessary.
     *
     * @param to update
     * @return the new value
     */
    @Override
    public String update(String to) {
        if (!to.equals(textArea.getText())) {
            textArea.setText(to);
        }
        return to;
    }
}
