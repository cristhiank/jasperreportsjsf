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

import net.sf.jasperreports.jsf.component.UISource;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class DataSourceTag.
 */
public class SourceTag extends UIComponentELTag {

    private ValueExpression converter;

    private ValueExpression data;
    /** The query. */
    private ValueExpression query;
    /** The type. */
    private ValueExpression type;
    /** The value. */
    private ValueExpression value;

    private ValueExpression validator;

    public void setConverter(ValueExpression converter) {
        this.converter = converter;
    }

    public void setData(ValueExpression data) {
        this.data = data;
    }

    /**
     * Sets the query.
     *
     * @param query el query a establecer
     */
    public void setQuery(final ValueExpression query) {
        this.query = query;
    }

    /**
     * Sets the type.
     *
     * @param type el type a establecer
     */
    public void setType(final ValueExpression type) {
        this.type = type;
    }

    /**
     * Sets the value.
     *
     * @param value el value a establecer
     */
    public void setValue(final ValueExpression value) {
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
        converter = null;
        data = null;
        query = null;
        type = null;
        value = null;
        validator = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return UISource.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return null;
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

        setStringAttribute(component, "data", data);
        setStringAttribute(component, "query", query);
        setStringAttribute(component, "type", type);

        UISource source = (UISource) component;
        if (converter != null) {
            source.setValueExpression("converter", converter);
        }
        if (value != null) {
        	source.setValueExpression("value", value);
        }
        if (validator != null) {
            source.setValueExpression("validator", validator);
        }
    }
}
