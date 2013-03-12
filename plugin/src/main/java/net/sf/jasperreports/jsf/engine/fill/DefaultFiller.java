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
package net.sf.jasperreports.jsf.engine.fill;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.JRFiller;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.component.UISubreport;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.ConnectionWrapper;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.FillerException;
import net.sf.jasperreports.jsf.engine.Filler;
import net.sf.jasperreports.jsf.engine.interop.FacesFileResolver;
import net.sf.jasperreports.jsf.engine.interop.FacesRepositoryService;
import net.sf.jasperreports.jsf.util.Util;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;
import net.sf.jasperreports.repo.RepositoryService;

/**
 * Default Filler implementation.
 *
 * @author A. Alonso Dominguez
 */
public class DefaultFiller implements Filler {

    public static final String SUBREPORT_PARAMETER_SEPARATOR = ".";
    // Report Parameters
    /**
     * The Constant PARAM_REPORT_CLASSLOADER.
     */
    public static final String PARAM_REPORT_CLASSLOADER = "REPORT_CLASS_LOADER";
    /**
     * The Constant PARAM_REPORT_LOCALE.
     */
    public static final String PARAM_REPORT_LOCALE = "REPORT_LOCALE";
    public static final String PARAM_APPLICATION_SCOPE = "APPLICATION_SCOPE";
    public static final String PARAM_SESSION_SCOPE = "SESSION_SCOPE";
    public static final String PARAM_REQUEST_SCOPE = "REQUEST_SCOPE";
    /**
     * The logger.
     */
    private static final Logger logger = Logger.getLogger(
            DefaultFiller.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /**
     * Fill the report object with data comming from the submitted source object
     * (if specified).
     *
     * @param context current faces' context.
     * @param component report component.
     * @throws FillerException if filler throws any exception.
     */
    public final void fill(final FacesContext context, final UIReport component)
            throws FillerException {
        final Map<String, Object> params =
                buildParamMap(context, component);
        JasperPrint print = doFill(context, component, params);
        component.setSubmittedPrint(print);
    }

    /**
     * Builds the parameter map that will be given to the JasperEngine when
     * generating the report.
     * <p>
     * The parameter map is built using
     *
     * @param context the faces' context
     * @param component the report component
     *
     * @return the parameter map analyzed from the component parameters.
     */
    protected Map<String, Object> buildParamMap(final FacesContext context,
            final UIReport component)
            throws FillerException {
        final String reportName = getStringAttribute(component, "name",
                component.getClientId(context));
        logger.log(Level.FINER, "JRJSF_0003", reportName);

        final Map<String, Object> parameters = new HashMap<String, Object>();

        // Build param map using component's child parameters and subreports
        processParameterMap(context, component, parameters, null);

        // Include implicit parameters
        processImplicitParameters(context, component, parameters);

        return parameters;
    }

    /**
     * Performs the internal fill operation.
     *
     * @param context current faces' context.
     * @param component report component
     * @param parameters report parameters.
     * @return the generated <tt>JasperPrint</tt> result.
     * @throws FillerException if some error happens.
     */
    protected JasperPrint doFill(FacesContext context, UIReport component,
            Map<String, Object> parameters)
            throws FillerException {
        JasperReport report = component.getSubmittedReport();
        if (report == null) {
            throw new IllegalStateException("Found a null report object previous to fill it.");
        }

        JRBaseFiller jrFiller;
        try {
            //<--Update to latest version of jasperreports 5.0.1 to support new apis
            jrFiller = JRFiller.createFiller(Util.getJasperReportsContext(component), report);
            //-->
        } catch (JRException e) {
            throw new FillerException(e);
        }

        Source reportSource = findReportSource(component);
        JasperPrint print = null;
        try {
            if (reportSource == null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "JRJSF_0045", component.getClientId(context));
                }
                print = jrFiller.fill(parameters);
            } else {
                print = printWithFiller(context, jrFiller, reportSource, parameters);
            }
        } catch (final JRException e) {
            throw new FillerException(e);
        } finally {
            if (reportSource != null) {
                try {
                    reportSource.dispose();
                } catch (Exception e) {;
                }
                reportSource = null;
            }
        }

        if (print == null) {
            throw new FillerException("Couldn't fill report template with data.");
        }

        return print;
    }

    protected JasperPrint printWithFiller(FacesContext context,
            JRBaseFiller filler, Source source, Map<String, Object> parameters)
            throws JRException {
        JasperPrint print = null;
        if (source instanceof ConnectionWrapper) {
            print = filler.fill(parameters, ((ConnectionWrapper) source).getConnection());
        } else if (source instanceof JRDataSourceWrapper) {
            print = filler.fill(parameters, ((JRDataSourceWrapper) source).getDataSource());
        }
        return print;
    }

    private Source findReportSource(UIReport report) {
        Source result = report.getSubmittedSource();
        if (result == null) {
            UISource source = null;
            for (UIComponent component : report.getChildren()) {
                if (component instanceof UISource) {
                    source = (UISource) component;
                    break;
                }
            }

            if (source != null) {
                result = source.getSubmittedSource();
            }
        }
        return result;
    }

    private void processImplicitParameters(FacesContext context,
            UIReport component, Map<String, Object> parameters) {
        // Specific component parameters
        parameters.put(PARAM_REPORT_CLASSLOADER,
                Util.getClassLoader(component));
        parameters.put(PARAM_REPORT_LOCALE,
                context.getViewRoot().getLocale());

        parameters.put(PARAM_APPLICATION_SCOPE,
                context.getExternalContext().getApplicationMap());
        parameters.put(PARAM_SESSION_SCOPE,
                context.getExternalContext().getSessionMap());
        parameters.put(PARAM_REQUEST_SCOPE,
                context.getExternalContext().getRequestMap());
    }

    /**
     * Builds parameter map recursively through the report/subreport tree.
     *
     * @param context cuurent faces' context.
     * @param component report component.
     * @param parameters report parameters.
     * @param prefix parent's report name, used as a parameter prefix.
     */
    private void processParameterMap(FacesContext context, UIReport component,
            Map<String, Object> parameters, String prefix) {
        for (final UIComponent kid : component.getChildren()) {
            if (kid instanceof UISubreport) {
                final UISubreport subreport = (UISubreport) kid;
                parameters.put(subreport.getName(),
                        subreport.getSubmittedReport());

                String newPrefix = (prefix != null && prefix.length() > 0
                        ? prefix + SUBREPORT_PARAMETER_SEPARATOR : "")
                        + subreport.getName();
                processParameterMap(context, subreport, parameters, newPrefix);
            } else if (kid instanceof UIParameter) {
                final UIParameter param = (UIParameter) kid;
                String paramName;
                if (prefix != null && prefix.length() > 0) {
                    paramName = prefix + SUBREPORT_PARAMETER_SEPARATOR
                            + param.getName();
                } else {
                    paramName = param.getName();
                }

                parameters.put(paramName, param.getValue());
            }
        }
    }
}
