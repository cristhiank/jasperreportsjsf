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
package net.sf.jasperreports.jsf.engine.interop;

import net.sf.jasperreports.engine.export.JRHyperlinkProducer;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;

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
public class FacesHyperlinkProducerFactoryTest {

    @DataPoint
    public static final String NULL_LINK_TYPE = null;

    @DataPoint
    public static final String EMPTY_LINK_TYPE = "";

    @DataPoint
    public static final String VALID_LINK_TYPE = Constants.FACES_HYPERLINK_TYPE;
    
    @Theory
    public void nullComponentThrowsIllegalArgEx() {
        try {
            new FacesHyperlinkProducerFactory(null);
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }

    public void invalidTypeReturnsNull(final String linkType) {
    	assumeThat(linkType, not(equalTo(Constants.FACES_HYPERLINK_TYPE)));
    	
        UIReport report = new DummyUIReport();
        FacesHyperlinkProducerFactory factory =
                new FacesHyperlinkProducerFactory(report);
        JRHyperlinkProducer producer = factory.getHandler(linkType);
        
        assertThat(producer, nullValue());
    }
    
    @Theory
    public void validTypeReturnsProducer(final String linkType) {
    	assumeThat(linkType, equalTo(Constants.FACES_HYPERLINK_TYPE));
        UIReport report = new DummyUIReport();
        FacesHyperlinkProducerFactory factory =
                new FacesHyperlinkProducerFactory(report);
        JRHyperlinkProducer producer = factory.getHandler(linkType);

        assertThat(producer, notNullValue());
        assertThat(producer, is(FacesHyperlinkProducer.class));

    }

}
