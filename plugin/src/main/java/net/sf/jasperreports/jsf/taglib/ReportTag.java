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
package net.sf.jasperreports.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import net.sf.jasperreports.jsf.component.UIReport;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class AbstractReportTag.
 */
public abstract class ReportTag extends UIComponentELTag {

    private ValueExpression sourceConverter;
    private ValueExpression reportConverter;
    /** The data source. */
    private ValueExpression source;
    private ValueExpression name;
    private ValueExpression value;
    private ValueExpression validator;

    public void setSourceConverter(ValueExpression converter) {
        this.sourceConverter = converter;
    }

    public void setReportConveter(ValueExpression converter) {
        this.reportConverter = converter;
    }

    /**
     * Sets the source object.
     *
     * @param source the new source object
     */
    public void setSource(final ValueExpression source) {
        this.source = source;
    }

    public void setName(final ValueExpression name) {
        this.name = name;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    public void setValidator(ValueExpression validator) {
        this.validator = validator;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentELTag#release()
     */
    @Override
    public void release() {
        super.release();
        sourceConverter = null;
        reportConverter = null;
        source = null;
        name = null;
        value = null;
        validator = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.webapp.UIComponentELTag#setProperties(javax.faces.component
     * .UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "name", name);
        setStringAttribute(component, "source", source);
        setStringAttribute(component, "value", value);

        UIReport report = (UIReport) component;
        if (sourceConverter != null) {
            report.setValueExpression("sourceConverter", sourceConverter);
        }
        if (reportConverter != null) {
            report.setValueExpression("reportConverter", reportConverter);
        }
        if (validator != null) {
            report.setValueExpression("validator", validator);
        }
    }
}
