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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.engine.SourceException;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Converter implementation which obtains a JDBC connection
 * from a JNDI datasource name.
 *
 * @author A. Alonso Dominguez
 */
public final class JndiSourceConverter extends DatabaseSourceConverter {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7288394077963583107L;
	/** The Constant logger. */
    private static final Logger logger = Logger.getLogger(
            JndiSourceConverter.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);
    
    @Override
    protected Connection getConnection(FacesContext context,
            UIComponent component)
    throws SourceException {
        final String jndiName = getStringAttribute(component, "value", null);
        if (jndiName == null || jndiName.length() == 0) {
            return null;
        }
        logger.log(Level.FINE, "JRJSF_0005", jndiName);

        try {
            final Context jndi = new InitialContext();
            final DataSource ds = (DataSource) jndi.lookup(jndiName);
            if (ds == null) {
                throw new NullDataSourceException(jndiName);
            }
            return ds.getConnection();
        } catch(NamingException e) {
            throw new SourceException(e);
        } catch(SQLException e) {
            throw new SourceException(e);
        }
    }

}
