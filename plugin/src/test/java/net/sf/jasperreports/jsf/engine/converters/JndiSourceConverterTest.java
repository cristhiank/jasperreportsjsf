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

import net.sf.jasperreports.jsf.engine.converters.JndiSourceConverter;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;

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

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Theories.class)
public class JndiSourceConverterTest {

    @DataPoint
    public static final String NULL_VALUE = null;

    @DataPoint
    public static final String EMPTY_VALUE = "";

    @DataPoint
    public static final String NONE_DATASOURCE = "nullDS";

    @DataPoint
    public static final String INVALID_DATASOURCE =
            "java:comp/env/jdbc/invalidDS";

    @DataPoint
    public static final String VALID_DATASOURCE = 
            "java:comp/env/jdbc/myDataSource";

    private MockFacesEnvironment facesEnv;

    private JndiSourceConverter converter;
    private UISource component;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        converter = new JndiSourceConverter();
        component = new UISource();
    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
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
    public void emptyValueReturnsNull(String value) {
        assumeThat(value, notNullValue());
        assumeTrue(value.length() == 0);

        component.setValue(value);

        Connection connection = converter.getConnection(
                facesEnv.getFacesContext(), component);
        assertThat(connection, nullValue());
    }

    @Theory
    @SuppressWarnings("unused")
    public void invalidJnidNameThrowsNamingEx(String jndiName) {
        assumeThat(jndiName, notNullValue());
        assumeTrue(jndiName.length() > 0);
        assumeTrue(!jndiName.startsWith("java:"));

        component.setValue(jndiName);

        Connection connection;
        try {
            connection = converter.getConnection(
                    facesEnv.getFacesContext(), component);
            fail("A NamingException was expected");
        } catch (Exception e) {
            assertThat(e, is(SourceException.class));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void invalidDSThrowsSQLEx(String jndiName) {
        assumeThat(jndiName, notNullValue());
        assumeTrue(jndiName.startsWith("java:"));
        assumeThat(jndiName, containsString("invalid"));

        component.setValue(jndiName);

        Connection connection;
        try {
            connection = converter.getConnection(
                    facesEnv.getFacesContext(), component);
            fail("A SQLException was expected");
        } catch (Exception e) {
            assumeThat(e, is(SourceException.class));
            assumeThat(e.getCause(), notNullValue());
            assumeThat(e.getCause(), is(SQLException.class));
        }
    }

    @Theory
    public void obtainValidConnection(String jndiName) {
        assumeThat(jndiName, notNullValue());
        assumeTrue(jndiName.startsWith("java:"));
        assumeThat(jndiName, not(containsString("invalid")));

        component.setValue(jndiName);
        Connection connection = converter.getConnection(
                facesEnv.getFacesContext(), component);
        assertThat(connection, notNullValue());
    }

}
