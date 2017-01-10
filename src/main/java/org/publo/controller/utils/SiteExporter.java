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
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.publo.controller.ProjectBrowserController.PROJECTS_PATH;

/**
 * Exports the markdown to a site.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class SiteExporter {

    private static final Logger LOGGER
            = Logger.getLogger(SiteExporter.class.getName());

    private static final Path TARGET_PATH = PROJECTS_PATH.resolve("target");
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
     * @param template content wrapper
     * @throws IOException
     */
    public static void export(final String template) throws IOException {

        final FileVisitor<Path> projectFileVisitor
                = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                LOGGER.log(Level.INFO, "Visiting directory {0}", dir);
                final Path basePath = PROJECTS_PATH.relativize(dir);
                final Path dirPath = TARGET_PATH.resolve(basePath);
                LOGGER.log(Level.INFO, "Target directory {0}", dirPath);
                if (dir.equals(TARGET_PATH)) {
                    LOGGER.info("Skipping target directory.");
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (!Files.exists(dirPath)) {
                    LOGGER.log(Level.INFO, "{0} does not exist. Creating it.", dirPath);
                    Files.createDirectory(dirPath);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                final Path basePath = PROJECTS_PATH.relativize(file);
                final Path targetPath = TARGET_PATH.resolve(basePath);
                final String fileName = targetPath.getFileName().toString();
                final String baseName = getBaseName(fileName);
                final String pageName = baseName + MARKUP_EXT;
                final Path filePath = targetPath.resolveSibling(pageName);
                final String markdown = new String(Files.readAllBytes(file));
                final String markup = MarkdownParser.parse(markdown);
                final String page = TemplateRenderer.render(template, markup);
                Files.write(filePath, page.getBytes());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public final FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                LOGGER.log(Level.SEVERE, "Failed to export {0}", file);
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(PROJECTS_PATH, projectFileVisitor);
    }

    /**
     * Retrieves the base name of a file given its full name.
     *
     * @param fileName to process
     * @return extension stripped file name
     */
    private static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }
}
