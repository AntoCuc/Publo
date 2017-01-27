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
package org.publo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.publo.controller.utils.Dialogs;
import org.publo.controller.utils.SiteExporter;
import org.publo.filebrowser.FileBrowserPane;

/**
 * JavaFX Application Launcher.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class Launcher extends Application {

    private static final Logger LOGGER
            = Logger.getLogger(Launcher.class.getName());
    
    /**
     * Resource bundle
     */
    public static final ResourceBundle BUNDLE = ResourceBundle
            .getBundle("language");

    /**
     * Directories
     */
    private static final String USER_DIR = System.getProperty("user.home");
    public static final String PROJ_DIR_NAME = ".publo";
    public static final String TEMPLATES_DIR_NAME = "templates";

    /**
     * Paths
     */
    public static final Path PROJECTS_PATH = Paths.get(USER_DIR, PROJ_DIR_NAME);
    public static final Path TEMPLATES_PATH = Paths.get(
            USER_DIR, PROJ_DIR_NAME, TEMPLATES_DIR_NAME);

    @Override
    public void start(final Stage primaryStage) throws Exception {
        /**
         * Initialise the FileBrowser
         */
        System.setProperty(FileBrowserPane.BROWSER_ROOT_KEY, PROJECTS_PATH.toString());
        /**
         * Initialise the file system (first-run only)
         */
        if (!Files.exists(PROJECTS_PATH, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(PROJECTS_PATH);
                Files.createDirectories(TEMPLATES_PATH);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        primaryStage.setTitle(BUNDLE.getString("publo.appname"));
        InputStream imgStream = Launcher.class.getResourceAsStream("/media/page_white.png");
        primaryStage.getIcons().add(new Image(imgStream));
        final URL mainViewFxml = getClass().getResource("/fxml/mainView.fxml");
        final FXMLLoader mainView = new FXMLLoader(mainViewFxml);
        final Scene scene = new Scene(mainView.load());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (evt) -> {
            switch (evt.getCode()) {
                case F1:
                    Dialogs.showHelp();
                    break;
                case F7:
                    SiteExporter.export();
                    break;
                case F11:
                    primaryStage.setFullScreen(true);
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Launcher.launch(args);
    }

}
