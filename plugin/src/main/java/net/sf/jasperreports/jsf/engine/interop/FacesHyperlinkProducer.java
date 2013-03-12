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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.jsf.Constants;

/**
 * The Class FacesHyperlinkProducer.
 */
public class FacesHyperlinkProducer implements JRHyperlinkProducer {
	
	private static final Logger logger = Logger.getLogger(
			FacesHyperlinkProducer.class.getPackage().getName(), 
			Constants.LOG_MESSAGES_BUNDLE);
	
    /** The report. */
    private final UIComponent report;
    
    /**
     * Instantiates a new faces hyperlink producer.
     *
     * @param context the context
     * @param report the report
     */
    protected FacesHyperlinkProducer(final UIComponent report) {
        if (report == null) {
            throw new IllegalArgumentException();
        }
        this.report = report;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.engine.export.JRHyperlinkProducer#getHyperlink(net
     * .sf.jasperreports.engine.JRPrintHyperlink)
     */
    public String getHyperlink(final JRPrintHyperlink link) {
        final StringBuilder buff = new StringBuilder();
        buff.append(link.getHyperlinkReference());

        String anchor;
        if ((anchor = link.getHyperlinkAnchor()) != null) {
        	buff.append("#");
        	buff.append(anchor);
        }
        
        List<?> params = null;
        if (link.getHyperlinkParameters() != null) {
            params = link.getHyperlinkParameters().getParameters();
        }
        if (params != null && params.size() > 0) {
            buff.append('?');
            for (int i = 0; i < params.size(); i++) {
                final JRHyperlinkParameter param =
                        (JRHyperlinkParameter) params.get(i);
                if (i > 0) {
                    buff.append("&");
                }
                buff.append(param.getName());
                buff.append('=');
                buff.append(param.getValueExpression().getText());
            }
        }

        String href = getFacesContext().getExternalContext()
                .encodeResourceURL(buff.toString());
        if (logger.isLoggable(Level.FINE)) {
        	logger.log(Level.FINE, "JRJSF_0040", new Object[]{ 
        			report.getClientId(getFacesContext()),
        			link.getHyperlinkReference(),
        			href
        	});
        }
        return href;
    }

    private FacesContext getFacesContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new IllegalStateException("No current FacesContext!");
        }
        return context;
    }

}
