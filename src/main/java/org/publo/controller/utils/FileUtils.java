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
package org.publo.controller.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.publo.controller.MainViewController;

/**
 * Holds File utilities.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public class FileUtils {

    /**
     * The {@code FileUtils} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(MainViewController.class.getName());

    /**
     * OS-independent line separator.
     */
    private static final String LINE_SEP
            = System.getProperty("line.separator");

    /**
     * Retrieves the base name of a file given its full name.
     *
     * @param fileName to process
     * @return extension stripped file name
     */
    public static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    /**
     * Retrieves the extension of a file given its full name.
     *
     * @param fileName to process
     * @return extension stripped file name
     */
    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index, fileName.length());
        }
    }

    /**
     * Read a file content {@code String} based on {@code Path}
     *
     * @param filePath
     * @return the file content
     */
    public static String readFileContent(Path filePath) {
        try {
            return Files
                    .readAllLines(filePath)
                    .stream()
                    .collect(Collectors.joining(LINE_SEP));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not read file.", ex);
            return "";
        }
    }
}
