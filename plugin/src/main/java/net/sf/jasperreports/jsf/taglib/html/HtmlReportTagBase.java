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
import net.sf.jasperreports.jsf.taglib.OutputReportTag;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 *
 * @author aalonsodominguez
 */
abstract class HtmlReportTagBase extends OutputReportTag {
	
    private ValueExpression dir;

    private ValueExpression lang;
    
    private ValueExpression tabindex;
    
    private ValueExpression title;
    
    private ValueExpression style;
    
    private ValueExpression styleClass;

    public void setDir(ValueExpression dir) {
        this.dir = dir;
    }

    public void setLang(ValueExpression lang) {
        this.lang = lang;
    }

	public void setTabindex(ValueExpression tabindex) {
		this.tabindex = tabindex;
	}

	public void setTitle(ValueExpression title) {
		this.title = title;
	}

	public void setStyle(ValueExpression style) {
		this.style = style;
	}

	public void setStyleClass(ValueExpression styleClass) {
		this.styleClass = styleClass;
	}

	@Override
    public void release() {
        super.release();
        dir = null;
        lang = null;
        tabindex = null;
        title = null;
        style = null;
        styleClass = null;
    }

    @Override
    protected void setProperties(final UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "dir", dir);
        setStringAttribute(component, "lang", lang);
        setStringAttribute(component, "tabindex", tabindex);
        setStringAttribute(component, "title", title);
        setStringAttribute(component, "style", style);
        setStringAttribute(component, "styleClass", styleClass);
    }

}
