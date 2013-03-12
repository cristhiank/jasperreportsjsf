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
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exporter implementation which generates a HTML report.
 *
 * @author A. Alonso Dominguez
 */
public final class HtmlExporter extends ExporterBase {

    /** Content type for this exporter type. */
    public static final ContentType CONTENT_TYPE = new ContentType("text/html");

    /** The Constant ATTR_HTML_HEADER. */
    public static final String ATTR_HTML_HEADER = "HTML_HEADER";
    /** The Constant ATTR_HTML_FOOTER. */
    public static final String ATTR_HTML_FOOTER = "HTML_FOOTER";
    /** The Constant ATTR_IMAGES_DIR. */
    public static final String ATTR_IMAGES_DIR = "IMAGES_DIR";
    /** The Constant ATTR_IMAGES_DIR_NAME. */
    public static final String ATTR_IMAGES_DIR_NAME = "IMAGES_DIR_NAME";
    /** The Constant ATTR_IMAGES_URI. */
    public static final String ATTR_IMAGES_URI = "IMAGES_URI";

    /**
     * @see net.sf.jasperreports.jsf.engine.Exporter#getContentType()
     */
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
        final JRHtmlExporter exporter = new JRHtmlExporter();
        setParameterUsingAttribute(component, exporter,
                JRHtmlExporterParameter.HTML_HEADER, ATTR_HTML_HEADER);
        setParameterUsingAttribute(component, exporter,
                JRHtmlExporterParameter.HTML_FOOTER, ATTR_HTML_FOOTER);
        setParameterUsingAttribute(component, exporter,
                JRHtmlExporterParameter.IMAGES_DIR, ATTR_IMAGES_DIR);
        setParameterUsingAttribute(component, exporter,
                JRHtmlExporterParameter.IMAGES_DIR_NAME, ATTR_IMAGES_DIR_NAME);
        setParameterUsingAttribute(component, exporter,
                JRHtmlExporterParameter.IMAGES_URI, ATTR_IMAGES_URI);
        return exporter;
    }
}
