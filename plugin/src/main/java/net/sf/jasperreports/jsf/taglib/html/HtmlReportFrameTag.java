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
package net.sf.jasperreports.jsf.taglib.html;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import net.sf.jasperreports.jsf.component.html.HtmlReportFrame;
import net.sf.jasperreports.jsf.renderkit.html.FrameRenderer;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class ReportTag.
 */
public class HtmlReportFrameTag extends HtmlReportTagBase {

    private ValueExpression layout;
    /** The frameborder. */
    private ValueExpression frameborder;
    /** The marginheight. */
    private ValueExpression marginheight;
    /** The marginwidth. */
    private ValueExpression marginwidth;
    /** The height. */
    private ValueExpression height;
    /** The width. */
    private ValueExpression width;

    public void setLayout(ValueExpression layout) {
        this.layout = layout;
    }

    /**
     * Sets the frameborder.
     *
     * @param frameborder el frameborder a establecer
     */
    public void setFrameborder(final ValueExpression frameborder) {
        this.frameborder = frameborder;
    }

    /**
     * Sets the height.
     *
     * @param height el height a establecer
     */
    public void setHeight(final ValueExpression height) {
        this.height = height;
    }

    /**
     * Sets the marginheight.
     *
     * @param marginheight el marginheight a establecer
     */
    public void setMarginheight(final ValueExpression marginheight) {
        this.marginheight = marginheight;
    }

    /**
     * Sets the marginwidth.
     *
     * @param marginwidth el marginwidth a establecer
     */
    public void setMarginwidth(final ValueExpression marginwidth) {
        this.marginwidth = marginwidth;
    }

    /**
     * Sets the width.
     *
     * @param width el width a establecer
     */
    public void setWidth(final ValueExpression width) {
        this.width = width;
    }

    // TagSupport

    /*
     * (non-Javadoc)
     *
     * @see net.sf.jasperreports.jsf.taglib.AbstractReportTag#release()
     */
    @Override
    public void release() {
        super.release();
        layout = null;
        frameborder = null;
        marginheight = null;
        marginwidth = null;
        height = null;
        width = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return HtmlReportFrame.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return FrameRenderer.RENDERER_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.taglib.AbstractReportTag#setProperties(javax
     * .faces.component.UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);

        setIntegerAttribute(component, "frameborder", frameborder);
        setStringAttribute(component, "height", height);
        setStringAttribute(component, "layout", layout);
        setStringAttribute(component, "marginheight", marginheight);
        setStringAttribute(component, "marginwidth", marginwidth);
        setStringAttribute(component, "width", width);
    }
}
