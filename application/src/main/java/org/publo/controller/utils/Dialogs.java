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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import static org.publo.Launcher.BUNDLE;
import static org.publo.Launcher.PROJ_DIR_NAME;
import static org.publo.Launcher.USER_DIR;

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

    public static void createNewProject() {
        final Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(BUNDLE.getString("publo.appname"));
        dialog.setHeaderText(BUNDLE.getString("publo.newproject"));

        final ButtonType buttonType
                = new ButtonType(
                        BUNDLE.getString("publo.newproject.button"),
                        ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(buttonType, ButtonType.CANCEL);

        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        final TextField projectNameField = new TextField();
        projectNameField.setPromptText(
                BUNDLE.getString("publo.projectname.prompt"));

        grid.add(new Label(BUNDLE.getString("publo.projectname.label")), 0, 0);
        grid.add(projectNameField, 1, 0);

        final Node createProjectButton
                = dialog.getDialogPane().lookupButton(buttonType);
        createProjectButton.setDisable(true);

        projectNameField.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    createProjectButton.setDisable(newValue.trim().isEmpty());
                });
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> projectNameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                return projectNameField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(projectName -> {
            try {
                final Path projectPath
                        = Paths.get(USER_DIR, PROJ_DIR_NAME, projectName);
                Files.createDirectory(projectPath);
                LOGGER.log(Level.INFO, "Project {0} created.", projectName);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Failed to create project.", ex);
            }
        });
    }

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
                } catch (URISyntaxException | IOException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to reach website.", ex);
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
