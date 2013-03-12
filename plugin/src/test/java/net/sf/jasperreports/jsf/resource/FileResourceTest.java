/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jasperreports.jsf.TestConstants;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author antonio.alonso
 */
@RunWith(Theories.class)
public class FileResourceTest {

    @DataPoint
    public static final File NULL_FILE = null;

    @DataPoint
    public static File DIRECTORY() {
        return new File(
                System.getProperty(TestConstants.PROP_BASEDIR));
    }

    @DataPoint
    public static File FILE() {
        File basedir = new File(
                System.getProperty(TestConstants.PROP_BASEDIR));
        return new File(basedir, "pom.xml");
    }

    @Theory
    public void fileWorksShouldWorkFine(File file) throws Exception {
        assumeThat(file, notNullValue());
        assumeTrue(!file.isDirectory());
        assumeTrue(file.exists());

        FileResource resource = new FileResource(file);
        assertThat(resource, notNullValue());
        assertThat(resource.getName(), equalTo(file.getAbsolutePath()));
        assertThat(resource.getSimpleName(), equalTo(file.getName()));

        URL expectedLocation = new URL("file://" + file.getAbsolutePath());
        URL location = resource.getLocation();
        assertThat(location, notNullValue());
        assertThat(location, equalTo(expectedLocation));

        String expectedPath = file.getParentFile().getAbsolutePath();
        String path = resource.getPath();
        assertThat(path, notNullValue());
        assertThat(path, equalTo(expectedPath));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (Exception e) {
            fail("No exception expected.");
        }
        assertThat(stream, notNullValue());

        try {
            stream.close();
        } catch (IOException e) { }
    }

    @Theory
    @SuppressWarnings("unused")
    public void nullFileThrowsIllegalArgEx(File file) {
        assumeThat(file, nullValue());

        FileResource resource = null;
        try {
            resource = new FileResource(file);
            fail("An IllegalArgumentException should be thrown.");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void streamForDirectoryThrowsResourceEx(File file) {
        assumeThat(file, notNullValue());
        assumeTrue(file.isDirectory());

        FileResource resource = new FileResource(file);
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            fail("A ResourceException should be thrown.");
        } catch (Exception e) {
            assertThat(e, is(ResourceException.class));
        }
    }

}
