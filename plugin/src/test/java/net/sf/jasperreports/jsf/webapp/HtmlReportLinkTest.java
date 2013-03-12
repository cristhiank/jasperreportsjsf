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
package net.sf.jasperreports.jsf.webapp;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.test.ComponentProfilerTestCase;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class HtmlReportLinkTest extends ComponentProfilerTestCase {

	private static final Logger logger = Logger.getLogger(
			HtmlReportLinkTest.class.getName(), 
			Constants.LOG_MESSAGES_BUNDLE);
	
    @Parameters
    public static Collection<?> linkIdentifiers() {
        return Arrays.asList(new Object[][]{{"reportForm:reportLink"}});
    }
    
    private final String linkId;

    public HtmlReportLinkTest(final String linkId) {
        this.linkId = linkId;
    }

    @Test
    public void clickOnLink() throws Exception {
        final WebResponse reportView = getComponentView();
        
        final WebLink link = reportView.getLinkWithID(linkId);
        assertThat(link, notNullValue());
        assertThat(link.getText(), equalTo("ReportLink"));
        
        logger.log(Level.INFO, "Clicking link: {0}", link.getID());
        
        final WebResponse reportResponse = link.click();
        assertThat(reportResponse.getContentType(), equalTo("text/html"));
    }
}
