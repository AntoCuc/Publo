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
package org.publo.controller.listener;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.publo.Launcher;

/**
 * Listener concerning the project currently selected.
 *
 * On selection of a file or directory in a different project it will update its
 * Project{@link Path} property.
 *
 * @author Antonio Cucchiara
 * @since 0.4
 */
public class ActiveProjectListener implements ChangeListener<Path> {

    /**
     * The {@code ActiveProjectListener} logger.
     */
    private static final Logger LOGGER
            = Logger.getLogger(ActiveProjectListener.class.getName());

    /**
     * The active project system property key.
     */
    public static final String PROJECT_KEY = "publo.active.project";

    @Override
    public void changed(
            final ObservableValue<? extends Path> observable,
            final Path oldValue,
            final Path newValue) {
        final int projPathNameCount = Launcher.PROJECTS_PATH.getNameCount();
        final Path projectPath = newValue.subpath(0, projPathNameCount + 1);
        final Path projectRootPath = newValue.getRoot().resolve(projectPath);
        LOGGER.log(Level.INFO, "Setting active project {0}", projectRootPath);
        System.setProperty(PROJECT_KEY, projectRootPath.toString());
    }
}
