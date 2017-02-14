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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static org.publo.Launcher.PROJECTS_PATH;
import static org.publo.Launcher.TARGET_DIR_NAME;
import static org.publo.Launcher.TEMPLATES_DIR_NAME;

/**
 * Exports the markdown to a site.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class SiteExporter {

    private static final Logger LOGGER
            = Logger.getLogger(SiteExporter.class.getName());

    private static final String MARKDOWN_EXT = ".md";
    private static final String MARKUP_EXT = ".html";

    /**
     * Compiles the content of a project markdown to markup and bundles in a
     * template.
     *
     * Navigating the file-system the exporter will create a counterpart
     * directory structure in a root "target" sub-folder. When encountering a
     * file it will attempt to parse its content as markdown, wrap it in an
     * template and write a markup file.
     *
     */
    public static void export() {

        final FileVisitor<Path> projectFileVisitor
                = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                LOGGER.log(Level.INFO, "Visiting directory {0}", dir);
                final String directoryName = dir.toFile().getName();
                if (TARGET_DIR_NAME.equals(directoryName)
                        || TEMPLATES_DIR_NAME.equals(directoryName)) {
                    LOGGER.log(Level.INFO, "Skipping {0} directory.", dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                final Path basePath = PROJECTS_PATH.resolve(file);
                final Path targetPath
                        = basePath.resolveSibling(TARGET_DIR_NAME);
                if (Files.exists(targetPath)) {
                    LOGGER.log(Level.INFO, "Target path {0} found.",
                            targetPath);
                } else {
                    LOGGER.log(Level.INFO,
                            "The path {0} does not exist. Creating it.",
                            targetPath);
                    Files.createDirectory(targetPath);
                }
                final String fileName = file.getFileName().toString();
                final Path targetFilePath = targetPath.resolve(fileName);
                final String extension = FileUtils.getExtension(fileName);
                if (MARKDOWN_EXT.equals(extension)) {
                    LOGGER.info("Processing markdown resource.");
                    final String baseName = FileUtils.getBaseName(fileName);
                    final String pageName = baseName + MARKUP_EXT;
                    final Path htmlFilePath
                            = targetFilePath.resolveSibling(pageName);
                    final String markdown = new String(Files.readAllBytes(file));
                    final String page = TemplateRenderer.render(markdown);
                    Files.write(htmlFilePath, page.getBytes());
                } else {
                    final Path sourceFilePath = PROJECTS_PATH.resolve(file);
                    LOGGER.log(Level.INFO, "Copying resource {0} to {1}",
                            new Object[]{sourceFilePath, targetFilePath});
                    Files.copy(
                            sourceFilePath,
                            targetFilePath,
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public final FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                LOGGER.log(Level.SEVERE, "Failed to export {0}", file);
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(PROJECTS_PATH, projectFileVisitor);
            final Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Export completed");
            alert.setContentText("Your site has been successfully exported. "
                    + "Find the output in the project target directory");
            alert.showAndWait();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "An error has occured", ex);
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unable to export site");
            alert.setHeaderText("Error whilst exporting your site");
            alert.setContentText("Please check that the " + PROJECTS_PATH
                    + "directory exists and that your have write permissions.");
            alert.showAndWait();
        }
    }
}
