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
import net.sf.jasperreports.jsf.engine.export.ExporterBase;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 *
 * @author antonio.alonso
 */
public abstract class OutputReportTag extends ReportTag {

    /** The format. */
    private ValueExpression format;

    private ValueExpression encoding;

    private ValueExpression pageIndex;

    private ValueExpression startPageIndex;

    private ValueExpression endPageIndex;

    private ValueExpression ignorePageMargins;

    private ValueExpression offsetX;

    private ValueExpression offsetY;

    /**
     * Sets the format.
     *
     * @param type the new format
     */
    public void setFormat(final ValueExpression format) {
        this.format = format;
    }

    public void setEncoding(ValueExpression encoding) {
        this.encoding = encoding;
    }

    public void setEndPageIndex(ValueExpression endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public void setIgnorePageMargins(ValueExpression ignorePageMargins) {
        this.ignorePageMargins = ignorePageMargins;
    }

    public void setOffsetX(ValueExpression offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(ValueExpression offsetY) {
        this.offsetY = offsetY;
    }

    public void setPageIndex(ValueExpression pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setStartPageIndex(ValueExpression startPageIndex) {
        this.startPageIndex = startPageIndex;
    }

    // TagSupport

    @Override
    public void release() {
        super.release();
        format = null;
        encoding = null;
        pageIndex = null;
        startPageIndex = null;
        endPageIndex = null;
        ignorePageMargins = null;
        offsetX = null;
        offsetY = null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringAttribute(component, "format", format);
        setStringAttribute(component,
                ExporterBase.ATTR_CHARACTER_ENCODING, encoding);
        setIntegerAttribute(component,
                ExporterBase.ATTR_PAGE_INDEX, pageIndex);
        setIntegerAttribute(component,
                ExporterBase.ATTR_START_PAGE_INDEX, startPageIndex);
        setIntegerAttribute(component,
                ExporterBase.ATTR_END_PAGE_INDEX, endPageIndex);
        setBooleanAttribute(component,
                ExporterBase.ATTR_IGNORE_PAGE_MARGINS, ignorePageMargins);
        setIntegerAttribute(component, ExporterBase.ATTR_OFFSET_X, offsetX);
        setIntegerAttribute(component, ExporterBase.ATTR_OFFSET_Y, offsetY);
    }

}
