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
package org.publo.filebrowser.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final Map<Watchable, PathTreeItem> CACHE = new HashMap<>();

    private WatchService watchService;

    /**
     * Constructs the {@code FileSystemWatcher} by initialising the
     * {@code WatchService} and configuring it as a daemon.
     */
    public FileSystemWatcher() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            setDaemon(true);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Register a {@code PathTreeItem} on the watch service for a set of events.
     *
     * As of 0.2 the only supported events are {@code ENTRY_CREATE} and
     * {@code ENTRY_DELETE}. All other event kinds are silently ignored.
     *
     * The {@code register} method caches a reference on registering a
     * {@code PathTreeItem}.
     *
     * @param pathTreeItem to register
     * @param events to register for
     */
    public void register(final PathTreeItem pathTreeItem, final Kind<?>... events) {
        LOGGER.log(Level.INFO, "Registering {0}", pathTreeItem);
        try {
            final Path path = pathTreeItem.getPath();
            CACHE.put(path, pathTreeItem);
            path.register(this.watchService, events);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            WatchKey key = this.watchService.take();
            while (key != null) {
                final Watchable path = key.watchable();
                LOGGER.log(Level.INFO, "Watchable item: {0}", path);
                final PathTreeItem parentItem = CACHE.get(path);
                for (WatchEvent event : key.pollEvents()) {
                    final Path relPath = (Path) event.context();
                    LOGGER.log(Level.INFO, "Resource {0}", relPath);
                    final String label = relPath.getFileName().toString();
                    final Kind kind = event.kind();
                    LOGGER.log(Level.INFO, "Event {0} ", kind.name());
                    final Path absPath = parentItem.getPath().resolve(relPath);
                    final List<PathTreeItem> children = parentItem.getChildren();
                    if (ENTRY_CREATE.equals(kind)) {
                        final PathTreeItem newItem
                                = new PathTreeItem(label, absPath);
                        children.add(newItem);
                    } else if (ENTRY_DELETE.equals(kind)) {
                        for (int i = 0; i < children.size(); i++) {
                            final PathTreeItem child = children.get(i);
                            if (absPath.equals(child.getPath())) {
                                children.remove(i);
                            }
                        }
                    }
                }
                key.reset();
                key = this.watchService.take();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
