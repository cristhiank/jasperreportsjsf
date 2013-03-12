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

import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assume.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class ContextResourceTest {

    @DataPoint
    public static final String VALID_RES_NAME = "/WEB-INF/web.xml";

    @Theory
    public void checkWithValidResource(String name) throws Exception {
        MockFacesEnvironment facesEnv = new MockFacesServletEnvironment();
        FacesContext facesContext = facesEnv.getFacesContext();

        InputStream expectedStream;
        assumeTrue(null != (expectedStream =
                facesContext.getExternalContext().getResourceAsStream(name)));
        URL expectedLocation = facesContext.getExternalContext().getResource(name);

        ContextResource resource = new ContextResource(name);
        assertThat(resource.getName(), sameInstance(name));

        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (Exception e) {
            assumeNoException(e);
        }

        assertThat(stream, notNullValue());

        URL location = null;
        try {
            location = resource.getLocation();
        } catch (Exception e) {
            assumeNoException(e);
        }

        assertThat(location, notNullValue());
        assertThat(location, equalTo(expectedLocation));
        //TODO enable this assertion
        //assertThat(resource.getPath(), equalTo(location.getPath()));

        File path = new File(resource.getPath());
        File pathExpected = new File(location.getPath());
        assertThat(path, equalTo(pathExpected));

        try {
            stream.close();
            expectedStream.close();
        } catch (IOException e) { }

        facesEnv.release();
    }

    @Theory
    public void withoutFacesContextThrowIllegalStateEx(final String name)
            throws Exception {
        ContextResource resource = new ContextResource(name);
        assertThat(resource.getName(), sameInstance(name));

        try {
            resource.getLocation();
            fail("An IllegalStateException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalStateException.class));
        }

        try {
            resource.getInputStream();
            fail("An IllegalStateException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalStateException.class));
        }

        try {
            resource.getPath();
            fail("An IllegalStateException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalStateException.class));
        }
    }
    
}
