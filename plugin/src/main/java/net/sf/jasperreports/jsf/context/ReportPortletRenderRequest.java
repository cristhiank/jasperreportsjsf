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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.render.ResponseStateManager;
import javax.portlet.ResourceRequest;
import javax.portlet.faces.Bridge;
import javax.portlet.filter.ResourceRequestWrapper;

import net.sf.jasperreports.jsf.Constants;

/**
 *
 * @author A. Alonso Dominguez
 */
final class ReportPortletRenderRequest extends ResourceRequestWrapper
        implements ReportRenderRequest {

    private final String oldViewId;
    private final String viewState;

    public ReportPortletRenderRequest(ResourceRequest request,
    		String facesMapping, String viewId, String viewState) {
        super(request);
        
        this.oldViewId = (String) request.getAttribute(Bridge.VIEW_ID);
        request.setAttribute(Bridge.VIEW_ID, viewId);
        this.viewState = viewState;
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (values == null || values.length == 0) {
            return null;
        } else {
            return values[0];
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.putAll(super.getParameterMap());
        paramMap.put(ResponseStateManager.VIEW_STATE_PARAM,
                new String[]{viewState});
        return Collections.unmodifiableMap(paramMap);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }

    public String getReportClientId() {
        return getRequest().getParameter(Constants.PARAM_CLIENTID);
    }

    public String getViewId() {
        return (String) getRequest().getAttribute(Bridge.VIEW_ID);
    }

    public void release() {
        getRequest().setAttribute(Bridge.VIEW_ID, oldViewId);
    }

}
