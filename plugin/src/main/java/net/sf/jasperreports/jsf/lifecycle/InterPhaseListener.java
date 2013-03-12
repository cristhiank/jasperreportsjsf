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

import javax.faces.component.ContextCallback;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.ReportRenderRequest;

abstract class InterPhaseListener extends AbstractReportPhaseListener
implements ContextCallback {

	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		if (isReportRequest(context)) {
			context.getExternalContext().getRequestMap()
					.remove(Constants.FACES_PHASE_ID);
		}
	}

	public void beforePhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		if (isReportRequest(context)) {
			context.getExternalContext().getRequestMap().put(
					Constants.FACES_PHASE_ID, getPhaseId());
			ReportRenderRequest request = (ReportRenderRequest) 
					context.getExternalContext().getRequest();
			UIViewRoot viewRoot = context.getViewRoot();
	        if (!viewRoot.invokeOnComponent(context, request.getReportClientId(), this)) {
	            throw new NoSuchReportComponentInViewException(request.getReportClientId());
	        }
		}
	}
	
}
