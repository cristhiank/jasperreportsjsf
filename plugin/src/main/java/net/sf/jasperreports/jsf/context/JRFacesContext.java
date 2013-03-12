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
import net.sf.jasperreports.jsf.util.Services;

/**
 * The specific plugin's faces' context.
 *
 * @author A. Alonso Dominguez
 */
public abstract class JRFacesContext {

    /** Singleton instance key. */
    protected static final String INSTANCE_KEY =
            JRFacesContext.class.getName();

    /** Default instance. */
    private static final JRFacesContext DEFAULT_JRFACES_CONTEXT =
            new DefaultJRFacesContext();

    /**
     * Obtains a singleton context instance.
     *
     * @param context current faces' context.
     * @return the singleton instance.
     */
    public static JRFacesContext getInstance(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        JRFacesContext instance = (JRFacesContext) context.getExternalContext()
                .getApplicationMap().get(INSTANCE_KEY);
        if (instance == null) {
            instance = Services.chain(JRFacesContext.class,
                    DEFAULT_JRFACES_CONTEXT);
            context.getExternalContext().getApplicationMap()
                    .put(INSTANCE_KEY, instance);
        }
        return instance;
    }

    /**
     * Collection of available source types.
     *
     * @return collection of available source types.
     */
    public abstract Collection<String> getAvailableSourceTypes();

    /**
     * Collection of availbale export formats.
     *
     * @return collection of available export formats.
     */
    public abstract Collection<String> getAvailableExportFormats();

    public abstract Collection<ContentType> getSupportedContentTypes();

    /**
     * Obtains the external context helper instance.
     *
     * @param context current faces' context.
     * @return the external context helper.
     */
    public abstract ExternalContextHelper getExternalContextHelper(
            FacesContext context);

    /**
     * Instantiates a source converter appropiate for the given component.
     *
     * @param context current faces' context.
     * @param component the component which is asking the source converter.
     * @return a source converter instance.
     */
    public abstract SourceConverter createSourceConverter(
            FacesContext context, UIComponent component);

    public abstract ReportConverter createReportConverter(
            FacesContext context, UIReport component);

    /**
     * Creates a new report instance.
     *
     * @param context current faces' context.
     * @param component a report component.
     * @param name resource name.
     * @return a resource instance.
     */
    public abstract Resource createResource(FacesContext context,
            UIComponent component, String name);

    /**
     * Obtains the report's filler instance.
     *
     * @param context current faces' context.
     * @param component the report component.
     * @return a filler instance.
     */
    public abstract Filler getFiller(
            FacesContext context, UIOutputReport component);

    /**
     * Obtains the report's exporter instance.
     *
     * @param context current faces' context.
     * @param component the report component
     * @return an exporter instance.
     */
    public abstract Exporter getExporter(
            FacesContext context, UIOutputReport component);

}
