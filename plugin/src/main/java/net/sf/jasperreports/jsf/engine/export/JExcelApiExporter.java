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

import java.util.Collection;
import java.util.Collections;

import net.sf.jasperreports.jsf.engine.ExporterException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;

/**
 * Exports the report contents to an Excel file using the JExcel API.
 *
 * @author A. Alonso Dominguez
 */
public final class JExcelApiExporter extends AbstractXlsExporter {

    /** MIME type of the exported report. */
    public static final ContentType CONTENT_TYPE = new ContentType("application/vnd.ms-excel");

    /** 'CREATE_CUSTOM_PALLETE' exporter attribute name. */
    public static final String ATTR_CREATE_CUSTOM_PALETTE =
            "CREATE_CUSTOM_PALETTE";

    /**
     * @see net.sf.jasperreports.jsf.engine.Exporter#getContentType()
     */
    public ContentType getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    protected JRXlsAbstractExporter createJRXlsExporter(
            final FacesContext context, final UIReport component)
            throws ExporterException {
        final JRXlsAbstractExporter exporter =
                new net.sf.jasperreports.engine.export.JExcelApiExporter();
        setParameterUsingAttribute(component, exporter,
                JExcelApiExporterParameter.CREATE_CUSTOM_PALETTE,
                ATTR_CREATE_CUSTOM_PALETTE);
        return exporter;
    }
}
