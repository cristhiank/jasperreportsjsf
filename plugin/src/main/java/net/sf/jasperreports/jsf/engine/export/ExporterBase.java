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

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.Exporter;
import net.sf.jasperreports.jsf.engine.ExporterException;
import net.sf.jasperreports.jsf.engine.interop.FacesFileResolver;
import net.sf.jasperreports.jsf.engine.interop.FacesHyperlinkProducerFactory;
import net.sf.jasperreports.jsf.util.Util;

/**
 * Base class for <tt>Exporter</tt> implementations.
 *
 * @author A. Alonso Dominguez
 */
public abstract class ExporterBase implements Exporter {

    public static final String ATTR_CHARACTER_ENCODING = "CHARACTER_ENCODING";
    public static final String ATTR_FILTER = "FILTER";
    public static final String ATTR_IGNORE_PAGE_MARGINS = "IGNORE_PAGE_MARGINS";
    /**
     * The Constant ATTR_END_PAGE_INDEX.
     */
    public static final String ATTR_END_PAGE_INDEX = "END_PAGE_INDEX";
    /**
     * The Constant ATTR_PAGE_INDEX.
     */
    public static final String ATTR_PAGE_INDEX = "PAGE_INDEX";
    /**
     * The Constant ATTR_START_PAGE_INDEX.
     */
    public static final String ATTR_START_PAGE_INDEX = "START_PAGE_INDEX";
    public static final String ATTR_OFFSET_X = "OFFSET_X";
    public static final String ATTR_OFFSET_Y = "OFFSET_Y";

    /**
     * Export.
     *
     * @param context the context
     * @param component the report component
     * @param stream the stream
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ExporterException the exporter exception
     */
    public final void export(final FacesContext context,
            final UIReport component, final OutputStream stream)
            throws IOException, ExporterException {
        JasperPrint print = component.getSubmittedPrint();
        if (print == null) {
            throw new JasperPrintNotFoundException(
                    component.getClientId(context));
        }


        final JRAbstractExporter exporter = createJRExporter(context, component);
        //<--Update to latest version of jasperreports 5.0.1 to support new apis
        exporter.setJasperReportsContext(Util.getJasperReportsContext(component));
        //-->
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);


        Object encoding = component.getAttributes()
                .get(ATTR_CHARACTER_ENCODING);
        if (encoding == null) {
            encoding = context.getExternalContext()
                    .getResponseCharacterEncoding();
        }
        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, encoding);

        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.IGNORE_PAGE_MARGINS,
                ATTR_IGNORE_PAGE_MARGINS);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.END_PAGE_INDEX, ATTR_END_PAGE_INDEX);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.PAGE_INDEX, ATTR_PAGE_INDEX);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.START_PAGE_INDEX, ATTR_START_PAGE_INDEX);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.FILTER, ATTR_FILTER);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.OFFSET_X, ATTR_OFFSET_X);
        setParameterUsingAttribute(component, exporter,
                JRExporterParameter.OFFSET_Y, ATTR_OFFSET_Y);

        JRHyperlinkProducerFactory hpf = new FacesHyperlinkProducerFactory(component);
        exporter.setParameter(
                JRExporterParameter.HYPERLINK_PRODUCER_FACTORY, hpf);

        try {
            exporter.exportReport();
        } catch (final JRException e) {
            throw new ExporterException(e);
        }
    }

    /**
     * Creates the jr exporter.
     *
     * @param context the faces' context
     * @param component the report component
     *
     * @return the jR exporter
     */
    protected abstract JRAbstractExporter createJRExporter(FacesContext context,
            UIReport component)
            throws ExporterException;

    protected void setParameterUsingAttribute(
            final UIReport component,
            final JRExporter exporter,
            final JRExporterParameter param,
            final String attr) {
        Object value;
        if (null != (value = component.getAttributes().get(attr))) {
            exporter.setParameter(param, value);
        }
    }
}
