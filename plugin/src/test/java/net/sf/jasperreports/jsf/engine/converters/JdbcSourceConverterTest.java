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

import net.sf.jasperreports.jsf.engine.converters.JdbcSourceConverter;
import net.sf.jasperreports.jsf.engine.converters.InvalidDatabaseDriverException;
import java.sql.Connection;
import java.sql.SQLException;

import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class JdbcSourceConverterTest {

    @DataPoint
    public static final String NULL_VALUE = null;

    @DataPoint
    public static final String INVALID_DRIVER = "invalidDriver";

    @DataPoint
    public static final String VALID_DRIVER = "org.hsqldb.jdbcDriver";

    @DataPoint
    public static final String VALID_JDBC_URL =
            "jdbc:hsqldb:file:target/test-db/data";

    private MockFacesEnvironment facesEnv;
    private JdbcSourceConverter converter;
    private UISource component;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        converter = new JdbcSourceConverter();
        component = new UISource();
    }

    @After
    public void dispose() {
        facesEnv.release();
    }

    @Theory
    public void nullValueReturnsNull(String value) {
        assumeThat(value, nullValue());

        component.setValue(value);

        Connection connection = converter.getConnection(
                facesEnv.getFacesContext(), component);
        assertThat(connection, nullValue());
    }

	@Theory
	@SuppressWarnings("unused")
    public void nullOrEmptyDriverThrowsEx(String value, String driverClass) {
        assumeThat(value, notNullValue());
        assumeTrue(value.length() > 0);
        if (driverClass != null) {
            assumeTrue(driverClass.length() == 0);
        }

        component.setValue(value);
        if (driverClass != null) {
            component.getAttributes().put(
                    JdbcSourceConverter.ATTR_DRIVER_CLASS_NAME, driverClass);
       } 

        Connection connection;
        try {
            connection = converter.getConnection(
                    facesEnv.getFacesContext(), component);
            fail("An exception was expected");
        } catch (RuntimeException e) {
            assertThat(e, is(InvalidDatabaseDriverException.class));
            assertThat(e.getCause(), nullValue());
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void invalidDriverThrowsEx(String value, String driverClass) {
        assumeNotNull(value, driverClass);
        assumeTrue(value.length() > 0);
        try {
            Class.forName(driverClass);
            assumeTrue(false);
        } catch (ClassNotFoundException e) { }

        component.setValue(value);
        component.getAttributes().put(
                JdbcSourceConverter.ATTR_DRIVER_CLASS_NAME, driverClass);

        Connection connection;
        try {
            connection = converter.getConnection(
                    facesEnv.getFacesContext(), component);
            fail("An exception was expected");
        } catch (RuntimeException e) {
            assertThat(e, is(InvalidDatabaseDriverException.class));
            assertThat(e.getCause(), notNullValue());
            assertThat(e.getCause(), is(ClassNotFoundException.class));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void invalidUrlThrowsSQLEx(String value, String driverClass) {
        assumeNotNull(value, driverClass);
        assumeThat(value, not(startsWith("jdbc")));
        assumeThat(driverClass, startsWith("org.hsqldb"));

        component.setValue(value);
        component.getAttributes().put(
                JdbcSourceConverter.ATTR_DRIVER_CLASS_NAME, driverClass);

        Connection connection;
        try {
            connection = converter.getConnection(
                    facesEnv.getFacesContext(), component);
            fail("An exception was expected");
        } catch (RuntimeException e) {
            assertThat(e, is(SourceException.class));
            assertThat(e.getCause(), notNullValue());
            assertThat(e.getCause(), is(SQLException.class));
        }
    }

    @Theory
    public void validDataReturnsConnection(String value, String driverClass)
    throws Exception {
        assumeNotNull(value, driverClass);
        assumeThat(value, startsWith("jdbc"));
        assumeThat(driverClass, startsWith("org.hsqldb"));

        component.setValue(value);
        component.getAttributes().put(
                JdbcSourceConverter.ATTR_DRIVER_CLASS_NAME, driverClass);
        component.getAttributes().put(
                JdbcSourceConverter.ATTR_USERNAME, "sa");
        component.getAttributes().put(
                JdbcSourceConverter.ATTR_PASSWORD, "");

        Connection conn = converter.getConnection(
                facesEnv.getFacesContext(), component);
        assertThat(conn, notNullValue());
    }

}
