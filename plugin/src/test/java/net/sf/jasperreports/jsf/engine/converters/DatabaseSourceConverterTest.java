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

import net.sf.jasperreports.jsf.engine.converters.QueryExecutionException;
import net.sf.jasperreports.jsf.engine.converters.DatabaseSourceConverter;
import net.sf.jasperreports.jsf.engine.converters.UnpreparedStatementException;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.jsf.engine.ConnectionWrapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.engine.SourceException;
import net.sf.jasperreports.jsf.test.JMockTheories;
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
public class DatabaseSourceConverterTest {

    @DataPoint
    public static final String NULL_QUERY = null;

    @DataPoint
    public static final String EMPTY_QUERY = "";

    @DataPoint
    public static final String ANY_QUERY = "select * from Table where id = ?";

    @DataPoint
    public static final Object ANY_DATA = new Object();

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;
    private MockDatabaseSourceConverter converter;
    private UISource component;
    
    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        converter = new MockDatabaseSourceConverter();
        component = new UISource();
        
        UIComponent ignored = new HtmlOutputText();
        component.getChildren().add(ignored);
    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullConnectionReturnsEmptyDataSource() {
        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, null);
        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource ds = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JREmptyDataSource.class));
    }

    @Theory
    public void nullQueryReturnsConnectionWrapper(final String query,
            final Object data) {
        assumeThat(query, nullValue());

        final Connection expectedConnection = mockery.mock(Connection.class);
        converter.setConnection(expectedConnection);

        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, data);
        assertThat(source, notNullValue());
        assertThat(source, is(ConnectionWrapper.class));

        Connection connection = ((ConnectionWrapper) source).getConnection();
        assertThat(connection, notNullValue());
        assertThat(connection, sameInstance(expectedConnection));
    }

    @Theory
    public void emptyQueryReturnsConnectionWrapper(final String query,
            final Object data) {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() == 0);

        final Connection expectedConnection = mockery.mock(Connection.class);
        converter.setConnection(expectedConnection);
        component.setQuery(query);

        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, data);
        assertThat(source, notNullValue());
        assertThat(source, is(ConnectionWrapper.class));

        Connection connection = ((ConnectionWrapper) source).getConnection();
        assertThat(connection, notNullValue());
        assertThat(connection, sameInstance(expectedConnection));
    }

    @Theory
    public void queryWithoutParamsReturnsWrapper(
            final String query, final Object data)
    throws Exception {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() > 0);

        final Connection connection = mockery.mock(Connection.class);
        final PreparedStatement statement =
                mockery.mock(PreparedStatement.class);
        final ResultSet expectedRS = mockery.mock(ResultSet.class);

        converter.setConnection(connection);
        component.setQuery(query);

        mockery.checking(new Expectations() {{
            oneOf(connection).prepareStatement(query);
            will(returnValue(statement));
            oneOf(statement).executeQuery(); will(returnValue(expectedRS));
            oneOf(statement).close();
        }});

        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, data);
        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource ds = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JRResultSetDataSource.class));
    }

    @Theory
    public void queryWithParamsSetsStatemetAndReturnsWrapper(
            final String query, final Object data)
    throws Exception {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() > 0);

        final Connection connection = mockery.mock(Connection.class);
        final PreparedStatement statement = mockery.mock(PreparedStatement.class);
        final ResultSet expectedRS = mockery.mock(ResultSet.class);

        final Integer i200 = new Integer(200);
        UIParameter param = new UIParameter();
        param.setName("id");
        param.setValue(i200);

        converter.setConnection(connection);
        component.setQuery(query);
        component.getChildren().add(param);

        mockery.checking(new Expectations() {{
            oneOf(connection).prepareStatement(query);
            will(returnValue(statement));
            oneOf(statement).setObject(1, i200);
            oneOf(statement).executeQuery(); will(returnValue(expectedRS));
            oneOf(statement).close();
        }});

        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, data);
        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource ds = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JRResultSetDataSource.class));
    }

    @Theory
    public void ifSQLExWhenFillingParamsDontRethrow(
            final String query, final Object data)
    throws Exception {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() > 0);

        final Connection connection = mockery.mock(Connection.class);
        final PreparedStatement statement = mockery.mock(PreparedStatement.class);
        final ResultSet expectedRS = mockery.mock(ResultSet.class);
        final SQLException statementEx = new SQLException();

        final Integer i200 = new Integer(200);
        UIParameter param = new UIParameter();
        param.setName("id");
        param.setValue(i200);

        converter.setConnection(connection);
        component.setQuery(query);
        component.getChildren().add(param);

        mockery.checking(new Expectations() {{
            oneOf(connection).prepareStatement(query);
            will(returnValue(statement));
            oneOf(statement).setObject(1, i200);
            will(throwException(statementEx));
            oneOf(statement).executeQuery(); will(returnValue(expectedRS));
            oneOf(statement).close();
        }});

        Source source = converter.createSource(facesEnv.getFacesContext(),
                component, data);
        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource ds = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JRResultSetDataSource.class));
    }

    @Theory
    @SuppressWarnings("unused")
    public void whenSQLExPreparingStatmentRethrowWrapped(
            final String query, final Object data)
    throws Exception {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() > 0);

        final Connection connection = mockery.mock(Connection.class);
        final SQLException expectedCause = new SQLException();

        converter.setConnection(connection);
        component.setQuery(query);

        mockery.checking(new Expectations() {{
            oneOf(connection).prepareStatement(query);
            will(throwException(expectedCause));
        }});

        Source source;
        try {
            source = converter.createSource(
                    facesEnv.getFacesContext(), component, data);
            fail("An exception invoking 'createSource' was expected");
        } catch (RuntimeException e) {
            assertThat(e, is(UnpreparedStatementException.class));

            UnpreparedStatementException use = (UnpreparedStatementException) e;
            assertThat(use.getMessage(), equalTo(query));
            
            assertThat(use.getCause(), notNullValue());
            assertThat(use.getCause(), is(SQLException.class));

            SQLException cause = (SQLException) use.getCause();
            assertThat(cause, sameInstance(expectedCause));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void whenSQLExExecutingQueryRethrowWrapped(
            final String query, final Object data)
    throws Exception {
        assumeThat(query, notNullValue());
        assumeTrue(query.length() > 0);

        final Connection connection = mockery.mock(Connection.class);
        final PreparedStatement statement = mockery.mock(PreparedStatement.class);
        final SQLException expectedCause = new SQLException();

        converter.setConnection(connection);
        component.setQuery(query);

        mockery.checking(new Expectations() {{
            oneOf(connection).prepareStatement(query);
            will(returnValue(statement));
            oneOf(statement).executeQuery();
            will(throwException(expectedCause));
            oneOf(statement).close();
        }});

        Source source;
        try {
            source = converter.createSource(
                    facesEnv.getFacesContext(), component, data);
            fail("An exception invoking 'createSource' was expected");
        } catch (RuntimeException e) {
            assertThat(e, is(QueryExecutionException.class));

            QueryExecutionException qee = (QueryExecutionException) e;
            assertThat(qee.getMessage(), equalTo(query));

            assertThat(qee.getCause(), notNullValue());
            assertThat(qee.getCause(), is(SQLException.class));

            SQLException cause = (SQLException) qee.getCause();
            assertThat(cause, sameInstance(expectedCause));
        }
    }

    @SuppressWarnings("serial")
	private static class MockDatabaseSourceConverter
            extends DatabaseSourceConverter {

        private Connection connection = null;

        @Override
        protected Connection getConnection(FacesContext context,
                UIComponent component)
        throws SourceException {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

    }

}
