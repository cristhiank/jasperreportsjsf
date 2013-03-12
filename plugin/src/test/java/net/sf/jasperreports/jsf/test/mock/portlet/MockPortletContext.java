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

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletContext implements PortletContext {

    private Map<String, String> initParameterMap = new HashMap<String, String>();
    private Map<String, Object> attributeMap = new HashMap<String, Object>();
    private Map<String, String> mimeTypeMap = new HashMap<String, String>();

    private String contextName;
    private int minorVersion, majorVersion;

    private File documentRoot;

    private PortletRequestDispatcher dispather;

    public MockPortletContext() {
        this(2, 4);
    }

    public MockPortletContext(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public String getServerInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PortletRequestDispatcher getRequestDispatcher(String path) {
        return dispather;
    }

    public PortletRequestDispatcher getNamedDispatcher(String name) {
        return dispather;
    }

    public void setRequestDispather(PortletRequestDispatcher dispatcher) {
        this.dispather = dispatcher;
    }

    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getMimeType(String file) {
        return mimeTypeMap.get(file);
    }

    public void setMimeType(String file, String mimeType) {
        mimeTypeMap.put(file, mimeType);
    }

    public String getRealPath(String path) {
        File file = new File(documentRoot, path);
        return file.getAbsolutePath();
    }

    public Set<String> getResourcePaths(String path) {
    	File file = new File(documentRoot, path);
    	if (file.isDirectory()) {
    		Set<String> set = new HashSet<String>();
    		for (File f : file.listFiles()) {
    			String name = f.getAbsolutePath();
    			name = name.substring(documentRoot.getAbsolutePath().length());
    			set.add(name);
    		}
    		return Collections.unmodifiableSet(set);
    	} else {
    		String name = file.getAbsolutePath();
    		return Collections.singleton(name.substring(
    				documentRoot.getAbsolutePath().length()));
    	}
    }

    public URL getResource(String path) throws MalformedURLException {
    	File file = new File(documentRoot, path);
    	return file.toURI().toURL();
    }

    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributeMap.keySet());
    }

    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameterMap.keySet());
    }

    public void setInitParameter(String name, String value) {
        initParameterMap.put(name, value);
    }

    public void log(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    public void setAttribute(String name, Object object) {
        attributeMap.put(name, object);
    }

    public String getPortletContextName() {
        return contextName;
    }

    public void setPortletContextName(String contextName) {
        this.contextName = contextName;
    }

    public Enumeration<String> getContainerRuntimeOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDocumentRoot(File documentRoot) {
        this.documentRoot = documentRoot;
    }

}
