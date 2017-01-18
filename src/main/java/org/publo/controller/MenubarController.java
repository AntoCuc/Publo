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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import static org.publo.filebrowser.controller.FileBrowserController.PROJECTS_PATH;
import org.publo.controller.utils.FileUtils;
import org.publo.controller.utils.SiteExporter;
import org.publo.model.PageFile;
import org.publo.model.PageSource;
import org.publo.model.PageTemplate;

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
     * The currently loaded markdown {@code PageSource}.
     */
    private PageSource source;

    /**
     * The currently set {@code PageTemplate}
     */
    private PageTemplate template;

    /**
     * The currently selected {@code PageFile}.
     */
    private PageFile sourceFile;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu templateMenu;

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
                source.getMarkdown().setValue(Files.readAllLines(file.toPath())
                        .stream().collect(Collectors.joining(LINE_SEP)));
                sourceFile.setLocation(file.toPath());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void save() {
        Path filePath;
        if (sourceFile.getLocation() == null) {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(MD_FILTER);
            File file = chooser.showSaveDialog(menuBar.getScene().getWindow());
            if (file == null) {
                return;
            }
            filePath = file.toPath();
        } else {
            filePath = sourceFile.getLocation();
        }
        try {
            if (Files.isRegularFile(filePath)) {
                Files.write(filePath, source.getMarkdown().getValue().getBytes());
            }
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
        template.getTemplate().setValue(radioMenuItem.getText());
    }

    @FXML
    public void exportSite() throws IOException {
        final String selectedTemplate = template.getTemplate().getValue();
        SiteExporter.export(selectedTemplate);
    }

    void init(
            final PageSource source,
            final PageTemplate template,
            final PageFile asset) {
        this.source = source;
        this.template = template;
        this.sourceFile = asset;
        this.loadTemplates();
    }

    private void loadTemplates() {
        LOGGER.info("Loading templates.");
        templateMenu.getItems().clear();
        final FileVisitor templatesVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(
                    Path file,
                    BasicFileAttributes attrs) throws IOException {
                final String tplFileName = file.getFileName().toString();
                final String tplName = FileUtils.getBaseName(tplFileName);
                LOGGER.log(Level.INFO, "Creating template entry {0}", tplFileName);
                final RadioMenuItem radioMenuItem = new RadioMenuItem(tplName);
                radioMenuItem.addEventHandler(EventType.ROOT, (Event event) -> {
                    MenubarController.this.setTemplate((ActionEvent) event);
                });
                templateMenu.getItems().add(radioMenuItem);
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(SiteExporter.TEMPLATES_PATH, templatesVisitor);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Templates could not be loaded.{0}", ex.getCause());
        }
    }
}
