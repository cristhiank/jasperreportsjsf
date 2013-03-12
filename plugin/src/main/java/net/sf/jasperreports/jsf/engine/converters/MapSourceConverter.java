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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.engine.Source;

/**
 * Converter implementation which transforms the received
 * value into a map or an array of maps.
 *
 * @author A. Alonso Dominguez
 */
public final class MapSourceConverter extends SourceConverterBase {

    /**
     * 
     */
    private static final long serialVersionUID = 343587676980464141L;
    
    private static final Logger logger = Logger.getLogger(
            MapSourceConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    @Override
    protected Source createSource(FacesContext context,
            UIComponent component, Object value) {
        JRDataSource dataSource;

        if (value == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "JRJSF_0020",
                           component.getClientId(context));
            }
            dataSource = new JREmptyDataSource();
        } else if (value instanceof Collection<?>) {
            dataSource = new JRMapCollectionDataSource((Collection<Map<String, ?>>) (Collection<?>) value);
        } else {
            Map<?, ?>[] mapArray;
            if (!value.getClass().isArray()) {
                mapArray = new Map[]{(Map<?, ?>) value};
            } else {
                mapArray = (Map[]) value;
            }
            dataSource = new JRMapArrayDataSource(mapArray);
        }
        return new JRDataSourceWrapper(dataSource);
    }
}
