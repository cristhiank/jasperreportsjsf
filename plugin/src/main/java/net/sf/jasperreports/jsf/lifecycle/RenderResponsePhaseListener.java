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
package net.sf.jasperreports.jsf.lifecycle;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.context.ReportRenderRequest;

/**
 * Implementation of the <tt>RENDER_REPORT</tt> phase.
 *
 * @author A. Alonso Dominguez
 */
public final class RenderResponsePhaseListener extends AbstractReportPhaseListener
        implements ContextCallback {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7619710717020985138L;
	/** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            RenderResponsePhaseListener.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    public RenderResponsePhaseListener() { }

    public void afterPhase(PhaseEvent event) throws FacesException {
        final FacesContext context = event.getFacesContext();
        Map<String, Object> requestMap = context
                .getExternalContext().getRequestMap();
        if (!isReportRequest(context) && Boolean.TRUE.equals(
                requestMap.get(Constants.ATTR_REPORT_VIEW))) {
            final JRFacesContext jrContext =
                    JRFacesContext.getInstance(context);
            ExternalContextHelper helper =
                    jrContext.getExternalContextHelper(context);
            Map<String, String> viewCacheMap = helper.getViewCacheMap(
                    context.getExternalContext());

            String viewId = helper.getViewId(context.getExternalContext());
            String viewState = (String) requestMap.get(
                    Constants.ATTR_VIEW_STATE);

            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "JRJSF_0032", new Object[]{
                		viewId, viewState
                });
            }
            viewCacheMap.put(viewId, viewState);
        } else if (isReportRequest(context)) {
            ReportRenderRequest request = (ReportRenderRequest) context
                    .getExternalContext().getRequest();
            request.release();
        }
    }

    public void beforePhase(PhaseEvent event) throws FacesException {
        FacesContext context = event.getFacesContext();
        if (!isReportRequest(context)) {
            return;
        }

        try {
            final ExternalContext extContext = context.getExternalContext();
            final String clientId = extContext
                    .getRequestParameterMap().get(Constants.PARAM_CLIENTID);
            if (clientId == null) {
                throw new MalformedReportURLException("Missed parameter: "
                        + Constants.PARAM_CLIENTID);
            }

            UIViewRoot viewRoot = context.getViewRoot();
            if (!viewRoot.invokeOnComponent(context, clientId, this)) {
                throw new NoSuchReportComponentInViewException(clientId);
            }
        } finally {
            context.responseComplete();
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    public void invokeContextCallback(FacesContext context, UIComponent target) {
        if (context == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("'target' can't be null");
        }
        if (!(target instanceof UIOutputReport)) {
            throw new IllegalArgumentException(
                    "'target' must be a report output component");
        }

        String clientId = target.getClientId(context);
        UIOutputReport report = (UIOutputReport) target;
        logger.log(Level.FINER, "JRJSF_0016", clientId);
        try {
            report.encodeContent(context);
            report.encodeHeaders(context);
        } catch (final IOException e) {
            throw new JRFacesException(e);
        } finally {
            logger.log(Level.FINER, "JRJSF_0017", clientId);
        }
    }
}
