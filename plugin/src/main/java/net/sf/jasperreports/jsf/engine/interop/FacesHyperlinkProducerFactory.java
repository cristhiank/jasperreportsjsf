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
package net.sf.jasperreports.jsf.engine.interop;

import javax.faces.component.UIComponent;

import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.jsf.Constants;

/**
 * A factory for creating FacesHyperlinkProducer objects.
 */
public class FacesHyperlinkProducerFactory extends JRHyperlinkProducerFactory {

    /** The report. */
    private final UIComponent report;
    
    /**
     * Instantiates a new faces hyperlink producer factory.
     *
     * @param context the context
     * @param report the report
     */
    public FacesHyperlinkProducerFactory(final UIComponent report) {
        if (report == null) {
            throw new IllegalArgumentException(
                    "'context' or 'report' can't be null");
        }
        
        this.report = report;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory#getHandler
     * (java.lang.String)
     */
    @Override
    public final JRHyperlinkProducer getHandler(final String linkType) {
        JRHyperlinkProducer producer = null;
        if (Constants.FACES_HYPERLINK_TYPE.equals(linkType)) {
            producer = new FacesHyperlinkProducer(report);
        }
        return producer;
    }
}
