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
package net.sf.jasperreports.jsf.context;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;

import net.sf.jasperreports.jsf.test.mock.MockFacesPortletEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@Ignore("Still under construction")
public class PortletContextHelperTest {

    private MockFacesPortletEnvironment facesEnv;
    private MockJRFacesContext jrContext;
    private PortletContextHelper helper;

    @Before
    public void init() {
        facesEnv = new MockFacesPortletEnvironment();
        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        helper = new PortletContextHelper();
        jrContext.setExternalContextHelper(helper);
    }

    @After
    public void dispose() {
        helper = null;
        jrContext = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Test
    public void getRequestServerName() {
        FacesContext facesContext = facesEnv.getFacesContext();
        PortletRequest request = (PortletRequest)
                facesContext.getExternalContext().getRequest();

        String serverName = helper.getRequestServerName(
                facesContext.getExternalContext());

        assertThat(serverName, is(not(nullValue())));
        assertThat(serverName, equalTo(request.getServerName()));
    }

}
