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

import net.sf.jasperreports.jsf.engine.converters.MapSourceConverter;
import net.sf.jasperreports.jsf.engine.JRDataSourceWrapper;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.jsf.component.UISource;
import net.sf.jasperreports.jsf.engine.Source;
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
public class MapSourceConverterTest {

    @DataPoint
    public static final Object NULL_DATA = null;

	@DataPoint
	@SuppressWarnings("rawtypes")
    public static final Map[] ARR_DATA = new Map[]{};

    @DataPoint
    @SuppressWarnings("rawtypes")
    public static final Map SINGLE_DATA = new HashMap();

    @DataPoint
    public static final Collection<?> COLL_DATA = Arrays.asList(ARR_DATA);

    private MockFacesEnvironment facesEnv;
    private UISource component;
    private MapSourceConverter factory;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        component = new UISource();
        factory = new MapSourceConverter();
    }

    @After
    public void dispose() {
        factory = null;
        component = null;

        facesEnv.release();
        facesEnv = null;
    }

    @Theory
    public void anyDataReturnsArrayDataSource(final Object data) {
        assumeThat(data, is(not(nullValue())));
        assumeThat(data, is(not(instanceOf(Collection.class))));

        final FacesContext facesContext = facesEnv.getFacesContext();

        component.setValue(data);
        Source reportSource = factory.createSource(
                facesContext, component, data);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceWrapper)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceWrapper) reportSource)
                .getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRMapArrayDataSource.class));

        if (data instanceof Map[]) {
            JRMapArrayDataSource bads = (JRMapArrayDataSource) dataSource;
            assertThat(bads.getData(), equalTo((Object[]) data));
        }
    }

    @Theory
    @SuppressWarnings("rawtypes")
    public void collectionDataReturnsCollectionDataSource(Object data) {
        assumeThat(data, is(not(nullValue())));
        assumeThat(data, is(Collection.class));

        final FacesContext facesContext = facesEnv.getFacesContext();

        component.setValue(data);
        Source reportSource = factory.createSource(
                facesContext, component, data);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceWrapper)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceWrapper) reportSource)
                .getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JRMapCollectionDataSource.class));

        JRMapCollectionDataSource bacs =
                (JRMapCollectionDataSource) dataSource;
        assertThat(bacs.getData(), equalTo((Collection) data));
    }

    @Theory
    public void nullDataReturnsEmptyDataSource(Object data) {
        assumeThat(data, is(nullValue()));

        Source reportSource = factory.createSource(
                facesEnv.getFacesContext(), component, data);
        assertThat(reportSource, is(not(nullValue())));

        if (!(reportSource instanceof JRDataSourceWrapper)) {
            fail("'Returned reportSource is not JasperReport's data source wrapper");
        }

        JRDataSource dataSource = ((JRDataSourceWrapper) reportSource)
                .getDataSource();
        assertThat(dataSource, is(not(nullValue())));
        assertThat(dataSource, is(JREmptyDataSource.class));
    }
}
