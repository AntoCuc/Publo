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
package org.publo.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.pegdown.PegDownProcessor;

/**
 * Business logic container.
 *
 * @author Antonio Cucchiara
 * @since 0.1
 */
public final class Model {

    private static final Logger LOGGER
            = Logger.getLogger(Model.class.getName());
    private final static PegDownProcessor PROCESSOR = new PegDownProcessor();
    public static final String LINE_SEP = System.getProperty("line.separator");
    public static final File DEFAULT_FILE = new File("unnamed.md");

    private File openFile;
    private String markdown;
    private String markup;

    public Model() {
        newFile();
    }

    public final void newFile() {
        open(DEFAULT_FILE);
    }

    public void open(File file) {
        if (file.equals(DEFAULT_FILE)) {
            openFile = file;
            markdown = "";
        } else {
            try {
                this.markdown = Files.readAllLines(file.toPath())
                        .stream().collect(Collectors.joining(LINE_SEP));
                openFile = file;
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void save(File file) {
        try {
            openFile = file;
            Files.write(file.toPath(), markdown.getBytes());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void update(String markdown) {
        this.markdown = markdown;
        this.markup = PROCESSOR.markdownToHtml(markdown);
    }

    public String getMarkdown() {
        return markdown;
    }

    public String getMarkup() {
        return markup;
    }

    public File getOpenFile() {
        return openFile;
    }
    
    public String getFileName() {
        return openFile.getName();
    }
}
