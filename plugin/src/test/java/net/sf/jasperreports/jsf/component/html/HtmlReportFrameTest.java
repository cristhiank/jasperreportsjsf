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
package net.sf.jasperreports.jsf.component.html;

import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.renderkit.ReportRenderer;
import net.sf.jasperreports.jsf.renderkit.html.FrameRenderer;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 *
 * @author aalonsodominguez
 */
@RunWith(JMock.class)
public class HtmlReportFrameTest {

    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    private MockFacesEnvironment facesEnv;

    private HtmlReportFrame reportPanel;
    private ReportRenderer renderer;

    @Before
    public void initSupportClasses() {
        facesEnv = new MockFacesServletEnvironment();

        reportPanel = new HtmlReportFrame();
        renderer = context.mock(ReportRenderer.class);
        facesEnv.getRenderKit().addRenderer(reportPanel.getFamily(),
                FrameRenderer.RENDERER_TYPE, renderer);
    }

    @Test
    public void encodeContent() throws Exception {
        final FacesContext facesContext = facesEnv.getFacesContext();

        context.checking(new Expectations() {{
            oneOf(renderer).encodeContent(facesContext, reportPanel);
        }});

        reportPanel.encodeContent(facesContext);

        context.assertIsSatisfied();
    }

    @Test
    public void encodeHeaders() throws Exception {
        final FacesContext facesContext = facesEnv.getFacesContext();

        context.checking(new Expectations() {{
            oneOf(renderer).encodeHeaders(facesContext, reportPanel);
        }});

        reportPanel.encodeHeaders(facesContext);

        context.assertIsSatisfied();
    }

}
