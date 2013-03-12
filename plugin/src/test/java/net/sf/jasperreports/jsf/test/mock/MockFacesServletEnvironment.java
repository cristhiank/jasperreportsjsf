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
package net.sf.jasperreports.jsf.test.mock;

import org.apache.shale.test.mock.MockExternalContext12;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;

/**
 *
 * @author aalonsodominguez
 */
public final class MockFacesServletEnvironment extends MockFacesEnvironment {

    private MockServletContext servletContext;
    private MockServletConfig servletConfig;
    private MockHttpSession session;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private MockExternalContext12 externalContext;

    @Override
    protected void initializeExternalContext() {
        servletContext = new MockServletContext();
        servletContext.setDocumentRoot(getDocumentRoot());
        
        servletConfig = new MockServletConfig(servletContext);

        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(session);
        response = new MockHttpServletResponse();

        externalContext = new MockExternalContext12(servletContext,
                request, response);
    }

    @Override
    protected void releaseExternalContext() {
        servletConfig = null;
        servletContext = null;
        session = null;
        request = null;
        response = null;
        externalContext = null;
    }

    public MockHttpServletRequest getRequest() {
        return request;
    }

    public MockHttpServletResponse getResponse() {
        return response;
    }

    public MockExternalContext12 getExternalContext() {
        return externalContext;
    }

    public MockServletConfig getServletConfig() {
        return servletConfig;
    }

    public MockServletContext getServletContext() {
        return servletContext;
    }

    public MockHttpSession getSession() {
        return session;
    }
   
}
