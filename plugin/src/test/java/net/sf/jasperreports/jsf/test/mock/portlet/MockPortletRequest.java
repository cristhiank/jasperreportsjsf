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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;

/**
 *
 * @author aalonsodominguez
 */
public abstract class MockPortletRequest implements PortletRequest {

    private PortalContext portalContext;
    private PortletSession portletSession;
    private PortletPreferences portletPreferences;

    private Map<String, Object> attributeMap =
            new HashMap<String, Object>();
    private Map<String, List<String>> publicParameterMap =
            new HashMap<String, List<String>>();
    private Map<String, List<String>> privateParameterMap =
            new HashMap<String, List<String>>();
    private Map<String, List<String>> propertyMap =
            new HashMap<String, List<String>>();

    private Locale locale = Locale.getDefault();
    private List<Locale> localeList = new ArrayList<Locale>();

    private PortletMode portletMode = PortletMode.VIEW;
    private WindowState windowState = WindowState.NORMAL;

    public boolean isWindowStateAllowed(WindowState state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPortletModeAllowed(PortletMode mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PortletMode getPortletMode() {
        return portletMode;
    }

    public void setPortletMode(PortletMode portletMode) {
        this.portletMode = portletMode;
    }

    public WindowState getWindowState() {
        return windowState;
    }

    public void setWindowState(WindowState windowState) {
        this.windowState = windowState;
    }

    public PortletPreferences getPreferences() {
        return portletPreferences;
    }

    public void setPreferences(PortletPreferences portletPreferences) {
        this.portletPreferences = portletPreferences;
    }

    public PortletSession getPortletSession() {
        return portletSession;
    }

    public PortletSession getPortletSession(boolean create) {
        return portletSession;
    }

    public void setPortletSession(PortletSession portletSession) {
        this.portletSession = portletSession;
    }

    public void addProperty(String name, String value) {
        List<String> list = propertyMap.get(name);
        if (list == null) {
            list = new ArrayList<String>();
            propertyMap.put(name, list);
        }
        list.add(value);
    }

    public String getProperty(String name) {
        List<String> list = propertyMap.get(name);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public Enumeration<String> getProperties(String name) {
        List<String> list = propertyMap.get(name);
        if (list == null) {
            list = Collections.<String>emptyList();
        }
        return Collections.enumeration(list);
    }

    public Enumeration<String> getPropertyNames() {
        return Collections.enumeration(propertyMap.keySet());
    }

    public PortalContext getPortalContext() {
        return portalContext;
    }

    public void setPortalContext(PortalContext portalContext) {
        this.portalContext = portalContext;
    }

    public String getAuthType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributeMap.keySet());
    }

    public String getParameter(String name) {
        List<String> list = publicParameterMap.get(name);
        if (list == null) {
            list = privateParameterMap.get(name);
        }
        if (list == null || list.isEmpty()) {
            return null;
        }

        String value = list.get(0);
        return value;
    }

    public Enumeration<String> getParameterNames() {
        Set<String> names = new HashSet<String>();
        names.addAll(privateParameterMap.keySet());
        names.addAll(publicParameterMap.keySet());
        return Collections.enumeration(names);
    }

    public String[] getParameterValues(String name) {
        Set<String> result = new HashSet<String>();
        if (publicParameterMap.containsKey(name)) {
            List<String> list = publicParameterMap.get(name);
            result.addAll(list);
        }
        if (privateParameterMap.containsKey(name)) {
            List<String> list = publicParameterMap.get(name);
            result.addAll(list);
        }
        return result.toArray(new String[result.size()]);
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        Enumeration<String> names = getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            map.put(key, getParameterValues(key));
        }
        return map;
    }

    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getResponseContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration<String> getResponseContentTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void addLocale(Locale locale) {
        localeList.add(locale);
    }

    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(localeList);
    }

    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getServerName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException ex) {
            return "localhost";
        }
    }

    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getWindowID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, String[]> getPrivateParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, String[]> getPublicParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
