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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.xml.namespace.QName;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletConfig implements PortletConfig {

    private String defaultNamespace = "http://example.com/mock";

    private Map<String, String> initParameterMap =
            new HashMap<String, String>();

    private String portletName = "MockPortlet";
    private PortletContext portletContext;

    public MockPortletConfig(PortletContext portletContext) {
        super();
        this.portletContext = portletContext;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameterMap.keySet());
    }

    public void removeInitParameter(String name) {
        initParameterMap.remove(name);
    }

    public void setInitParameter(String name, String value) {
        initParameterMap.put(name, value);
    }

    public Enumeration<String> getPublicRenderParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    public Enumeration<QName> getPublishingEventQNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration<QName> getProcessingEventQNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration<Locale> getSupportedLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, String[]> getContainerRuntimeOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
