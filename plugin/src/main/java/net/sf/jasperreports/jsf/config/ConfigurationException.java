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
package net.sf.jasperreports.jsf.config;

import net.sf.jasperreports.jsf.JRFacesException;

/**
 * Exception wrapper which communicates any error happened
 * when parsing the application configuration files.
 *
 * @author A. Alonso Dominguez
 */
public class ConfigurationException extends JRFacesException {

    /**
     *
     */
    private static final long serialVersionUID = -6436187355445724783L;

    /**
     * Default constructor.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Consructor with a message and a cause.
     *
     * @param msg the message.
     * @param t the cause.
     */
    public ConfigurationException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Constructor with a message.
     *
     * @param msg the message.
     */
    public ConfigurationException(final String msg) {
        super(msg);
    }

    /**
     * Constructor with a cause.
     *
     * @param t the cause.
     */
    public ConfigurationException(final Throwable t) {
        super(t);
    }
}
