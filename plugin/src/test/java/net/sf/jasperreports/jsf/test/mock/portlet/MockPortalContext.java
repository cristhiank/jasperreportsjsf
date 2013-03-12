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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortalContext implements PortalContext {

    private Map<String, String> propertyMap =
            new HashMap<String, String>();
    private List<PortletMode> supportedPortletModes
            = new ArrayList<PortletMode>();
    private List<WindowState> supportedWindowStates
            = new ArrayList<WindowState>();
    private String portalInfo = "MockPortal";

    public String getProperty(String name) {
        return propertyMap.get(name);
    }

    public Enumeration<String> getPropertyNames() {
        return Collections.enumeration(propertyMap.keySet());
    }

    public void setProperty(String name, String value) {
        propertyMap.put(name, value);
    }

    public void addSupportedPortletMode(PortletMode portletMode) {
        supportedPortletModes.add(portletMode);
    }

    public Enumeration<PortletMode> getSupportedPortletModes() {
        return Collections.enumeration(supportedPortletModes);
    }

    public void addSupportedWindowState(WindowState windowState) {
        supportedWindowStates.add(windowState);
    }

    public Enumeration<WindowState> getSupportedWindowStates() {
        return Collections.enumeration(supportedWindowStates);
    }

    public String getPortalInfo() {
        return portalInfo;
    }

    public void setPortalInfo(String portalInfo) {
        this.portalInfo = portalInfo;
    }

}
