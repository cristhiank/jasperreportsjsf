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
package net.sf.jasperreports.jsf.engine;

import net.sf.jasperreports.jsf.JRFacesException;

/**
 * Root class for the exporter exceptions.
 *
 * @author A. Alonso Dominguez
 */
public class ExporterException extends JRFacesException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3447264929238791580L;

    /**
     * Instantiates a new exporter exception.
     *
     * @param msg the message.
     * @param t the cause.
     */
    public ExporterException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new exporter exception.
     *
     * @param msg the message.
     */
    public ExporterException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new exporter exception.
     *
     * @param t the cause.
     */
    public ExporterException(final Throwable t) {
        super(t);
    }
}
