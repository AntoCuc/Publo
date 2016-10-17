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
package org.publo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The Browser Applet facilitates the rendering of html components on Swing
 * based UIs.
 * <p>Users of the Browser Applet should:
 * <ol>
 * <li>Construct an instance - {@code new BrowserApplet}</li>
 * <li>Initialise the class' state - {@code #init()}</li>
 * <li>Start the coordinating Thread - {@code #start()}</li>
 * </ol>
 * <p>
 * The class in its nature is blocking so it is recommended to run it in a
 * {@code SwingUtilities#invokeLater()} block. Loading of html should only be
 * called after the above steps.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public class BrowserApplet extends JApplet {

    private WebEngine webEngine;

    /**
     * Initialises the Applet by adding a Java FX Panel, building a View and
     * setting the scene.
     *
     * @see JFXPanel
     * @see WebView
     * @see Scene
     */
    @Override
    public void init() {
        final JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webEngine = webView.getEngine();
            webEngine.loadContent("Ready.");
            fxPanel.setScene(new Scene(webView));
        });
    }

    /**
     * Renders the html. Relies on a browser to be successfully initialised and
     * started.
     *
     * @param html
     */
    public void load(String html) {
        Platform.runLater(() -> {
            webEngine.loadContent(html);
        });
    }
}
