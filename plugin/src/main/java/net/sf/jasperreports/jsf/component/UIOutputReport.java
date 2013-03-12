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
package net.sf.jasperreports.jsf.component;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;

/**
 * Base report class for JSF components that output a report filled with data.
 *
 * @author A. Alonso Dominguez
 */
public class UIOutputReport extends UIReport {

    /** Specified format for the report output. */
    private String format;

    /** Specified exporter class. */
    private Exporter exporter;

    /** Instantiates a new UIOutputReport. */
    public UIOutputReport() {
        super();
    }

    /**
     * Obtains the specified format for the report output.
     *
     * @return format for the report output.
     */
    public final String getFormat() {
        if (format != null) {
            return format;
        }
        final ValueExpression ve = getValueExpression("format");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return format;
        }
    }

    /**
     * Establishes a new value for the report's output.
     *
     * @param format the new value for the report's output
     */
    public final void setFormat(final String format) {
        this.format = format;
    }

    /**
     * Obtains the established exporter.
     *
     * @return the exporter.
     */
    public final Exporter getExporter() {
        if (exporter != null) {
            return exporter;
        }
        ValueExpression ve = getValueExpression("exporter");
        if (ve != null) {
            try {
                return (Exporter) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return exporter;
        }
    }

    /**
     * Establishes a new exporter instance.
     *
     * @param exporter the new exporter instance.
     */
    public final void setExporter(final Exporter exporter) {
        this.exporter = exporter;
    }

    // UIOutputReport encode methods

    /**
     * Encodes the report contents into the current response.
     *
     * @param context current faces' context.
     * @throws IOException when an error happens encoding the results.
     */
    public final void encodeContent(final FacesContext context)
    throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeContent(context, this);
    }

    /**
     * Encodes the report header into the current response.
     *
     * @param context current faces' context.
     * @throws IOException when an error happens encoding the results.
     */
    public final void encodeHeaders(final FacesContext context)
    throws IOException {
        final ReportRenderer renderer = (ReportRenderer) getRenderer(context);
        renderer.encodeHeaders(context, this);
    }

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		format = (String) values[1];
		exporter = (Exporter) values[2];
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = format;
		values[2] = exporter;
		return values;
	}

}
