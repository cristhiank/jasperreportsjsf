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

import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.convert.ReportConverter;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.resource.Resource;

/**
 *
 * @author A. Alonso Dominguez
 */
public abstract class JRFacesContextWrapper extends JRFacesContext {

    @Override
    public ReportConverter createReportConverter(FacesContext context, UIReport component) {
        return getWrapped().createReportConverter(context, component);
    }

    @Override
    public Resource createResource(FacesContext context, UIComponent component, String name) {
        return getWrapped().createResource(context, component, name);
    }

    @Override
    public SourceConverter createSourceConverter(FacesContext context, UIComponent component) {
        return getWrapped().createSourceConverter(context, component);
    }

    @Override
    public Collection<String> getAvailableExportFormats() {
        return getWrapped().getAvailableExportFormats();
    }

    @Override
    public Collection<String> getAvailableSourceTypes() {
        return getWrapped().getAvailableSourceTypes();
    }

    @Override
    public Exporter getExporter(FacesContext context, UIOutputReport component) {
        return getWrapped().getExporter(context, component);
    }

    @Override
    public ExternalContextHelper getExternalContextHelper(FacesContext context) {
        return getWrapped().getExternalContextHelper(context);
    }

    @Override
    public Filler getFiller(FacesContext context, UIOutputReport component) {
        return getWrapped().getFiller(context, component);
    }

    @Override
    public Collection<ContentType> getSupportedContentTypes() {
        return getWrapped().getSupportedContentTypes();
    }
    
    protected abstract JRFacesContext getWrapped();
    
}
