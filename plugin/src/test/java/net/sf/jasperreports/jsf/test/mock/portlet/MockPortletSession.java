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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletSession implements PortletSession {

    private Map<String, Object> globalAttributeMap =
            new HashMap<String, Object>();
    private Map<String, Object> portletAttributeMap =
            new HashMap<String, Object>();

    private String id = "123";

    private PortletContext portletContext;

    public MockPortletSession() {
        super();
    }

    public MockPortletSession(PortletContext portletContext) {
        super();
        setPortletContext(portletContext);
    }

    public Object getAttribute(String name) {
        return portletAttributeMap.get(name);
    }

    public Object getAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return globalAttributeMap.get(name);
        } else {
            return portletAttributeMap.get(name);
        }
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(portletAttributeMap.keySet());
    }

    public Enumeration<String> getAttributeNames(int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return Collections.enumeration(globalAttributeMap.keySet());
        } else {
            return Collections.enumeration(portletAttributeMap.keySet());
        }
    }

    public long getCreationTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastAccessedTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void invalidate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isNew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String name) {
        portletAttributeMap.remove(name);
    }

    public void removeAttribute(String name, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            globalAttributeMap.remove(name);
        } else {
            portletAttributeMap.remove(name);
        }
    }

    public void setAttribute(String name, Object value) {
        portletAttributeMap.put(name, value);
    }

    public void setAttribute(String name, Object value, int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            globalAttributeMap.put(name, value);
        } else {
            portletAttributeMap.put(name, value);
        }
    }

    public void setMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    public Map<String, Object> getAttributeMap() {
        return portletAttributeMap;
    }

    public Map<String, Object> getAttributeMap(int scope) {
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return globalAttributeMap;
        } else {
            return portletAttributeMap;
        }
    }

}
