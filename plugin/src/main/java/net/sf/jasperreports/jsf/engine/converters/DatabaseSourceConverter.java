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
package net.sf.jasperreports.jsf.engine.converters;

import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.jsf.engine.ConnectionWrapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Base converter class for source converters which can obtain
 * a JDBC connection from the value to be converted.
 *
 * @author A. Alonso Dominguez
 */
public abstract class DatabaseSourceConverter
        extends SourceConverterBase {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5189066786528032421L;
	private static final Logger logger = Logger.getLogger(
            DatabaseSourceConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    @Override
    protected Source createSource(FacesContext context,
            UIComponent component, Object value)
    throws SourceException {
        Connection connection = getConnection(context, component);
        if (connection == null) {
            if (logger.isLoggable(Level.WARNING)) {
                String clientId = component.getClientId(context);
                logger.log(Level.WARNING, "JRJSF_0020", clientId);
            }
            JRDataSource ds = new JREmptyDataSource();
            return new JRDataSourceWrapper(ds);
        } else {
            Source reportSource;
            final String query = getStringAttribute(component, "query", null);
            if (query != null && query.length() > 0) {
                ResultSet rs = executeQuery(context, component, connection, query);
                JRDataSource dataSource = new JRResultSetDataSource(rs);
                reportSource = new JRDataSourceWrapper(dataSource);
            } else {
                reportSource = new ConnectionWrapper(connection);
            }
            return reportSource;
        }
    }

    protected abstract Connection getConnection(
            FacesContext context, UIComponent component)
    throws SourceException;

    /**
     * Execute query.
     *
     * @param conn the conn
     *
     * @return the result set
     *
     * @throws ReportSourceException if some error happens when executing
     *         the sql statement
     */
    protected ResultSet executeQuery(final FacesContext context,
            final UIComponent component, final Connection conn,
            final String query)
            throws SourceException {
        PreparedStatement st;
        try {
            st = conn.prepareStatement(query);
        } catch (SQLException ex) {
            throw new UnpreparedStatementException(query, ex);
        }

        int paramIdx = 1;
        for (final UIComponent kid : component.getChildren()) {
            if (!(kid instanceof UIParameter)) {
                continue;
            }

            final UIParameter param = (UIParameter) kid;
            try {
                st.setObject(paramIdx++, param.getValue());
            } catch (Exception e) {
                if (logger.isLoggable(Level.WARNING)) {
                    LogRecord record = new LogRecord(
                            Level.WARNING, "JRJSF_0028");
                    record.setParameters(new Object[]{
                    		paramIdx, param.getName(), query
                    });
                    record.setThrown(e);
                    record.setResourceBundleName(logger.getResourceBundleName());
                    record.setResourceBundle(logger.getResourceBundle());
                    logger.log(record);
                }
            }
        }


        try {
            return st.executeQuery();
        } catch (SQLException e) {
            throw new QueryExecutionException(query, e);
        } finally {
            try {
                st.close();
            } catch (final SQLException e) { ; }
        }

    }

}
