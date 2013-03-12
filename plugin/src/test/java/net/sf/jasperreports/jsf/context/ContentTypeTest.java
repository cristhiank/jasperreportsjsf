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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class ContentTypeTest {

	@Parameters
    public static Collection<Object[]> parameters() {
    	return Arrays.asList(new Object[][]{ {"text/html"}, {"text/html;encoding=utf-8"} });
    }
	
    private String contentType;
    
    public ContentTypeTest(String contentType) {
    	this.contentType = contentType;
    }
    
    @Test
    public void properlyParsed() {
    	ContentType ct = new ContentType(contentType);
    	assertThat(ct.toString(), equalTo(contentType));
    }
    
}
