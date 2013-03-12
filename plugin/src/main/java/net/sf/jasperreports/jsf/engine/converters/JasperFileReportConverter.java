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
package net.sf.jasperreports.jsf.engine.converters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.ContextClassLoaderObjectInputStream;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.convert.ConverterException;
import net.sf.jasperreports.jsf.resource.Resource;

/**
 *
 * @author aalonsodominguez
 */
public class JasperFileReportConverter extends ReportConverterBase {

    private static final Logger logger = Logger.getLogger(
            JasperFileReportConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    protected JasperReport loadFromResource(FacesContext context,
            UIReport component, Resource resource) throws ConverterException {
        JasperReport aReport = null;
        ObjectInputStream ois = null;
        try {
            ois = new ContextClassLoaderObjectInputStream(
                    resource.getInputStream());
            if (logger.isLoggable(Level.FINE)) {
            	logger.log(Level.FINE, "JRJSF_0042", new Object[]{
            			resource.getName(), component.getClientId(context)
            	});
            }
            aReport = (JasperReport) ois.readObject();
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                LogRecord record = new LogRecord(
                        Level.SEVERE, "JRJSF_0033");
                record.setParameters(new Object[]{
                            resource.getName()});
                record.setThrown(e);
                logger.log(record);
            }
            throw new ConverterException(e);
        } catch (ClassNotFoundException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                LogRecord record = new LogRecord(
                        Level.SEVERE, "JRJSF_0034");
                record.setParameters(new Object[]{
                            resource.getName()});
                record.setThrown(e);
                logger.log(record);
            }
            throw new ConverterException(e);
        }

        return aReport;
    }

}
