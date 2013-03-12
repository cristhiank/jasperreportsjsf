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
package net.sf.jasperreports.jsf.engine.export;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRAbstractExporter;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exporter implementation which generates a PDF report.
 *
 * @author A. Alonso Dominguez
 */
public final class PdfExporter extends ExporterBase {

    public static final ContentType CONTENT_TYPE = new ContentType("application/pdf");
    
    /** The Constant ATTR_IS_COMPRESSED. */
    public static final String ATTR_IS_COMPRESSED = "IS_COMPRESSED";
    /** The Constant ATTR_IS_ENCRYPTED. */
    public static final String ATTR_IS_ENCRYPTED = "IS_ENCRYPTED";
    public static final String ATTR_IS_TAGGED = "IS_TAGGED";

    public ContentType getContentType() {
        return CONTENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.export.ExporterBase#createJRExporter(
     * javax.faces.context.FacesContext)
     */
    @Override
    protected JRAbstractExporter createJRExporter(
            final FacesContext context, final UIReport component) {
        final JRPdfExporter exporter = new JRPdfExporter();
        setParameterUsingAttribute(component, exporter,
                JRPdfExporterParameter.IS_COMPRESSED, ATTR_IS_COMPRESSED);
        setParameterUsingAttribute(component, exporter,
                JRPdfExporterParameter.IS_ENCRYPTED, ATTR_IS_ENCRYPTED);
        setParameterUsingAttribute(component, exporter,
                JRPdfExporterParameter.IS_TAGGED, ATTR_IS_TAGGED);
        return exporter;
    }
}
