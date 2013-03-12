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
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exporter implementation which generates a plain text report.
 *
 * @author A. Alonso Dominguez
 */
public final class TextExporter extends ExporterBase {

    /** The MIME type of this exporter. */
    public static final ContentType CONTENT_TYPE = new ContentType("text/plain");

    /** The Constant ATTR_BETWEEN_PAGES_TEXT. */
    public static final String ATTR_BETWEEN_PAGES_TEXT = "BETWEEN_PAGES_TEXT";
    /** The Constant ATTR_CHARACTER_HEIGHT. */
    public static final String ATTR_CHARACTER_HEIGHT = "CHARACTER_HEIGHT";
    /** The Constant ATTR_CHARACTER_WIDTH. */
    public static final String ATTR_CHARACTER_WIDTH = "CHARACTER_WIDTH";
    /** The Constant ATTR_LINE_SEPARATOR. */
    public static final String ATTR_LINE_SEPARATOR = "LINE_SEPARATOR";
    /** The Constant ATTR_PAGE_HEIGHT. */
    public static final String ATTR_PAGE_HEIGHT = "PAGE_HEIGHT";
    /** The Constant ATTR_PAGE_WIDTH. */
    public static final String ATTR_PAGE_WIDTH = "PAGE_WIDTH";

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
        final JRTextExporter exporter = new JRTextExporter();
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.BETWEEN_PAGES_TEXT,
                ATTR_BETWEEN_PAGES_TEXT);
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.CHARACTER_HEIGHT,
                ATTR_CHARACTER_HEIGHT);
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.CHARACTER_WIDTH, ATTR_CHARACTER_WIDTH);
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.LINE_SEPARATOR, ATTR_LINE_SEPARATOR);
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.PAGE_HEIGHT, ATTR_PAGE_HEIGHT);
        setParameterUsingAttribute(component, exporter,
                JRTextExporterParameter.PAGE_WIDTH, ATTR_PAGE_WIDTH);
        return exporter;
    }
}
