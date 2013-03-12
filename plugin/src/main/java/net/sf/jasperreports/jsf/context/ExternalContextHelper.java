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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.InvalidEnvironmentException;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 * Helper class that will provide with utility methods related with the
 * treatment of the faces' ExternalContext.
 *
 * @author A. Alonso Dominguez
 */
public abstract class ExternalContextHelper {

    /** Name of the Portlet class. */
    private static final String PORTLET_CLASS = "javax.portlet.Portlet";

    /** Name of the ResourceURL class. */
    private static final String PORTLET_RESOURCEURL_CLASS =
            "javax.portlet.ResourceURL";

    /** Name of the Bridge class. */
    private static final String BRIDGE_CLASS = "javax.portlet.faces.Bridge";

    /**
     * Obtains the application local instance of the ExternalContextHelper.
     *
     * @param context the current ExternalContext
     * @return an instance of the ExternalContextHelper
     */
    protected static ExternalContextHelper newInstance (
            final ExternalContext context)
    throws InvalidEnvironmentException {
        ExternalContextHelper instance = null;
        if (isServletContext(context)) {
            instance = new ServletContextHelper();
        } else if (isPortletAvailable()) {
            String portletVersion = getPortletVersion();

            if (!"2.0".equals(portletVersion)) {
                throw new InvalidEnvironmentException("Incorrect portlet"
                        + " version: " + portletVersion);
            }

            if (!isFacesBridgeAvailable()) {
                throw new InvalidEnvironmentException("Portlet 2.0"
                        + " environment detected but not Faces' bridge has"
                        + " found. Please use a portlet faces bridge"
                        + " compliant with JSR-329.");
            }

            instance = new PortletContextHelper();
        } else {
            throw new IllegalArgumentException(
                    "Unrecognized application context: " + 
                    context.getContext().getClass().getName());
        }
        return instance;
    }

    /**
     * Detects if the faces bridge is available for this application.
     *
     * @return <code>true</code> if the faces bridge is available -
     *         <code>false</code> otherwise
     */
    public static boolean isFacesBridgeAvailable() {
        boolean bridgeAvailable = false;
        try {
            Class.forName(BRIDGE_CLASS);
            bridgeAvailable = true;
        } catch (final ClassNotFoundException e) {
            bridgeAvailable = false;
        } catch (final NoClassDefFoundError e) {
            bridgeAvailable = false;
        }
        return bridgeAvailable;
    }

    /**
     * Detects if the portlet environment is available for this application.
     *
     * @return <code>true</code> if the Portlet environment is available -
     *         <code>false</code> otherwise
     */
    public static boolean isPortletAvailable() {
        boolean portletAvailable = false;
        try {
            Class.forName(PORTLET_CLASS);
            portletAvailable = true;
        } catch (final ClassNotFoundException e) {
            portletAvailable = false;
        } catch (final NoClassDefFoundError e) {
            portletAvailable = false;
        }
        return portletAvailable;
    }

    /**
     * Obtains the current portlet version in use.
     *
     * @return the current portlet version
     */
    public static String getPortletVersion() {
        final boolean portletAvailable = isPortletAvailable();

        String portletVersion = null;
        try {
            Class.forName(PORTLET_RESOURCEURL_CLASS);
            portletVersion = "2.0";
        } catch (final ClassNotFoundException e) {
            portletVersion = portletAvailable ? "1.0" : null;
        } catch (final NoClassDefFoundError e) {
            portletVersion = portletAvailable ? "1.0" : null;
        }

        return portletVersion;
    }

    /**
     * Detects if working with a servlet context.
     *
     * @param context the current ExternalContext
     * @return <code>true</code> if the Portlet context is available -
     *         <code>false</code> otherwise
     */
    public static boolean isServletContext(final ExternalContext context) {
        final Object ctx = context.getContext();
        return (ctx instanceof ServletContext);
    }

    /**
     * Protected constructor to prevent instantiation.
     */
    protected ExternalContextHelper() { }

    public abstract Collection<ContentType> getAcceptedContentTypes(
    		final ExternalContext context);
    
    /**
     * Creates a <code>ReportRenderRequest</code> based on the data code in the
     * current ExternalContext.
     *
     * @param context the current ExternalContext
     * @return A representation of the request render request
     */
    public ReportRenderRequest restoreReportRenderRequest(
            ExternalContext context) {
    	final Configuration config = Configuration.getInstance(context);
    	final String viewId = context.getRequestParameterMap()
		        .get(Constants.PARAM_VIEWID);
		final String viewState = getViewCacheMap(context).get(viewId);
		
		ReportRenderRequest request = createReportRenderRequest(context, 
				config.getDefaultMapping(), viewId, viewState);
		context.setRequest(request);
		return request;
    }

    protected abstract ReportRenderRequest createReportRenderRequest(
    		ExternalContext context, String defaultMapping, 
    		String viewId, String viewState);
    
    /**
     * Gets the request uri.
     *
     * @param context the context
     * @return the request uri
     */
    public abstract String getRequestURI(final ExternalContext context);

    /**
     * Obtains the server name of the current request.
     *
     * @param context the current ExternalContext
     * @return the server name
     */
    public abstract String getRequestServerName(final ExternalContext context);

    /**
     * Obtains the real path name of the resource local to the current context.
     *
     * @param context the current ExternalContext
     * @param name the resource name
     * @return the resource real path name
     */
    public abstract String getResourceRealPath(
            final ExternalContext context, String name);

    /**
     * Obtains the view chache map used to restore views which
     * contains report references.
     *
     * @param context the current ExternalContext
     * @return the view cache map
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Map<String, String> getViewCacheMap(
            final ExternalContext context) {
        Map<String, String> cacheMap = (Map) context.getSessionMap()
                .get(Constants.VIEW_CACHE_KEY);
        if (cacheMap == null) {
            cacheMap = new HashMap<String, String>();
            context.getSessionMap().put(
                    Constants.VIEW_CACHE_KEY, cacheMap);
        }
        return cacheMap;
    }

    /**
     * Obtains the view identifier associated with the current request.
     *
     * @param context the current ExternalContext
     * @return the view identifier
     */
    public final String getViewId(final ExternalContext context) {
        String pathInfo = context.getRequestPathInfo();
        String servletPath = context.getRequestServletPath();
        if (pathInfo == null) {
            String suffix = context.getInitParameter(
                    ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            if (suffix == null || suffix.length() == 0) {
                suffix = ViewHandler.DEFAULT_SUFFIX;
            }
            return servletPath.substring(0, servletPath
                    .lastIndexOf('.')) + suffix;
        } else {
            return pathInfo;
        }
    }

    /**
     * Writes the report headers into the current response using the report
     * renderer.
     *
     * @param context the current ExternalContext
     * @param renderer the report renderer instance
     * @param report the report component instance
     * @throws IOException if any input or output error happens when writing
     *         the report headers
     */
    public abstract void writeHeaders(ExternalContext context,
            ReportRenderer renderer, UIReport report) throws IOException;

    /**
     * Writes the report contents into the context response.
     *
     * @param context the context
     * @param contentType the content type
     * @param data byte array containing the data to be written
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void writeResponse(
    		final ExternalContext context,
            final ContentType contentType, 
            final byte[] data) 
    throws IOException;

}
