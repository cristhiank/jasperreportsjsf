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
package net.sf.jasperreports.jsf.test.mock;

import java.io.IOException;
import java.util.Collection;

import javax.faces.context.ExternalContext;

import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.ReportRenderRequest;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 *
 * @author aalonsodominguez
 */
public class MockExternalContextHelper extends ExternalContextHelper {

    private String requestServerName;

    @Override
	public Collection<ContentType> getAcceptedContentTypes(ExternalContext context) {
		throw new UnsupportedOperationException("Not supported yet");
	}

    @Override
    public String getRequestURI(ExternalContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestServerName(ExternalContext context) {
        return requestServerName;
    }

    public void setRequestServerName(String requestServerName) {
        this.requestServerName = requestServerName;
    }

    @Override
    public String getResourceRealPath(ExternalContext context, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeHeaders(ExternalContext context, ReportRenderer renderer, UIReport report) 
    throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeResponse(ExternalContext context, ContentType contentType, byte[] data) 
    throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	protected ReportRenderRequest createReportRenderRequest(
			ExternalContext context, String defaultMapping, String viewId,
			String viewState) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
