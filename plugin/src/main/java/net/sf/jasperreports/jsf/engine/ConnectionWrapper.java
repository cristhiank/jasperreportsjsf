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

import java.sql.Connection;

/**
 * <tt>Source</tt> implementation of a connection wrapper.
 *
 * @author A. Alonso Dominguez
 */
public class ConnectionWrapper implements Source {

    /** The wrapped connection. */
    private Connection connection;

    /**
     * Constructor with connection instance.
     *
     * @param connection the connection to wrap.
     */
    public ConnectionWrapper(final Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("'connection' can't be null");
        }
        this.connection = connection;
    }

    /**
     * Obtains the wrapped connection.
     *
     * @return the wrapped connection.
     */
    public final Connection getConnection() {
        return connection;
    }

    /**
     * Disposes this source instance.
     *
     * @throws Exception if some error happens closing the connection.
     */
    public void dispose() throws Exception {
        connection.close();
        connection = null;
    }

}
