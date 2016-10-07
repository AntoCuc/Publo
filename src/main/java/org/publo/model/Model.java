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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
public final class Model extends Observable {

    private static final Logger LOGGER
            = Logger.getLogger(Model.class.getName());
    private final static PegDownProcessor PROCESSOR = new PegDownProcessor();
    public static final String LINE_SEP = System.getProperty("line.separator");

    private String markdown = "";

    public void newFile() {
        markdown = "";
        setChanged();
        notifyObservers(markdown);
    }

    public void openFile(InputStream inputStream) {
        markdown = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(LINE_SEP));
        setChanged();
        notifyObservers(markdown);
    }

    public void saveFile(OutputStream outputStream) {
        try {
            outputStream.write(markdown.getBytes());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public String parse() {
        return PROCESSOR.markdownToHtml(markdown);
    }

    public void updateSource(String text) {
        this.markdown = text;
    }

    public String getSource() {
        return markdown;
    }
}
