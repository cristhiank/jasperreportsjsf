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

import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.ReportConverter;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.resource.UnresolvedResourceException;

/**
 *
 * @author aalonsodominguez
 */
public class MockJRFacesContext extends JRFacesContext {

    private Set<String> availableExportFormats = new HashSet<String>();
    private Set<String> availableDataSourceTypes = new HashSet<String>();
    private Set<ContentType> supportedContentTypes = new HashSet<ContentType>();
    private SourceConverter sourceConverter;
    private ReportConverter reportConverter;
    private ResourceResolver resourceResolver;
    private Filler filler;
    private Exporter exporter;
    private ExternalContextHelper extContextHelper;

    public MockJRFacesContext(FacesContext context) {
        context.getExternalContext().getApplicationMap().put(INSTANCE_KEY, this);
        extContextHelper = new MockExternalContextHelper();
    }

    public SourceConverter getSourceConverter() {
        return sourceConverter;
    }

    public void setSourceConverter(SourceConverter sourceConverter) {
        this.sourceConverter = sourceConverter;
    }

    public ReportConverter getReportConverter() {
        return reportConverter;
    }

    public void setReportConverter(ReportConverter reportConverter) {
        this.reportConverter = reportConverter;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public void setResourceResolver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    public Resource createResource(
            FacesContext context, UIComponent component, String name) {
        if (resourceResolver != null) {
            Resource res = resourceResolver.resolveResource(context, component,
                                                            name);
            if (res == null) {
                throw new UnresolvedResourceException(name);
            }
            return res;
        } else {
            return null;
        }
    }

    public Filler getFiller(
            FacesContext context, UIOutputReport component) {
        if (filler != null) {
            return filler;
        }
        return null;
    }

    public void setFiller(Filler filler) {
        this.filler = filler;
    }

    public ExternalContextHelper getExternalContextHelper(
            FacesContext context) {
        return extContextHelper;
    }

    public void setExternalContextHelper(ExternalContextHelper extContextHelper) {
        this.extContextHelper = extContextHelper;
    }

    public Exporter getExporter(
            FacesContext context, UIOutputReport component) {
        if (exporter != null) {
            return exporter;
        }
        return null;
    }

    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }

    public SourceConverter createSourceConverter(
            FacesContext context, UIComponent component) {
        if (sourceConverter != null) {
            return sourceConverter;
        }
        return null;
    }

    @Override
    public ReportConverter createReportConverter(FacesContext context,
            UIReport component) {
        if (reportConverter != null) {
            return reportConverter;
        }
        return null;
    }

    public Set<String> getAvailableExportFormats() {
        return availableExportFormats;
    }

    public void setAvailableExportFormats(Set<String> availableExportFormats) {
        this.availableExportFormats = availableExportFormats;
    }

    public Set<String> getAvailableSourceTypes() {
        return availableDataSourceTypes;
    }

    public void setAvailableDataSourceTypes(Set<String> availableDataSourceTypes) {
        this.availableDataSourceTypes = availableDataSourceTypes;
    }

    public Set<ContentType> getSupportedContentTypes() {
        return supportedContentTypes;
    }

    public void setSupportedContentTypes(Set<ContentType> supportedContentTypes) {
        this.supportedContentTypes = supportedContentTypes;
    }
}
