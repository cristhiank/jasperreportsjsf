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
package net.sf.jasperreports.jsf.test.mock.portlet;

import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletRequestDispatcher implements PortletRequestDispatcher {

    public enum Action {
        RENDER_INCLUDE, INCLUDE, FORWARD
    }

    private Action performed = null;

    public Action getActionPerformed() {
        return performed;
    }

    public void include(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        performed = Action.RENDER_INCLUDE;
    }

    public void include(PortletRequest request, PortletResponse response)
            throws PortletException, IOException {
        performed = Action.INCLUDE;
    }

    public void forward(PortletRequest request, PortletResponse response)
            throws PortletException, IOException {
        performed = Action.FORWARD;
    }

}
