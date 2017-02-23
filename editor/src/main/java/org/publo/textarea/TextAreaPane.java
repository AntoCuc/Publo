/*
 * The MIT License
 *
 * Copyright 2016-2017 Antonino Cucchiara.
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
package org.publo.textarea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.publo.controller.utils.FileUtils;

/**
 * A {@code BorderPane} {@code TextArea}.
 *
 * @author Antonio Cucchiara
 * @since 0.3
 */
public class TextAreaPane extends BorderPane
        implements ChangeListener<Path> {

    private static final Logger LOGGER
            = Logger.getLogger(TextAreaPane.class.getName());

    private final TextArea textArea;

    /**
     * Property holding the TextArea scroll position in percentage of the page
     * scrolled.
     */
    private final DoubleProperty scrollPercentageProperty;

    private final FileAutoSave autoSave;

    public TextAreaPane() {
        this.textArea = new TextArea();
        this.textArea.setWrapText(true);
        this.textArea.setFont(Font.font("monospaced", 16));
        this.scrollPercentageProperty = new SimpleDoubleProperty(0.0);
        this.scrollPercentageProperty.bind(Bindings.createDoubleBinding(() -> {
            final Node text = textArea.lookup(".content");
            final Node scrollPane = textArea.lookup(".scroll-pane");
            if (text == null || scrollPane == null) {
                return 0.0;
            }
            final double textHeight = text.getLayoutBounds().getHeight();
            final double textAreaHeight = ((ScrollPane) scrollPane)
                    .getViewportBounds().getHeight();
            if (textHeight <= textAreaHeight) {
                return 100.0;
            }
            final double percentage
                    = textArea.getScrollTop() / (textHeight - textAreaHeight);
            LOGGER.log(Level.FINEST, "Percent scrolled {0}", percentage);
            return percentage;
        }, textArea.scrollTopProperty()));
        this.autoSave = new FileAutoSave();
        this.setCenter(this.textArea);
    }

    public void addTextChangeListener(ChangeListener<String> listener) {
        this.textArea.textProperty().addListener(listener);
    }

    public String getText() {
        return this.textArea.textProperty().getValue();
    }

    /**
     * Retrieves a reference to the scroll percentage property.
     *
     * @return the property
     */
    public DoubleProperty scrollPercentageProperty() {
        return this.scrollPercentageProperty;
    }

    /**
     * On Selection of a new {@code PathTreeItem} on the file browser. Reload
     * the content of the {@code TextArea}.
     *
     * @param observable not used
     * @param oldValue used to verify the presence of changes
     * @param newValue populate the area
     */
    @Override
    public void changed(
            final ObservableValue observable,
            final Path oldValue,
            final Path newValue) {
        if (oldValue == null || !oldValue.equals(newValue)) {
            LOGGER.info("Updating the TextArea.");
            try {
                final String fileContent
                        = FileUtils.readFileContent(newValue);
                textArea.textProperty().setValue(fileContent);
                autoSave.start(textArea.textProperty(), newValue);
            } catch (IOException ex) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error prompt");
                alert.setHeaderText("Failed to load the file");
                alert.setContentText("The file selected could not be rendered"
                        + " as text.");
                alert.showAndWait();
            }
        }
    }

    /**
     * File auto save facility. Auto saving occurs every 2500 milliseconds.
     */
    private class FileAutoSave {

        /**
         * The {@code FileAutoSave} logger
         */
        private final Logger LOGGER
                = Logger.getLogger(FileAutoSave.class.getName());

        /**
         * The file content in its most up-to-date form.
         */
        private StringProperty fileContent;

        /**
         * The current file {@code Path}.
         */
        private Path filePath;

        /**
         * The internal {@code Timeline}.
         */
        private final Timeline autoSaveTimer;

        public FileAutoSave() {
            this.autoSaveTimer = new Timeline(new KeyFrame(
                    Duration.millis(2500),
                    ae -> save()));
            this.autoSaveTimer.setCycleCount(Animation.INDEFINITE);
        }

        /**
         * Sets the paths and starts the {@code TimeLine}.
         *
         * Based on the {@code Animation} documentation the states can only be
         * {@link Status#STOPPED}, {@link Status#PAUSED} or
         * {@link Status#RUNNING}.
         *
         * If already started, it will trustingly check if
         * {@link Status#RUNNING} and, if so, temporarily pause.
         *
         * @param fileContent to auto save
         * @param filePath to auto save to
         */
        private void start(
                final StringProperty fileContent,
                final Path filePath) {
            if (Status.RUNNING == autoSaveTimer.getStatus()) {
                autoSaveTimer.pause();
            }
            if (Files.isRegularFile(filePath)) {
                this.fileContent = fileContent;
                this.filePath = filePath;
                this.autoSaveTimer.play();
            }
        }

        /**
         * Saves the {@link fileContent} to the {@link filePath}.
         */
        private void save() {
            LOGGER.log(Level.INFO, "Saving {0}", filePath);
            if (Files.isRegularFile(filePath)) {
                try {
                    Files.write(filePath, fileContent.getValue().getBytes());
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Could not save the file.", ex);
                }
            }
        }
    }
}
