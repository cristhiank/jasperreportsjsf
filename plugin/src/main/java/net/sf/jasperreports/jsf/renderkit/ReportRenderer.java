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
package net.sf.jasperreports.jsf.renderkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.config.Configuration;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.context.ContentType;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.util.ComponentUtil;
import net.sf.jasperreports.jsf.util.Util;

/**
 *
 * @author aalonsodominguez
 */
public abstract class ReportRenderer extends Renderer {

    /** The logger instance */
    private static final Logger logger = Logger.getLogger(
            ReportRenderer.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /**
     * Obtain the specific content disposition for this kind of renderer.
     *
     * @return the content disposition.
     */
    public abstract String getContentDisposition();

    /**
     * Encodes the report contents into the current response.
     *
     * @param context the faces' context.
     * @param component the report component being rendered.
     * @throws IOException if something happens when encoding the report.
     */
    public void encodeContent(final FacesContext context,
            final UIOutputReport component)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (component == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        final String clientId = component.getClientId(context);

        final Filler filler = jrContext.getFiller(context, component);
        logger.log(Level.FINE, "JRJSF_0006", clientId);
        filler.fill(context, component);

        final ExternalContextHelper helper = jrContext
                .getExternalContextHelper(context);
        Exporter exporter = component.getExporter();
        if (exporter == null) {
        	exporter = jrContext
                .getExporter(context, component);
        }
        
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
        	ContentType contentType = exporter.getContentType();
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "JRJSF_0010", new Object[]{
                		clientId, contentType});
            }
            exporter.export(context, component, stream);
            helper.writeResponse(context.getExternalContext(),
                    contentType, stream.toByteArray());
        } finally {
            try {
            	stream.close();
            } catch (final IOException e) { ; }
        }
    }

    /**
     * Encodes the HTTP <code>CONTENT-DISPOSITION</code> header string.
     *
     * @param component the report component.
     * @param encoding current response encoding.
     * @return An encoded <code>CONTENT-DISPOSITION</code> header value.
     * @throws IOException if some error happens when encoding the
     *                     report file name.
     */
    public String encodeContentDisposition(
            final UIReport component, final String encoding)
            throws IOException {
        final StringBuffer disposition = new StringBuffer();
        if (component.getName() != null) {
            disposition.append(getContentDisposition());
            disposition.append("; filename=");
            disposition.append(
                    URLEncoder.encode(component.getName(), encoding));
        }
        return disposition.toString();
    }

    /**
     * Encodes the required headers into the current response.
     *
     * @param context the faces' context.
     * @param component the report component.
     * @throws IOException if some error happens writing the headers values.
     */
    public void encodeHeaders(final FacesContext context,
            final UIOutputReport component)
            throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("Faces' context is null");
        }
        if (component == null) {
            throw new IllegalArgumentException("Report component is null");
        }

        final JRFacesContext jrContext = JRFacesContext.getInstance(context);
        final ExternalContextHelper helper = jrContext
                .getExternalContextHelper(context);
        helper.writeHeaders(context.getExternalContext(), this, component);
    }

    /**
     * Builds the report URL which will trigger the <code>RENDER_REPORT</code>
     * phase of the plugin's lifecycle.
     *
     * @param context the faces' context.
     * @param component the report component.
     *
     * @return the report URL.
     */
    public String encodeReportURL(final FacesContext context,
            final UIComponent component)
    throws IOException {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (component == null) {
            throw new IllegalArgumentException();
        }

        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        final Configuration config = Configuration.getInstance(
                context.getExternalContext());
        final ExternalContextHelper helper = jrContext
                .getExternalContextHelper(context);

        final StringBuffer reportURI = new StringBuffer(Constants.BASE_URI);
        String name = ComponentUtil.getStringAttribute(component, "name", null);
        if (name != null) {
        	reportURI.append("/").append(name);
        }
        
        String mapping = Util.getInvocationPath(context);
        if (!config.getFacesMappings().contains(mapping)) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0021", new Object[]{mapping,
                            config.getDefaultMapping()});
            }
            mapping = config.getDefaultMapping();
        }

        if (Util.isPrefixMapped(mapping)) {
            reportURI.insert(0, mapping);
        } else {
            reportURI.append(mapping);
        }

        reportURI.append('?').append(Constants.PARAM_CLIENTID);
        reportURI.append('=').append(component.getClientId(context));
        reportURI.append('&').append(Constants.PARAM_VIEWID);
        reportURI.append('=').append(helper.getViewId(
                context.getExternalContext()));

        final ViewHandler viewHandler = context.getApplication()
                .getViewHandler();
        final String result = context.getExternalContext().encodeResourceURL(
                viewHandler.getResourceURL(context, reportURI.toString()));
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0031", result);
        }
        return result;
    }

    /*private ContentType findAppropiateContentType(FacesContext context, 
    		UIOutputReport component, Exporter exporter) {
    	ContentType result = null;
    	
    	String formatValue = component.getFormat();
    	if (ContentType.isContentType(formatValue)) {
    		ContentType format = new ContentType(formatValue);
    		if (isContentTypeAccepted(context, format)) {
    			result = format;
    		}
    	}
    	
    	if (result == null) {
    		for (ContentType type : exporter.getContentType()) {
    			if (isContentTypeAccepted(context, type)) {
    				result = type;
    				break;
    			}
    		}
    	}
    	
    	return result;
    }
    
    private boolean isContentTypeAccepted(FacesContext context, ContentType contentType) {
    	JRFacesContext jrFacesContext = JRFacesContext.getInstance(context);
    	ExternalContextHelper helper = jrFacesContext.getExternalContextHelper(context);
    	Collection<ContentType> limitedTo = helper.getAcceptedContentTypes(
    			context.getExternalContext());
    	
    	if (limitedTo.isEmpty()) {
    		return true;
    	}
    	
    	for (ContentType accepted : limitedTo) {
    		if (accepted.implies(contentType)) {
    			return true;
    		}
    	}
    	return false;
    }*/
    
    /**
     * Establishes some flags into the faces' context so the plugin's lifecycle
     * can perform the additional tasks needed to handle the
     * <code>RENDER_REPORT</code> phase.
     *
     * @param context the faces' context.
     */
    protected final void registerReportView(final FacesContext context) {
        if (Boolean.TRUE.equals(context.getExternalContext().getRequestMap()
                .get(Constants.ATTR_REPORT_VIEW))) {
            return;
        }

        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        final ExternalContextHelper helper = jrContext
                .getExternalContextHelper(context);
        final String viewId = helper.getViewId(context.getExternalContext());

        context.getExternalContext().getRequestMap().put(
                Constants.ATTR_REPORT_VIEW, Boolean.TRUE);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "JRJSF_0013", viewId);
        }
    }

}
