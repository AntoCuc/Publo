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
package org.publo.controller.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileUtilsTest {

    @Test
    public void testGetBasename() throws Exception {
        final String basename = "abc";
        final String extension = ".txt";
        final String result = FileUtils.getBaseName(basename + extension);
        assertEquals(basename, result);
    }

    @Test
    public void testGetBasenameNoExtension() throws Exception {
        final String basename = "abc";
        final String result = FileUtils.getBaseName(basename);
        assertEquals(basename, result);
    }

    @Test
    public void testGetExtension() throws Exception {
        final String basename = "abc";
        final String extension = ".txt";
        final String result = FileUtils.getExtension(basename + extension);
        assertEquals(extension, result);
    }

    @Test
    public void testGetExtensionNoExtension() throws Exception {
        final String basename = "abc";
        final String result = FileUtils.getExtension(basename);
        assertEquals("", result);
    }
}
