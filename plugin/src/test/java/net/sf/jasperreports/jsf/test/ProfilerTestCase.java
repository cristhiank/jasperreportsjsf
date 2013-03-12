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
package net.sf.jasperreports.jsf.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import net.sf.jasperreports.jsf.test.webapp.MockWebappContainer;

import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletUnitClient;

/**
 * The Class JasperReportTest.
 */
public abstract class ProfilerTestCase {

    public static final String PROP_CONTEXT_DIR = "context-dir";
    public static final String PROP_CONTEXT_PATH = "context-path";
    public static final String PROP_CONTAINER_TYPE = "container-type";
    
    private static final String HOSTNAME = "localhost";
    private static final String DEFAULT_WEB_XML = "WEB-INF/web.xml";
    private static final String DEFAULT_PORTLET_XML = "WEB-INF/portlet.xml";

    private static final Logger logger = Logger.getLogger(
            ProfilerTestCase.class.getPackage().getName());

    /** The context dir. */
    private File contextDir;
    /** The context path. */
    private String contextPath;

    private MockWebappContainer container;
    
    private transient ServletUnitClient client;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void startContainer() throws Exception {
        contextDir = new File(System.getProperty(PROP_CONTEXT_DIR));
        contextPath = System.getProperty(PROP_CONTEXT_PATH);
        String containerType = System.getProperty(PROP_CONTAINER_TYPE, "servlet");

        logger.info("Starting web application '" + contextPath
                + "' from directory: " + contextDir);

        if ("servlet".equals(containerType)) {
        	container = MockWebappContainer.getServletContainer(
        			contextDir, contextPath, DEFAULT_WEB_XML);
        } else if ("portlet".equals(containerType)) {
        	container = MockWebappContainer.getPortletContainer(
        			contextDir, contextPath, DEFAULT_WEB_XML, DEFAULT_PORTLET_XML);
        } else {
        	throw new IllegalArgumentException("Illegal container type: " + containerType);
        }
        container.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void stopContainer() throws Exception {
        if (client != null) {
            client.clearContents();
        }
        client = null;

        container.stop();
    }

    protected final ServletUnitClient getClient() {
        if (client == null) {
            client = container.createClient();
        }
        return client;
    }

    protected WebResponse getCurrentPage() {
        final ServletUnitClient client = getClient();
        return client.getCurrentPage();
    }

    protected WebResponse getResponse(final String path)
            throws MalformedURLException, IOException, SAXException {
        final ServletUnitClient client = getClient();

        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(HOSTNAME);
        url.append(contextPath);
        url.append(path);

        logger.finer("Invoking URL: " + url);
        return client.getResponse(url.toString());
    }
}
