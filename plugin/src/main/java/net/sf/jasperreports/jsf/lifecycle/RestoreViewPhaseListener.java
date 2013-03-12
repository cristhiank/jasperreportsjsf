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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.context.ReportRenderRequest;

/**
 * Establishes some changes in the request wrapping it and
 * forcing the faces' lifecycle to restore the lastest view.
 *
 * @author A- Alonso Dominguez
 */
public class RestoreViewPhaseListener extends AbstractReportPhaseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7123125597556150875L;
	private static final Logger logger = Logger.getLogger(
            RestoreViewPhaseListener.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    public RestoreViewPhaseListener() { }

    public void afterPhase(PhaseEvent event) throws FacesException {
        FacesContext context = event.getFacesContext();
        if (isReportRequest(context) && (null == context.getViewRoot())) {
            throw new ReportLifecycleException(
                    "Report holder view couldn't be restored");
        }
    }

    public void beforePhase(PhaseEvent event) throws FacesException {
        FacesContext context = event.getFacesContext();
        JRFacesContext jrContext = JRFacesContext.getInstance(context);
        if (isReportRequest(context)) {
            // Mark request as a postback to force view restoring
            context.getExternalContext().getRequestMap().put(
                    Constants.ATTR_POSTBACK, Boolean.TRUE);

            final ExternalContextHelper helper =
                    jrContext.getExternalContextHelper(context);
            final ReportRenderRequest renderRequest = helper
                    .restoreReportRenderRequest(context.getExternalContext());

            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "JRJSF_0030", new Object[]{
                    renderRequest.getViewId(),
                    renderRequest.getReportClientId()
                });
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
