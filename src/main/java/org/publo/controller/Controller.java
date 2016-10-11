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
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import org.publo.model.Model;
import org.publo.view.AboutDialog;
import org.publo.view.View;

/**
 * Event flow coordinating controller.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class Controller {

    private static final Logger LOGGER
            = Logger.getLogger(Controller.class.getName());

    private static final String HELP_SYSTEM_LINK
            = "https://github.com/AntoCuc/Publo/blob/master/README.md";

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void initialise() {
        view.getNewMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                model.newFile();
                view.updatePreview();
                view.updateTitle();
            }
        });

        view.getOpenItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(view);
                File selectedFile = fileChooser.getSelectedFile();
                model.open(selectedFile);
                view.getTextArea().setText(model.getMarkdown());
                view.updatePreview();
                view.updateTitle();
            }
        });

        view.getSaveMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(model.getOpenFile());
                fileChooser.showSaveDialog(view);
                File selectedFile = fileChooser.getSelectedFile();
                model.save(selectedFile);
                view.updateTitle();
            }
        });

        view.getExitMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        view.getHelpContentsMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        URI uri = new URI(HELP_SYSTEM_LINK);
                        desktop.browse(uri);
                    } catch (URISyntaxException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Failed to reach website.", e);
                    }
                } else {
                    LOGGER.log(Level.SEVERE, "Browser not supported.");
                }
            }
        });

        view.getAboutMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                AboutDialog aboutDialog = new AboutDialog(view, false);
                aboutDialog.setVisible(true);
            }
        });

        view.getTextArea().addCaretListener((CaretEvent e) -> {
            JTextArea textArea = (JTextArea) e.getSource();
            model.update(textArea.getText());
            view.updatePreview();
            view.updateTitle();
        });

        EventQueue.invokeLater(() -> {
            model.newFile();
            view.updatePreview();
            view.updateTitle();
            view.setVisible(true);
        });
    }

}
