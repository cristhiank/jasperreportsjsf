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
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 * Portlet implementation of the external context helper.
 *
 * @author A. Alonso Dominguez
 */
final class PortletContextHelper extends ExternalContextHelper {

    /**
     * Protected constructor to prevent instantiation.
     */
    protected PortletContextHelper() {
    }

    @Override
    public Collection<ContentType> getAcceptedContentTypes(
            final ExternalContext context) {
        if ("2.0".equals(getPortletVersion())) {
            ResourceRequest request = (ResourceRequest) context.getRequest();
            Enumeration<String> values = request.getProperties("Accept");
            List<ContentType> list = new ArrayList<ContentType>();
            while (values.hasMoreElements()) {
                ContentType type = new ContentType(values.nextElement());
                list.add(type);
            }
            Collections.sort(list);
            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalStateException(
                    "Only Resource Request/Response state is allowed");
        }
    }

    /**
     * Creates a <code>ReportRenderRequest</code> based on the data code in the
     * current ExternalContext.
     *
     * @param context the current ExternalContext
     * @return A representation of the request render request
     */
    @Override
    public ReportRenderRequest restoreReportRenderRequest(
            final ExternalContext context) {
        if (!"2.0".equals(getPortletVersion())) {
        	throw new IllegalStateException(
            	"Only Resource Request/Response state is allowed");
        }
        return super.restoreReportRenderRequest(context);
    }

    /**
     * Gets the request uri.
     *
     * @param context the context
     * @return the request uri
     */
    @Override
    public String getRequestURI(final ExternalContext context) {
        if ("2.0".equals(getPortletVersion())) {
            final ResourceRequest request =
                    (ResourceRequest) context.getRequest();
            return request.getResourceID();
        } else {
            return null;
        }
    }

    /**
     * Obtains the server name of the current request.
     *
     * @param context the current ExternalContext
     * @return the server name
     */
    @Override
    public String getRequestServerName(final ExternalContext context) {
        final PortletRequest request = (PortletRequest) context.getRequest();
        return request.getServerName();
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
        PortletContext ctx = (PortletContext) context.getContext();
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
        if ("2.0".equals(getPortletVersion())) {
            final ResourceResponse response = (ResourceResponse) context.
                    getResponse();
            response.setProperty("Cache-Type", "no-cache");
            response.setProperty("Expires", "0");

            if (report.getName() != null) {
                response.setProperty("Content-Disposition",
                                     renderer.encodeContentDisposition(report,
                                                                       response.
                        getCharacterEncoding()));
            }
        } else {
            throw new IllegalStateException(
                    "Only Resource Request/Response state is allowed");
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
        if ("2.0".equals(getPortletVersion())) {
            final ResourceResponse response = (ResourceResponse) context.
                    getResponse();
            response.setContentType(contentType.toString());
            response.getPortletOutputStream().write(data);
            response.setContentLength(data.length);
        } else {
            throw new IllegalStateException(
                    "Only Resource Request/Response state is allowed");
        }
    }

	@Override
	protected ReportRenderRequest createReportRenderRequest(
			ExternalContext context, String defaultMapping, String viewId,
			String viewState) {
		ResourceRequest request = (ResourceRequest) context.getRequest();
		return new ReportPortletRenderRequest(request,
                defaultMapping, viewId, viewState);
	}
}
