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
package net.sf.jasperreports.jsf.engine.export;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRAbstractExporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ContentType;
import net.sf.jasperreports.jsf.engine.ExporterException;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Expectations;
import static org.jmock.Expectations.any;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class DefaultExporterTest {

    public static final String REPORT_ID = "reportId";
    public static final String CUSTOM_ENCODING = "UTF-8";
    public static final String CUSTOM_JASPER_PRINT_NAME =
            DefaultExporterTest.class.getName();
    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    private MockFacesEnvironment facesEnv;
    private JRAbstractExporter mockExporter;
    private UIReport report;
    private OutputStream stream;
    private JasperPrint print;

    @Before
    public void init() {
        facesEnv = new MockFacesServletEnvironment();

        report = new DummyUIReport();
        report.setId(REPORT_ID);

        mockExporter = mockery.mock(JRAbstractExporter.class);
        stream = new ByteArrayOutputStream();
        print = mockery.mock(JasperPrint.class);

        report.setSubmittedPrint(print);
    }

    @After
    public void dispose() {
        print = null;
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
        }

        report = null;
        facesEnv.release();
        facesEnv = null;
    }

    @Test
    public void withoutJasperPrintThrowEx() {
        report.setSubmittedPrint(null);
        ExporterBase exporter = new MockDefaultExporter();
        try {
            exporter.export(facesEnv.getFacesContext(), report, stream);
        } catch (Exception e) {
            assertThat(e, is(JasperPrintNotFoundException.class));

            String clientId = report.getClientId(facesEnv.getFacesContext());
            JasperPrintNotFoundException jpnfe =
                    (JasperPrintNotFoundException) e;
            assertThat(jpnfe.getClientId(), equalTo(clientId));
        }
    }

    @Test
    public void usingPrintInitsJRExporter() throws Exception {
        FacesContext context = facesEnv.getFacesContext();

        mockery.checking(new Expectations() {
            {
                oneOf(mockExporter).setJasperReportsContext(with(any(JasperReportsContext.class)));
                atLeast(4).of(mockExporter).setParameter(
                        with(any(JRExporterParameter.class)),
                        with(any(Object.class)));
                oneOf(mockExporter).exportReport();
            }
        });

        ExporterBase exporter = new MockDefaultExporter();
        exporter.export(context, report, stream);
    }

    @Test
    public void withJRExRethrowItWrapped() throws Exception {
        FacesContext context = facesEnv.getFacesContext();
        String reportId = report.getClientId(context);

        final JRException jrException = new JRException(reportId);

        mockery.checking(new Expectations() {
            {
                oneOf(mockExporter).setJasperReportsContext(with(any(JasperReportsContext.class)));
                atLeast(4).of(mockExporter).setParameter(
                        with(any(JRExporterParameter.class)),
                        with(any(Object.class)));
                oneOf(mockExporter).exportReport();
                will(throwException(jrException));
            }
        });

        ExporterBase exporter = new MockDefaultExporter();
        try {
            exporter.export(context, report, stream);
        } catch (Exception e) {
            assertThat(e, is(ExporterException.class));
            assertThat(e.getCause(), notNullValue());
            assertThat(e.getCause(), is(JRException.class));
        }
    }

    private class MockDefaultExporter extends ExporterBase {

        private ContentType contentType;

        @Override
        protected JRAbstractExporter createJRExporter(FacesContext context,
                UIReport component)
                throws ExporterException {
            return mockExporter;
        }

        public ContentType getContentType() {
            return contentType;
        }

        @SuppressWarnings("unused")
        public void setContentType(ContentType contentType) {
            this.contentType = contentType;
        }
    }
}
