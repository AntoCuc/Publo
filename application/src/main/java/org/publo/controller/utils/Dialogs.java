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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
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
import javafx.scene.control.PasswordField;
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

    /**
     * The class delineating CSS errors.
     */
    private static final String ERROR_STYLE_CLASS = "error";

    /**
     * The properties file name.
     */
    private static final String CONFIG_PROP_FILE = "/config.properties";

    /**
     * Project name compliance RegEx.
     */
    private static final String PROJECT_NAME_REGEX = "^[a-zA-Z0-9\\-_]{3,20}$";

    /**
     * FTP server compliance RegEx.
     */
    private static final String FTP_SERVER_REGEX
            = "^ftp(s?):\\/\\/[a-zA-z0-9.:\\/]{5,100}$";

    /**
     * Create new project dialog.
     */
    public static void createNewProject() {
        final Dialog<ProjectDetail> dialog = new Dialog<>();
        dialog.setTitle(BUNDLE.getString("publo.appname"));
        dialog.setHeaderText(BUNDLE.getString("publo.newproject"));

        final ButtonType buttonType
                = new ButtonType(
                        BUNDLE.getString("publo.newproject.button"),
                        ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(buttonType, ButtonType.CANCEL);

        final GridPane grid = new GridPane();
        grid.getStylesheets().add("validation.css");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 10));

        final TextField projectNameField = new TextField();
        projectNameField.setPromptText(
                BUNDLE.getString("publo.projectname.prompt"));

        grid.add(new Label(BUNDLE.getString("publo.projectname.label")), 0, 0);
        grid.add(projectNameField, 1, 0);

        final TextField ftpUrlField = new TextField();
        ftpUrlField.setPromptText(BUNDLE.getString("publo.ftpurl.prompt"));

        grid.add(new Label(BUNDLE.getString("publo.ftpurl.label")), 0, 1);
        grid.add(ftpUrlField, 1, 1);
        grid.add(new Label(BUNDLE.getString("publo.ftpurl.example")), 2, 1);

        final Node createProjectButton
                = dialog.getDialogPane().lookupButton(buttonType);
        createProjectButton.setDisable(true);

        projectNameField.textProperty().addListener((evt) -> {
            final String projectNameFileText = projectNameField.getText();
            if (projectNameFileText.matches(PROJECT_NAME_REGEX)) {
                projectNameField.getStyleClass().remove(ERROR_STYLE_CLASS);
                createProjectButton.setDisable(false);
            } else if (!projectNameField.getStyleClass()
                    .contains(ERROR_STYLE_CLASS)) {
                projectNameField.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        });

        ftpUrlField.textProperty().addListener((evt) -> {
            final String ftpUrlFieldText = ftpUrlField.getText();
            if (ftpUrlFieldText.matches(FTP_SERVER_REGEX)) {
                ftpUrlField.getStyleClass().remove(ERROR_STYLE_CLASS);

            } else if (!ftpUrlField.getStyleClass()
                    .contains(ERROR_STYLE_CLASS)) {
                ftpUrlField.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> projectNameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                return new ProjectDetail(
                        projectNameField.getText(),
                        ftpUrlField.getText());
            }
            return null;
        });

        Optional<ProjectDetail> result = dialog.showAndWait();
        result.ifPresent(projectDetail -> {
            try {
                final String projectName = projectDetail.getProjectName();
                final Path projectPath
                        = Paths.get(USER_DIR, PROJ_DIR_NAME, projectName);
                Files.createDirectory(projectPath);
                LOGGER.log(Level.INFO, "Project {0} directory created.",
                        projectDetail);
                if (projectDetail.getProjectFtpUrl() != null) {
                    Properties p = new Properties();
                    p.setProperty("ftp.url", projectDetail.getProjectFtpUrl());
                    final File propFile
                            = new File(projectPath + CONFIG_PROP_FILE);
                    p.store(new FileOutputStream(propFile), USER_DIR);
                    LOGGER.log(Level.INFO, "Prop file {0} created.", propFile);
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Failed to create project.", ex);
            }
        });
    }

    static Credentials showLoginDialog() {
        final Dialog<Credentials> dialog = new Dialog<>();
        dialog.setTitle(BUNDLE.getString("publo.appname"));
        dialog.setHeaderText(BUNDLE.getString("publo.login"));

        final GridPane grid = new GridPane();
        grid.getStylesheets().add("validation.css");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 10));

        final TextField usernameField = new TextField();
        usernameField.setPromptText(
                BUNDLE.getString("publo.username.prompt"));

        grid.add(new Label(BUNDLE.getString("publo.username.label")), 0, 0);
        grid.add(usernameField, 1, 0);

        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(BUNDLE.getString("publo.password.prompt"));

        grid.add(new Label(BUNDLE.getString("publo.password.label")), 0, 1);
        grid.add(passwordField, 1, 1);

        final ButtonType buttonType
                = new ButtonType(
                        BUNDLE.getString("publo.login.button"),
                        ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes()
                .addAll(buttonType, ButtonType.CANCEL);

        final Node createProjectButton
                = dialog.getDialogPane().lookupButton(buttonType);
        createProjectButton.setDisable(true);

        usernameField.textProperty().addListener((evt) -> {
            final String projectNameFileText = usernameField.getText();
            if (projectNameFileText.matches(PROJECT_NAME_REGEX)) {
                usernameField.getStyleClass().remove(ERROR_STYLE_CLASS);
                createProjectButton.setDisable(false);
            } else if (!usernameField.getStyleClass()
                    .contains(ERROR_STYLE_CLASS)) {
                usernameField.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> usernameField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                return new Credentials(
                        usernameField.getText(),
                        passwordField.getText());
            }
            return null;
        });
        return dialog.showAndWait().orElse(new Credentials("", ""));
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

    /**
     * Wraps the project details.
     *
     * @since 0.4
     */
    private static final class ProjectDetail {

        private final String projectName;
        private final String projectFtpUrl;

        public ProjectDetail(
                final String projectName,
                final String projectFtpUrl) {
            this.projectName = projectName;
            this.projectFtpUrl = projectFtpUrl;
        }

        public String getProjectName() {
            return projectName;
        }

        public String getProjectFtpUrl() {
            return projectFtpUrl;
        }

        @Override
        public String toString() {
            return "Project Name: " + projectName + ", FTP: " + projectFtpUrl;
        }
    }

    /**
     * Credentials allowing access to privileged information/systems.
     *
     * @since 0.4
     */
    static final class Credentials {

        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
