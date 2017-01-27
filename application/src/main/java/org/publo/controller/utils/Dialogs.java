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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import static org.publo.Launcher.BUNDLE;

/**
 * Dialogs.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public class Dialogs {

    /**
     * Dialogs logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(Dialogs.class.getName());

    public static void showHelp() {
        final Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(BUNDLE.getString("publo.about"));
        alert.setHeaderText(BUNDLE.getString("publo.appname"));
        alert.setContentText(BUNDLE.getString("publo.credits"));

        Label label = new Label(BUNDLE.getString("publo.license"));

        TextArea textArea = new TextArea(BUNDLE.getString("publo.license.mit"));
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        Hyperlink link = new Hyperlink();
        link.setText(BUNDLE.getString("publo.readme.label"));
        link.setOnAction((ActionEvent e) -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URI uri = new URI(BUNDLE.getString("publo.readme.link"));
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException exe) {
                    LOGGER.log(Level.SEVERE, "Failed to reach website.", exe);
                }
            } else {
                LOGGER.log(Level.SEVERE, "Browser not supported.");
            }
        });

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        expContent.add(link, 0, 2);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
