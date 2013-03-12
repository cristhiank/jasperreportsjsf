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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;
import static net.sf.jasperreports.jsf.test.Matchers.*;

@RunWith(Parameterized.class)
public class URLResourceTest {

    @Parameters
    public static Collection<Object[]> parameters() {
        Object[][] params;
        try {
            URL exists = new URL("http", "jasperreportjsf.sourceforge.net",
                                 "/tld/jasperreports-jsf-1_0.tld");
            URL nonExists = new URL("http", "jasperreportjsf.sourceforge.net",
                                    "/tld/jasperreports-jsf-1_0.xml");
            params = new Object[][]{{exists}, {nonExists}};
        } catch (MalformedURLException e) {
            params = new Object[0][0];
        }
        return Arrays.asList(params);
    }
    
    private final URL url;
    private final URLResource resource;

    public URLResourceTest(URL url) {
        this.url = url;
        resource = new URLResource(url);
    }

    @Test
    public void checkGetName() {
        String expectedName = url.toExternalForm();
        assertThat(resource.getName(), equalTo(expectedName));
    }

    @Test
    public void checkGetSimpleName() {
        String expectedName = url.getPath().substring(
                url.getPath().lastIndexOf('/'));
        assertThat(resource.getSimpleName(), equalTo(expectedName));
    }

    @Test
    public void checkGetLocation() throws Exception {
        assertThat(resource.getLocation(), sameInstance(url));
    }

    @Test
    public void checkGetPath() throws Exception {
        String expectedPath = url.getPath().substring(0,
                                                      url.getPath().lastIndexOf(
                '/'));
        assertThat(resource.getPath(), equalTo(expectedPath));
    }

    @Test
    public void checkGetInputStreamWithValidResource() throws Exception {
        assumeThat(url, existsURL());

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (Exception e) {
            fail("Received unexpected exception: " + e.getClass().getName());
        }
        assertThat(stream, notNullValue());

        try {
            stream.close();
        } catch (IOException e) {
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void checkGetInputStreamWithUnexistantResource() {
        assumeThat(url, not(existsURL()));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
            fail("A I/O exception should be thrown.");
        } catch (Exception e) {
            assertThat(e, is(IOException.class));
        }
    }
}
