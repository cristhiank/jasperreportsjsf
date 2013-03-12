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
package net.sf.jasperreports.jsf.component.html;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.renderkit.html.OutputLinkRenderer;

/**
 * Report link faces' component.
 *
 * @author A. Alonso Dominguez
 */
public class HtmlReportLink extends UIOutputReport {

    /** The report component type. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".HtmlReportLink";

    // HTML attributes

    /** The target HTML frame. */
    private String target;
    /** The CSS style attributes. */
    private String style;
    /** The CSS style class. */
    private String styleClass;

    /**
     * Instantiates a new html report link.
     */
    public HtmlReportLink() {
        super();
        setRendererType(OutputLinkRenderer.RENDERER_TYPE);
    }

    /**
     * Obtains the target HTML frame.
     *
     * @return the target HTML frame.
     */
    public final String getTarget() {
        if (target != null) {
            return target;
        }
        ValueExpression ve = getValueExpression("target");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return target;
        }
    }

    /**
     * Establishes a new target HTML frame.
     *
     * @param target the new target HTML frame.
     */
    public final void setTarget(final String target) {
        this.target = target;
    }

    /**
     * Obtains the CSS style attributes.
     *
     * @return the CSS style attributes.
     */
    public final String getStyle() {
        if (style != null) {
            return style;
        }
        ValueExpression ve = getValueExpression("style");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return style;
        }
    }

    /**
     * Establishes the new CSS style attributes.
     *
     * @param style the new CSS style.
     */
    public final void setStyle(final String style) {
        this.style = style;
    }

    /**
     * Obtains the CSS style class.
     *
     * @return the CSS style class.
     */
    public String getStyleClass() {
        if (styleClass != null) {
            return styleClass;
        }
        ValueExpression ve = getValueExpression("styleClass");
        if (ve != null) {
            try {
                return (String) ve.getValue(getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return styleClass;
        }
    }

    /**
     * Establishes a new CSS style class.
     *
     * @param styleClass the CSS style class.
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.html.HtmlPanelGroup#restoreState(
     * javax.faces.context.FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        target = (String) values[1];
        style = (String) values[2];
        styleClass = (String) values[3];
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.html.HtmlPanelGroup#saveState(javax.faces.context
     * .FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        Object[] values = new Object[4];
        values[0] = super.saveState(context);
        values[1] = target;
        values[2] = style;
        values[3] = styleClass;
        return values;
    }

}
