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

import static net.sf.jasperreports.jsf.test.Matchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.component.UIComponent;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.component.html.HtmlReportFrame;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import net.sf.jasperreports.jsf.util.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 *
 * @author antonio.alonso
 */
@RunWith(Theories.class)
public class DefaultResourceResolverTest {

    // Data points

    @DataPoint
    public static final String EMPTY_RESOURCE = "";

    @DataPoint
    public static final UIComponent NULL_COMPONENT = null;

    @DataPoint
    public static final String NULL_RESOURCE = null;

    @DataPoint
    public static final String CLASSPATH_RESOURCE_WITHOUT_PREFIX =
            DefaultResourceResolverTest.class.getName().replaceAll("\\.", "/");

    @DataPoint
    public static final String CLASSPATH_RESOURCE_WITH_PREFIX =
            ClasspathResource.PREFIX +
            DefaultResourceResolverTest.class.getName().replaceAll("\\.", "/");

    @DataPoint
    public static final String CONTEXT_RESOURCE = "/WEB-INF/web.xml";

    @DataPoint
    public static final String RELATIVE_TO_REPORT_RESOURCE =
            "LineaFactura.jasper";

    @DataPoint
    public static UIComponent REPORT_COMPONENT() {
        HtmlReportFrame component = new HtmlReportFrame();
        component.setValue("/WEB-INF/report/Factura.jasper");
        return component;
    }

    @DataPoint
    public static final UIComponent SOURCE_COMPONENT = new UISource();

    @DataPoint
    public static final String URL_RESOURCE =
            "http://jasperreportjsf.sourceforge.net/tld/" +
            "jasperreports-jsf-1_0.tld";

    @DataPoint
    public static final String UNEXISTANT_RESOURCE = "resource/doesnt/exists";

    // Instance attributes

    private MockFacesEnvironment facesEnv;

    private DefaultResourceResolver resolver;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        facesEnv.getFacesContext().getViewRoot().setViewId("/viewId.jsp");
        resolver = new DefaultResourceResolver();
    }

    @After
    public void dispose() {
        resolver = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullResourceNameThrowsIllegalArgEx(String resourceName,
            UIComponent component) {
        assumeThat(resourceName, nullValue());

        try {
            resolver.resolveResource(facesEnv.getFacesContext(),
                    component, resourceName);
            fail("An IllegalArgumentException should be thrown.");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void emptyResourceNameThrowsIllegalArgEx(String resourceName,
            UIComponent component) {
        assumeThat(resourceName, notNullValue());
        assumeTrue(resourceName.length() == 0);

        try {
            resolver.resolveResource(facesEnv.getFacesContext(),
                    component, resourceName);
            fail("An IllegalArgumentException should be thrown.");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void urlResolvesURLResource(String resourceName, UIComponent component) {
        assumeNotNull(resourceName);
        assumeThat(resourceName, is(validURL()));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        
        assertThat(res, is(not(nullValue())));
        assertThat(res, is(URLResource.class));
        assertThat(res.getName(), equalTo(resourceName));
    }

    @Theory
    public void classpathPrefixResolvesClasspathResource(String resourceName,
            UIComponent component) {
        assumeNotNull(resourceName);
        assumeThat(resourceName, is(classpathResource()));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertThat(res, is(not(nullValue())));
        assertThat(res, is(ClasspathResource.class));
        assertThat(res.getName(), equalTo(resourceName.substring(
                ClasspathResource.PREFIX.length())));
    }

    @Theory
    public void noClasspathPrefixResolvesClasspathResourceIfItExists(
            String resourceName, UIComponent component) {
        assumeNotNull(resourceName);
        assumeTrue(resourceName.length() > 0);
        assumeThat(resourceName, not(startsWith(ClasspathResource.PREFIX)));

        ClassLoader loader = Util.getClassLoader(this);
        InputStream stream;
        assumeTrue(null != (stream = loader.getResourceAsStream(resourceName)));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertThat(res, is(not(nullValue())));
        assertThat(res, is(ClasspathResource.class));

        try {
            stream.close();
        } catch (IOException e) { }
    }

    @Theory
    public void leadingSlashResolvesContextResource(String resourceName,
            UIComponent component) {
        assumeNotNull(resourceName);
        assumeThat(resourceName, startsWith("/"));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertNotNull(res);
        assertThat(res, is(ContextResource.class));
    }

    @Theory
    public void resourceRelativeToReportComponentResolvesFileResource(
            String resourceName, UIComponent component) {
        assumeNotNull(resourceName);
        assumeTrue(resourceName.length() > 0);
        assumeThat(resourceName, not(startsWith("/")));
        assumeNotNull(component);
        assumeThat(component, is(UIReport.class));
        assumeTrue(!Character.isLowerCase(resourceName.charAt(0)));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);
        
        assertNotNull(res);
        assertThat(res, is(FileResource.class));
        assertThat(res.getSimpleName(), equalTo(resourceName));
    }

    @Theory
    public void resourceRelativeToSourceComponentResolvesNull(
            String resourceName, UIComponent component) {
        assumeNotNull(resourceName);
        assumeTrue(resourceName.length() > 0);
        assumeThat(resourceName, not(startsWith("/")));
        assumeNotNull(component);
        assumeThat(component, is(UISource.class));

        assumeTrue(!Character.isLowerCase(resourceName.charAt(0)));

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertThat(res, nullValue());
    }
    
    @Theory
    public void unexistantResourceResolvesNull(String resourceName,
            UIComponent component) {
        assumeNotNull(resourceName);
        assumeTrue(resourceName.length() > 0);
        assumeThat(resourceName, containsString("doesnt"));
        assumeThat(component, nullValue());

        Resource res = resolver.resolveResource(facesEnv.getFacesContext(),
                component, resourceName);

        assertThat(res, nullValue());
    }

}
