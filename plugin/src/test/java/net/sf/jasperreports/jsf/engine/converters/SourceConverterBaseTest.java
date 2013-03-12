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

import net.sf.jasperreports.jsf.engine.converters.SourceConverterBase;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.convert.ConverterException;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.ConnectionWrapper;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.test.JMockTheories;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMockTheories.class)
public class SourceConverterBaseTest {

    @DataPoint
    public static final Object NULL_VALUE = null;

    @DataPoint
    public static final Object ANY_VALUE = new Object();

    @DataPoint
    public static final Source EMPTY_SOURCE = 
    	new JRDataSourceWrapper(new JREmptyDataSource());

    @DataPoint
    public static final Source NULL_SOURCE = null;

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;
    private UIReport component;

    private Source expectedSource;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        component = new DummyUIReport();
        expectedSource = mockery.mock(Source.class);
    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
    }
    
    @Theory
    @SuppressWarnings("unused")
    public void nullFacesContextThrowsIllegalArgEx() {
        SourceConverter converter = new DummyDefaultSourceConverter();

        Source source;
        try {
            source = converter.convertFromValue(null, component, null);
            fail("An IllegalArgumentException was expected");
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void nullComponentThrowsIllegalArgEx() {
        SourceConverter converter = new DummyDefaultSourceConverter();

        Source source;
        try {
            source = converter.convertFromValue(
                    facesEnv.getFacesContext(), null, null);
            fail("An IllegalArgumentException was expected");
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void nullValueReturnsNull(final Object value) {
        assumeThat(value, nullValue());

        SourceConverter converter = new DummyDefaultSourceConverter();
        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, value);

        assertThat(source, nullValue());
    }

    @Theory
    public void withSourceValueReturnSameInstance() {
        SourceConverter converter = new DummyDefaultSourceConverter();

        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, expectedSource);
        assertThat(source, notNullValue());
        assertThat(source, sameInstance(expectedSource));
    }

    @Theory
    public void withConnectionValueReturnConnectionWrapper() {
        Connection expectedConn = mockery.mock(Connection.class);

        SourceConverter converter = new DummyDefaultSourceConverter();
        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, expectedConn);

        assertThat(source, notNullValue());
        assertThat(source, is(ConnectionWrapper.class));

        Connection conn = ((ConnectionWrapper) source).getConnection();
        assertThat(conn, notNullValue());
        assertThat(conn, sameInstance(expectedConn));
    }

    @Theory
    public void withDataSourceValueReturnConnectionWrapper() throws Exception {
        final Connection expectedConn = mockery.mock(Connection.class);
        final DataSource expectedDS = mockery.mock(DataSource.class);

        mockery.checking(new Expectations() {{
            oneOf(expectedDS).getConnection();
            will(returnValue(expectedConn));
        }});

        SourceConverter converter = new DummyDefaultSourceConverter();
        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, expectedDS);

        assertThat(source, notNullValue());
        assertThat(source, is(ConnectionWrapper.class));

        Connection conn = ((ConnectionWrapper) source).getConnection();
        assertThat(conn, notNullValue());
        assertThat(conn, sameInstance(expectedConn));
    }

    @Theory
    @SuppressWarnings("unused")
    public void withDataSourceValueAndSQLExThrowConverterEx() throws Exception {
        final DataSource expectedDS = mockery.mock(DataSource.class);
        final SQLException expectedCause = new SQLException();

        SourceConverter converter = new DummyDefaultSourceConverter();

        mockery.checking(new Expectations() {{
            oneOf(expectedDS).getConnection();
            will(throwException(expectedCause));
        }});

		Source source;
        try {
            source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, expectedDS);
            fail("A ConverterException was expected");
        } catch (Exception e) {
            assertThat(e, is(ConverterException.class));
            assertThat(e.getCause(), notNullValue());
            assertThat(e.getCause(), is(SQLException.class));

            SQLException ex = (SQLException) e.getCause();
            assertThat(ex, sameInstance(expectedCause));
        }
    }

    @Theory
    public void withJRDataSourceValueReturnJRDataSourceWrapper() {
        JRDataSource expectedDS = mockery.mock(JRDataSource.class);

        SourceConverter converter = new DummyDefaultSourceConverter();
        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, expectedDS);

        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource dataSource =
                ((JRDataSourceWrapper) source).getDataSource();
        assertThat(dataSource, notNullValue());
        assertThat(dataSource, sameInstance(expectedDS));
    }

	@Theory
	@SuppressWarnings("unchecked")
    public void withAnyValueReturnSource(final Object value) {
        assumeThat(value, allOf(notNullValue(),
                not(sameInstance(EMPTY_SOURCE))));

        DummyDefaultSourceConverter converter =
                new DummyDefaultSourceConverter();
        converter.setExpectedSource(expectedSource);

        Source source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, value);
        assertThat(source, notNullValue());
        assertThat(source, sameInstance(expectedSource));
    }

	@Theory
	@SuppressWarnings("unused")
    public void ifAnyValueThrowsExRethrowConverterEx(final Object value) {
        assumeThat(value, notNullValue());

        SourceException expected = new SourceException("");
        DummyDefaultSourceConverter converter =
                new DummyDefaultSourceConverter();
        converter.setExpectedException(expected);

        Source source;
        try {
            source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, value);
        } catch (Exception e) {
            assertThat(e, is(ConverterException.class));
            assertThat(e.getCause(), notNullValue());
            assertThat(e.getCause(), is(SourceException.class));

            SourceException ex = (SourceException) e.getCause();
            assertThat(ex, sameInstance(expected));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void ifAnyValueReturnsNullThrowConverterEx(final Object value) {
        assumeThat(value, notNullValue());

        DummyDefaultSourceConverter converter =
                new DummyDefaultSourceConverter();

        Source source;
        try {
            source = converter.convertFromValue(
                facesEnv.getFacesContext(), component, value);
        } catch (Exception e) {
            assertThat(e, is(ConverterException.class));
        }
    }

    @SuppressWarnings("serial")
	private static class DummyDefaultSourceConverter extends SourceConverterBase {

        private Source expectedSource;
        private SourceException expectedException;

        @Override
        protected Source createSource(FacesContext context,
                UIComponent component, Object value) throws SourceException {
            if (expectedException != null) {
                throw expectedException;
            }
            return expectedSource;
        }

        public void setExpectedSource(Source source) {
            this.expectedSource = source;
        }

        public void setExpectedException(SourceException exception) {
            this.expectedException = exception;
        }

    }

}
