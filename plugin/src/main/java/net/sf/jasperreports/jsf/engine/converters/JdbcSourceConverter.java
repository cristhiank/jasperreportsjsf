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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.engine.SourceException;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Converter implementation which obtains a JDBC connection
 * from a JDBC url and some additional attributes added to the
 * requesting component.
 *
 * @author A. Alonso Dominguez
 */
public final class JdbcSourceConverter extends DatabaseSourceConverter {

    /**
     * 
     */
    private static final long serialVersionUID = 2746686961906403123L;
    
    public static final String ATTR_DRIVER_CLASS_NAME = "driverClassName";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_PASSWORD = "password";
    
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JdbcSourceConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    @Override
    protected Connection getConnection(
            FacesContext context, UIComponent component)
            throws SourceException {
        final String connectionURL = getStringAttribute(
                component, "value", null);
        if (connectionURL == null) {
            return null;
        } else {
            final String driverClass = (String) component.getAttributes().get(
                    ATTR_DRIVER_CLASS_NAME);
            if ((driverClass == null) || (driverClass.length() == 0)) {
                throw new InvalidDatabaseDriverException(
                        "jdbc report source type requires a driverClassName value!");
            }

            try {
                Class.forName(driverClass);
            } catch (final ClassNotFoundException e) {
                throw new InvalidDatabaseDriverException(
                        "Driver class not found: " + driverClass, e);
            }
            logger.log(Level.FINE, "JRJSF_0004", driverClass);

            final String username = (String) component.getAttributes().get(
                    ATTR_USERNAME);
            final String password = (String) component.getAttributes().get(
                    ATTR_PASSWORD);

            Connection conn = null;
            try {
                if (username == null) {
                    logger.log(Level.FINE, "JRJSF_0007", connectionURL);
                    conn = DriverManager.getConnection(connectionURL);
                } else {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, "JRJSF_0008", new Object[]{
                                    connectionURL, username});
                    }
                    conn = DriverManager.getConnection(
                            connectionURL, username,
                            password);
                }
            } catch (final SQLException e) {
                throw new SourceException(e);
            }
            return conn;
        }
    }
}
