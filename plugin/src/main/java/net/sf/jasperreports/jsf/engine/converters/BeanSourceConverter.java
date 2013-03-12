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

import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;

/**
 * Converter implementation which transforms the data into
 * a bean or an array of beans.
 *
 * @author A. Alonso Dominguez
 */
public final class BeanSourceConverter extends SourceConverterBase {

    /**
     * 
     */
    private static final long serialVersionUID = -6307709860143591157L;
    
    private static final Logger logger = Logger.getLogger(
            BeanSourceConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    @Override
    public Source createSource(FacesContext context,
            UIComponent component, Object value)
            throws SourceException {
        JRDataSource dataSource;

        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                           component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof Collection<?>) {
            dataSource = new JRBeanCollectionDataSource(
                    (Collection<?>) value);
        } else {
            Object[] beanArray;
            if (!value.getClass().isArray()) {
                beanArray = new Object[]{value};
            } else {
                beanArray = (Object[]) value;
            }
            dataSource = new JRBeanArrayDataSource(beanArray);
        }
        return new JRDataSourceWrapper(dataSource);
    }
}
