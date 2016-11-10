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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.publo.controller.utils.Updatable;

/**
 * Generic <code>ChangeListener</code> concerning the conditional amendment of
 * <code>Updatable</code> resources.
 *
 * @author Antonio Cucchiara
 * @param <T> the change listener type
 * @since 0.2
 */
public final class ResourceChangeListener<T> implements ChangeListener<T> {

    private final Updatable<T> updatableResource;

    public ResourceChangeListener(final Updatable<T> updatableResource) {
        this.updatableResource = updatableResource;
    }

    /**
     * Update a resource only if its value has changed.
     * 
     * @param observable value
     * @param oldValue of the resource
     * @param newValue of the resource
     */
    @Override
    public void changed(
            ObservableValue<? extends T> observable,
            T oldValue,
            T newValue) {
        if (!newValue.equals(oldValue)) {
            updatableResource.update(newValue);
        }
    }
}
