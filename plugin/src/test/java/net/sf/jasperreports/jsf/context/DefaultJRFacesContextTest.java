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
package net.sf.jasperreports.jsf.context;

import java.util.Arrays;
import java.util.Collection;
import net.sf.jasperreports.jsf.component.UIOutputReport;
import net.sf.jasperreports.jsf.convert.ReportConverter;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Parameterized.class)
public class DefaultJRFacesContextTest {
    
    private static final String COMPILED_REPORT_RESOURCE_NAME =
            "/reports/productCategory.jasper";
    
    private static final String SOURCE_REPORT_RESOURCE_NAME =
            "/reports/productCategory.jrxml";
    
    @Parameters
    public static Collection<Object[]> parameters() {
        Object[][] values = new Object[][] { 
            {COMPILED_REPORT_RESOURCE_NAME}, {SOURCE_REPORT_RESOURCE_NAME} };
        return Arrays.asList(values);
    }
    
    private String reportResourceName;
    
    private MockFacesEnvironment facesEnv;
    private DefaultJRFacesContext jrContext = new DefaultJRFacesContext();
    private UIOutputReport report;
    
    public DefaultJRFacesContextTest(String reportResourceName) {
        this.reportResourceName = reportResourceName;
    }
    
    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();
        jrContext = new DefaultJRFacesContext();
        report = new UIOutputReport();
        report.setValue(reportResourceName);
    }
    
    @Test
    public void resolveReportConverter() {
        ReportConverter converter = jrContext.createReportConverter(
                facesEnv.getFacesContext(), report);
        assertThat(converter, notNullValue());
    }
    
}
