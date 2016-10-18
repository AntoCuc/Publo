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

import java.net.URL;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.pegdown.PegDownProcessor;
import org.publo.controller.TextAreaController;
import org.publo.controller.MenubarController;
import org.publo.controller.WebViewController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * JavaFX Application Launcher.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class Launcher extends Application {

    private static final String PREFIX_TITLE = "Publo";
    private final static PegDownProcessor PROCESSOR = new PegDownProcessor();

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle(PREFIX_TITLE);

        final StringProperty markdown = new SimpleStringProperty();

        final BorderPane rootPane = new BorderPane();
        final GridPane gridPane = new GridPane();
        final ColumnConstraints colConst = new ColumnConstraints();
        colConst.setPercentWidth(50);
        gridPane.getColumnConstraints().add(colConst);
        rootPane.setCenter(gridPane);

        final URL textAreaFxml = getClass().getResource("/fxml/textArea.fxml");
        final FXMLLoader textArea = new FXMLLoader(textAreaFxml);
        gridPane.add(textArea.load(), 0, 0);
        final TextAreaController textAreaController = textArea.getController();
        textAreaController.initMarkDown(markdown);

        final URL webViewFxml = getClass().getResource("/fxml/webView.fxml");
        final FXMLLoader webView = new FXMLLoader(webViewFxml);
        gridPane.add(webView.load(), 1, 0);
        final WebViewController webViewController = webView.getController();

        markdown.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("/templates/");
            resolver.setTemplateMode(TemplateMode.HTML);
            resolver.setSuffix(".html");
            final TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);
            final Context context = new Context();
            context.setVariable("main", PROCESSOR.markdownToHtml(newValue));
            String markup = templateEngine.process("default", context);
            webViewController.updateWebView(markup);
        });

        final URL menuBarFxml = getClass().getResource("/fxml/menubar.fxml");
        final FXMLLoader menuBarLoader = new FXMLLoader(menuBarFxml);
        rootPane.setTop(menuBarLoader.load());
        final MenubarController menuBarController = menuBarLoader.getController();
        menuBarController.initMarkdown(markdown);
        menuBarController.initTextArea(textAreaController);

        final Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Launcher.launch(args);
    }

}
