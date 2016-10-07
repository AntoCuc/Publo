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

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import org.publo.model.Model;
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

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void initialise() {
        model.addObserver(view);
        
        view.getNewMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                model.newFile();
            }
        });

        view.getOpenItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showOpenDialog(view);
                    File selectedFile = fileChooser.getSelectedFile();
                    model.openFile(new FileInputStream(selectedFile));
                } catch (FileNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        });

        view.getSaveMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showSaveDialog(view);
                    File selectedFile = fileChooser.getSelectedFile();
                    model.saveFile(new FileOutputStream(selectedFile));
                } catch (FileNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
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
            public void mousePressed(MouseEvent e) {
                view.showHelpContents();
            }
        });

        view.getAboutMenuItem().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                view.showAbout();
            }
        });

        view.getTextArea().addCaretListener((CaretEvent e) -> {
            JTextArea textArea = (JTextArea) e.getSource();
            model.updateSource(textArea.getText());
        });

        view.getTabbedPanel().addChangeListener((ChangeEvent e) -> {
            JTabbedPane sourcePanel = (JTabbedPane) e.getSource();
            int selectedIndex = sourcePanel.getSelectedIndex();
            String selectedTabTitle = sourcePanel.getTitleAt(selectedIndex);
            if ("Preview".equals(selectedTabTitle)) {
                String parsedContent = model.parse();
                view.getEditorPanel().setText(parsedContent);
            }
        });

        EventQueue.invokeLater(() -> {
            view.setVisible(true);
        });
    }

}
