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
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exporter implementation that generates a CSV data file.
 *
 * @author A. Alonso Dominguez
 */
public final class CsvExporter extends ExporterBase {

    /** Exporter content type. */
    public static final ContentType CONTENT_TYPE = new ContentType("text/plain");

    /** The Constant ATTR_FIELD_DELIMITER. */
    public static final String ATTR_FIELD_DELIMITER = "FIELD_DELIMITER";
    /** The Constant ATTR_RECORD_DELIMITER. */
    public static final String ATTR_RECORD_DELIMITER = "RECORD_DELIMITER";

    /**
     * Obtains the exporter content type.
     *
     * @return the exporter content type.
     */
    public ContentType getContentType() {
        return CONTENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.export.ExporterBase#createJRExporter(javax.faces
     * .context.FacesContext)
     */
    @Override
    protected JRAbstractExporter createJRExporter(
            final FacesContext context, final UIReport component) {
        final JRCsvExporter exporter = new JRCsvExporter();
        setParameterUsingAttribute(component, exporter,
                JRCsvExporterParameter.FIELD_DELIMITER, ATTR_FIELD_DELIMITER);
        setParameterUsingAttribute(component, exporter,
                JRCsvExporterParameter.RECORD_DELIMITER, ATTR_RECORD_DELIMITER);
        return exporter;
    }
}
