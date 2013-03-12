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

import net.sf.jasperreports.engine.JRDataSource;

/**
 * <tt>Source</tt> implementation of a <tt>JRDataSource</tt>.
 *
 * @author A. Alonso Dominguez
 */
public class JRDataSourceWrapper implements Source {

    /** The wrapped data source. */
    private JRDataSource dataSource;

    /**
     * Instantiates a new <tt>JRDataSourceWrapper</tt>.
     *
     * @param dataSource the data soruce to wrap.
     */
    public JRDataSourceWrapper(final JRDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("'dataSource' can't be null");
        }
        this.dataSource = dataSource;
    }

    /**
     * Obtains the wrapped data source.
     *
     * @return the wrapped data source.
     */
    public final JRDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Disposes this data source resources.
     */
    public void dispose() throws Exception {
        dataSource = null;
    }

}
