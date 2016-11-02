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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread watching the registered paths.
 *
 * @author Antonio Cucchiara
 * @since 0.2
 */
public class FileSystemWatcher extends Thread {

    private static final Logger LOGGER
            = Logger.getLogger(FileSystemWatcher.class.getName());

    private final WatchService watchService;
    private final Callable<Path> callable;

    public FileSystemWatcher(final Callable<Path> callable) throws IOException {
        this.callable = callable;
        this.watchService = FileSystems.getDefault().newWatchService();
        setDaemon(true);
    }

    public void register(final Path path, final Kind<?>... events)
            throws IOException {
        path.register(this.watchService, events);
    }

    @Override
    public void run() {
        try {
            WatchKey key = this.watchService.take();
            while (key != null) {
                for (WatchEvent event : key.pollEvents()) {
                    //Path directoryNode = (Path) event.context();
                    callable.call();
                }
                key.reset();
                key = this.watchService.take();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
