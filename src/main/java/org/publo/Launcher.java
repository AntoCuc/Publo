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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.publo.controller.TextAreaController;
import org.publo.controller.MenubarController;
import org.publo.model.Model;

/**
 * JavaFX Application Launcher.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class Launcher extends Application {

    private static final String PREFIX_TITLE = "Publo";

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle(PREFIX_TITLE);
        
        final Model model = new Model();
        
        final BorderPane rootPane = new BorderPane();

        final FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/menubar.fxml"));
        rootPane.setTop(menuLoader.load());
        final MenubarController menuController = menuLoader.getController();
        menuController.initModel(model);

        final FXMLLoader editor = new FXMLLoader(getClass().getResource("/fxml/textArea.fxml"));
        rootPane.setCenter(editor.load());
        final TextAreaController editorController = editor.getController();
        editorController.initModel(model);
        
        model.addObserver(editorController);

        final Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Launcher.launch(args);
    }

}
