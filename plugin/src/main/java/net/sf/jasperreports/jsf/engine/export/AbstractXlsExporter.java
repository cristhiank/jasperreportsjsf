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

import net.sf.jasperreports.jsf.engine.ExporterException;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRAbstractExporter;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.jsf.component.UIReport;

@SuppressWarnings("deprecation")
abstract class AbstractXlsExporter extends ExporterBase {

    @Deprecated
    public static final String ATTR_IS_AUTO_DETECT_CELL_TYPE =
            "IS_AUTO_DETECT_CELL_TYPE";
    public static final String ATTR_IS_COLLAPSE_ROW_SPAN =
            "IS_COLLAPSE_ROW_SPAN";
    public static final String ATTR_IS_DETECT_CELL_TYPE =
            "IS_DETECT_CELL_TYPE";
    public static final String ATTR_IS_FONT_SIZE_FIX_ENABLED =
            "IS_FONT_SIZE_FIX_ENABLED";
    public static final String ATTR_IS_IGNORE_CELL_BORDER =
            "IS_IGNORE_CELL_BORDER";
    public static final String ATTR_IS_IGNORE_GRAPHICS =
            "IS_IGNORE_GRAPHICS";
    public static final String ATTR_IS_IMAGE_SIZE_FIX_ENABLED =
            "IS_IMAGE_SIZE_FIX_ENABLED";
    public static final String ATTR_IS_ONE_PAGE_PER_SHEET =
            "IS_ONE_PAGE_PER_SHEET";
    public static final String ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS =
            "IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS";
    public static final String ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS =
            "IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS";
    public static final String ATTR_IS_WHITE_PAGE_BACKGROUND =
            "IS_WHITE_PAGE_BACKGROUND";
    public static final String ATTR_MAXIMUM_ROWS_PER_SHEET =
            "MAXIMUM_ROWS_PER_SHEET";

    @Override
    protected JRAbstractExporter createJRExporter(final FacesContext context,
            UIReport component) {
        final JRXlsAbstractExporter exporter = 
                createJRXlsExporter(context, component);
//        setParameterUsingAttribute(component, exporter,
//                JRXlsAbstractExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
//                ATTR_IS_AUTO_DETECT_CELL_TYPE);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN,
                ATTR_IS_COLLAPSE_ROW_SPAN);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE,
                ATTR_IS_DETECT_CELL_TYPE);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED,
                ATTR_IS_FONT_SIZE_FIX_ENABLED);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER,
                ATTR_IS_IGNORE_CELL_BORDER);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS,
                ATTR_IS_IGNORE_GRAPHICS);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED,
                ATTR_IS_IMAGE_SIZE_FIX_ENABLED);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
                ATTR_IS_ONE_PAGE_PER_SHEET);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
                ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                ATTR_IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                ATTR_IS_WHITE_PAGE_BACKGROUND);
        setParameterUsingAttribute(component, exporter,
                JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET,
                ATTR_MAXIMUM_ROWS_PER_SHEET);
        return exporter;
    }

    protected abstract JRXlsAbstractExporter createJRXlsExporter(
            FacesContext context, UIReport component)
            throws ExporterException;
}
