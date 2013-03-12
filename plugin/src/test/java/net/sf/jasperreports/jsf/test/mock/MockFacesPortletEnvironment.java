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

import javax.faces.context.ExternalContext;

import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletConfig;
import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletContext;
import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletExternalContext;
import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletRequest;
import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletResponse;
import net.sf.jasperreports.jsf.test.mock.portlet.MockPortletSession;

/**
 *
 * @author aalonsodominguez
 */
public class MockFacesPortletEnvironment extends MockFacesEnvironment {

    private MockPortletContext portletContext;
    private MockPortletConfig portletConfig;
    private MockPortletSession portletSession;
    private MockPortletRequest portletRequest;
    private MockPortletResponse portletResponse;

    private MockPortletExternalContext externalContext;

    @Override
    public ExternalContext getExternalContext() {
        return externalContext;
    }

    @Override
    protected void initializeExternalContext() {
        portletContext = new MockPortletContext();
        portletContext.setDocumentRoot(getDocumentRoot());
        portletConfig = new MockPortletConfig(portletContext);

        portletSession = new MockPortletSession(portletContext);

        externalContext = new MockPortletExternalContext(portletContext,
                portletRequest, portletResponse);
    }

    @Override
    protected void releaseExternalContext() {
        portletContext = null;
        portletConfig = null;
        portletSession = null;
        portletRequest = null;
        portletResponse = null;
    }

	public MockPortletContext getPortletContext() {
		return portletContext;
	}

	public MockPortletConfig getPortletConfig() {
		return portletConfig;
	}

	public MockPortletSession getPortletSession() {
		return portletSession;
	}

	public MockPortletRequest getPortletRequest() {
		return portletRequest;
	}

	public MockPortletResponse getPortletResponse() {
		return portletResponse;
	}

}
