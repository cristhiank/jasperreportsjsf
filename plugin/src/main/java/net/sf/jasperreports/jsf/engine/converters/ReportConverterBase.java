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

import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.ConverterException;
import net.sf.jasperreports.jsf.convert.ReportConverter;
import net.sf.jasperreports.jsf.resource.Resource;

/**
 *
 * @author aalonsodominguez
 */
public abstract class ReportConverterBase implements ReportConverter {

    public boolean accepts(Object value) {
        if (value == null) return true;
        return (true);
    }
    
    public final JasperReport convertFromValue(FacesContext context,
            UIReport component, Object value)
    throws ConverterException {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        if (component == null) {
            throw new IllegalArgumentException("component can't be null");
        }

        JasperReport aReport = null;
        Object aValue = component.getValue();
        if (aValue != null) {
            String valueStr;
            if (aValue instanceof String) {
                valueStr = (String) aValue;
            } else {
                valueStr = aValue.toString();
            }

            Resource resource = getJRFacesContext().createResource(
                    context, component, valueStr);

            aReport = loadFromResource(context, component, resource);
        }
        
        if (aReport == null) {
            throw new ConverterException(
                    "Couldn't convert report value: " + aValue);
        }
        
        return aReport;
    }

    protected abstract JasperReport loadFromResource(FacesContext context,
            UIReport component, Resource resource)
    throws ConverterException;

    protected final FacesContext getFacesContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException("No faces context");
        }
        return context;
    }

    protected final JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

}
