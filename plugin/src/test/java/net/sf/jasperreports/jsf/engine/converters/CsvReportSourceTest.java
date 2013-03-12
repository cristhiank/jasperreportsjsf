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

import java.io.InputStream;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.jsf.engine.converters.CsvSourceConverter.CsvReportSource;
import net.sf.jasperreports.jsf.test.JMockTheories;
import org.jmock.Expectations;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
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
public class CsvReportSourceTest {

    @DataPoint
    public static final String NULL_CLIENTID = null;

    @DataPoint
    public static final String EMPTY_CLIENTID = "";

    @DataPoint
    public static final String VALID_CLIENTID = "clientId";

    private Mockery mockery = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private JRDataSource dataSource;

    @Before
    public void init() {
        dataSource = mockery.mock(JRDataSource.class);
    }

	@Theory
	@SuppressWarnings("unused")
    public void nullClientIdThrowsIllegalArgEx(String clientId) {
        assumeThat(clientId, nullValue());

        CsvReportSource source;
        try {
            source = new CsvReportSource(clientId, dataSource, null);
            fail("An IllegalArgumentException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    @SuppressWarnings("unused")
    public void emptyClientIdThrowsIllegalArgEx(String clientId) {
        assumeThat(clientId, notNullValue());
        assumeTrue(clientId.length() == 0);

        CsvReportSource source;
        try {
            source = new CsvReportSource(clientId, dataSource, null);
            fail("An IllegalArgumentException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    @Theory
    public void nullInputStreamWorksFine(final String expectedClientId)
    throws Exception {
        assumeThat(expectedClientId, notNullValue());
        assumeTrue(expectedClientId.length() > 0);

        CsvReportSource source = new CsvReportSource(
                expectedClientId, dataSource, null);

        String clientId = source.getClientId();
        assertThat(clientId, notNullValue());
        assertThat(clientId, equalTo(expectedClientId));

        source.dispose();
        assertThat(source.getClientId(), nullValue());
    }

    @Theory
    public void nonNullInputStreamReceivesClose(final String expectedClientId)
    throws Exception {
        assumeThat(expectedClientId, notNullValue());
        assumeTrue(expectedClientId.length() > 0);

        final InputStream stream = mockery.mock(InputStream.class);
        CsvReportSource source = new CsvReportSource(
                expectedClientId, dataSource, stream);

        mockery.checking(new Expectations() {{
            oneOf(stream).close();
        }});

        String clientId = source.getClientId();
        assertThat(clientId, notNullValue());
        assertThat(clientId, equalTo(expectedClientId));

        source.dispose();
        assertThat(source.getClientId(), nullValue());
    }

}
