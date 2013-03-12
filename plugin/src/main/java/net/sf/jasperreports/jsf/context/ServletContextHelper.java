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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 * Servlet implementation of the external context helper.
 *
 * @author A. Alonso Dominguez
 */
final class ServletContextHelper extends ExternalContextHelper {
	
    /**
     * Private constructor to prevent instantiation.
     */
    protected ServletContextHelper() { }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ContentType> getAcceptedContentTypes(
        final ExternalContext context) {
    	final HttpServletRequest request = (HttpServletRequest)
            context.getRequest();
    	
    	Enumeration<String> values = request.getHeaders("Accept");
    	List<ContentType> list = new ArrayList<ContentType>();
    	while (values.hasMoreElements()) {
            ContentType type = new ContentType(values.nextElement());
            list.add(type);
    	}
    	Collections.sort(list);
    	
    	return Collections.unmodifiableList(list);
    }
    
    /**
     * Obtains the server name of the current request.
     *
     * @param context the current ExternalContext
     * @return the server name
     */
    @Override
    public String getRequestServerName(final ExternalContext context) {
        final HttpServletRequest request = (HttpServletRequest)
                context.getRequest();
        return request.getServerName();
    }

    /**
     * Gets the request uri.
     *
     * @param context the context
     * @return the request uri
     */
    @Override
    public String getRequestURI(final ExternalContext context) {
        final HttpServletRequest request = (HttpServletRequest)
                context.getRequest();
        return request.getRequestURI();
    }

    /**
     * Obtains the real path name of the resource local to the current context.
     *
     * @param context the current ExternalContext
     * @param name the resource name
     * @return the resource real path name
     */
    @Override
    public String getResourceRealPath(final ExternalContext context,
            final String name) {
        final ServletContext ctx = (ServletContext) context.getContext();
        return ctx.getRealPath(name);
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
    @Override
    public void writeHeaders(final ExternalContext context,
            final ReportRenderer renderer, final UIReport report)
            throws IOException {
        final HttpServletResponse response = (HttpServletResponse)
                context.getResponse();
        response.setHeader("Cache-Type", "no-cache");
        response.setHeader("Expires", "0");

        if (report.getName() != null) {
            response.setHeader("Content-Disposition", 
                    renderer.encodeContentDisposition(report,
                    response.getCharacterEncoding()));
        }
    }

    /**
     * Writes the report contents into the context response.
     *
     * @param context the context
     * @param contentType the content type
     * @param data the data
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void writeResponse(final ExternalContext context,
            final ContentType contentType, final byte[] data) 
    throws IOException {
        final HttpServletResponse response = (HttpServletResponse)
                context.getResponse();
        response.setContentType(contentType.toString());
        response.getOutputStream().write(data);
        response.setContentLength(data.length);
    }
    
    protected ReportRenderRequest createReportRenderRequest(
    		ExternalContext context, String defaultMapping, 
    		String viewId, String viewState) {
    	HttpServletRequest request = (HttpServletRequest) context.getRequest();
        return new ReportHttpRenderRequest(request, viewId,
                defaultMapping, viewState);
    }
}
