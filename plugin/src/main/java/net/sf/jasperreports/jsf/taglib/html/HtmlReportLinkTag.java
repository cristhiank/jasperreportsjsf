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

import net.sf.jasperreports.jsf.component.html.HtmlReportLink;
import net.sf.jasperreports.jsf.renderkit.html.OutputLinkRenderer;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * The Class ReportLinkTag.
 */
public class HtmlReportLinkTag extends HtmlReportTagBase {

	private ValueExpression accesskey;
	
	private ValueExpression ondblclick;
    
    private ValueExpression onmousedown;
    
    private ValueExpression onmousemove;
    
    private ValueExpression onmouseup;
    
    private ValueExpression onmouseout;
    
    private ValueExpression onmouseover;
	
    /** The target. */
    private ValueExpression target;

    public void setAccesskey(ValueExpression accesskey) {
		this.accesskey = accesskey;
	}

	public void setOndblclick(ValueExpression ondblclick) {
		this.ondblclick = ondblclick;
	}

	public void setOnmousedown(ValueExpression onmousedown) {
		this.onmousedown = onmousedown;
	}

	public void setOnmousemove(ValueExpression onmousemove) {
		this.onmousemove = onmousemove;
	}

	public void setOnmouseup(ValueExpression onmouseup) {
		this.onmouseup = onmouseup;
	}

	public void setOnmouseout(ValueExpression onmouseout) {
		this.onmouseout = onmouseout;
	}

	public void setOnmouseover(ValueExpression onmouseover) {
		this.onmouseover = onmouseover;
	}

	/**
     * Sets the target.
     *
     * @param target
     *            the new target
     */
    public void setTarget(final ValueExpression target) {
        this.target = target;
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
        accesskey = null;
        ondblclick = null;
        onmousedown = null;
        onmousemove = null;
        onmouseout = null;
        onmouseover = null;
        onmouseup = null;
        target = null;
    }

    // UIComponentELTag

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getComponentType()
     */
    @Override
    public String getComponentType() {
        return HtmlReportLink.COMPONENT_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.webapp.UIComponentTagBase#getRendererType()
     */
    @Override
    public String getRendererType() {
        return OutputLinkRenderer.RENDERER_TYPE;
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

        setStringAttribute(component, "accesskey", accesskey);
        setStringAttribute(component, "ondblclick", ondblclick);
        setStringAttribute(component, "onmousedown", onmousedown);
        setStringAttribute(component, "onmousemove", onmousemove);
        setStringAttribute(component, "onmouseout", onmouseout);
        setStringAttribute(component, "onmouseover", onmouseover);
        setStringAttribute(component, "onmouseup", onmouseup);
        setStringAttribute(component, "target", target);
    }
}
