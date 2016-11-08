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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.publo.controller.utils.Movable;

/**
 * Application {@code PageFile} model.
 *
 * Holds state concerning resources the application manipulates.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public final class PageFile implements Movable {

    /**
     * The {@code PageFile} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(PageFile.class.getName());

    /**
     * The {@code PageFile} current location {@code Path}.
     */
    private Path location;

    public void setLocation(Path location) {
        this.location = location;
    }

    public Path getLocation() {
        return location;
    }

    @Override
    public Path move(Path to) {
        final Path newFilePath = location.getParent().resolve(to);
        LOGGER.log(Level.INFO, "Moving {0} to {1}",
                new Object[]{location, newFilePath});
        try {
            Files.move(location, newFilePath, REPLACE_EXISTING);
            location = newFilePath;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return newFilePath;
    }
}
