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
package net.sf.jasperreports.jsf.resource;

import net.sf.jasperreports.jsf.JRFacesException;

public class ResourceException extends JRFacesException {

    /**
     *
     */
    private static final long serialVersionUID = 3926030327829973068L;

    public ResourceException(final String msg, final Throwable t) {
        super(msg, t);
    }

    public ResourceException(final String msg) {
        super(msg);
    }

    public ResourceException(final Throwable t) {
        super(t);
    }
}
