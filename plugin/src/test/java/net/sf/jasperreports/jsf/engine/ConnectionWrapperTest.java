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
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class ConnectionWrapperTest {

    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void nonNullConnectionReceivesDelegations() throws Exception {
        final Connection expectedConn = mockery.mock(Connection.class);

        ConnectionWrapper wrapper = new ConnectionWrapper(expectedConn);

        mockery.checking(new Expectations() {{
            oneOf(expectedConn).close();
        }});

        Connection conn = wrapper.getConnection();
        assertThat(conn, notNullValue());
        assertThat(conn, sameInstance(expectedConn));

        wrapper.dispose();
        assertThat(wrapper.getConnection(), nullValue());
    }

    @Test
    public void nullConnectionThrowsIllegalArgEx() {
        try {
            new ConnectionWrapper(null);
            fail("An IllegalArgumentException should be thrown");
        } catch (RuntimeException e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

}
