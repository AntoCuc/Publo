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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import static org.publo.controller.ProjectBrowserController.PROJECTS_PATH;
import org.publo.model.Asset;
import org.publo.model.Page;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class MenubarController implements Initializable {

    private static final Logger LOGGER
            = Logger.getLogger(MenubarController.class.getName());

    private static final String ABOUT_LINK
            = "https://github.com/AntoCuc/Publo/blob/master/README.md";

    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final FileChooser.ExtensionFilter MD_FILTER
            = new FileChooser.ExtensionFilter("Markdown (*.md)", "*.md");

    /**
     * The currently loaded markdown {@code Page}.
     */
    private Page page;

    /**
     * The currently selected {@code Asset}.
     */
    private Asset asset;

    @FXML
    private MenuBar menuBar;

    /**
     * Initialises the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void newProject() throws IOException {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Project name");
        dialog.setTitle("New project...");
        final Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String projectName = result.get();
            Files.createDirectory(PROJECTS_PATH.resolve(projectName));
        }
    }

    @FXML
    public void open() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(MD_FILTER);
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            try {
                page.getMarkdown().setValue(Files.readAllLines(file.toPath())
                        .stream().collect(Collectors.joining(LINE_SEP)));
                asset.setLocation(file.toPath());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void save() {
        Path filePath;
        if (asset.getLocation() == null) {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(MD_FILTER);
            File file = chooser.showSaveDialog(menuBar.getScene().getWindow());
            if (file == null) {
                return;
            }
            filePath = file.toPath();
        } else {
            filePath = asset.getLocation();
        }
        try {
            Files.write(filePath, page.getMarkdown().getValue().getBytes());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void exit() {
        menuBar.getScene().getWindow().hide();
    }

    @FXML
    public void about() {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI(ABOUT_LINK);
                desktop.browse(uri);
            } catch (URISyntaxException | IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to reach website.", e);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Browser not supported.");
        }
    }

    @FXML
    public void setTemplate(ActionEvent event) {
        RadioMenuItem radioMenuItem = (RadioMenuItem) event.getSource();
        page.getTemplate().setValue(radioMenuItem.getText());
    }

    void initPage(Page page) {
        this.page = page;
    }
}
