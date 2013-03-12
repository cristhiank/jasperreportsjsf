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

import net.sf.jasperreports.jsf.engine.converters.XmlSourceConverter;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
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
public class XmlSourceConverterTest {

    @DataPoint
    public static final Object NULL_VALUE = null;

    private MockFacesEnvironment facesEnv;
    private UIReport component;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        component = new DummyUIReport();
    }

    @After
    public void dispose() {
        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void nullValueReturnsEmptyDS(Object value) {
        assumeThat(value, nullValue());

        component.setValue(value);
        XmlSourceConverter converter = new XmlSourceConverter();
        Source source = converter.createSource(
                facesEnv.getFacesContext(), component, value);

        assertThat(source, notNullValue());
        assertThat(source, is(JRDataSourceWrapper.class));

        JRDataSource ds = ((JRDataSourceWrapper) source).getDataSource();
        assertThat(ds, notNullValue());
        assertThat(ds, is(JREmptyDataSource.class));
    }

}
