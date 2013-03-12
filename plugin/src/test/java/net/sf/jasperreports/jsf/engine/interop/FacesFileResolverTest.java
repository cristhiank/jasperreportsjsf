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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.jsf.TestConstants;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.resource.Resource;
import net.sf.jasperreports.jsf.resource.ResourceResolver;
import net.sf.jasperreports.jsf.test.dummy.DummyUIReport;
import net.sf.jasperreports.jsf.test.mock.MockFacesEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockFacesServletEnvironment;
import net.sf.jasperreports.jsf.test.mock.MockJRFacesContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author aalonsodominguez
 */
@RunWith(Parameterized.class)
public class FacesFileResolverTest {

    private static String REMOTE_RESOURCE =
            "http://jasperreportjsf.sourceforge.net/web/index.html";
    
    @Parameters
    public static Collection<Object[]> parameters() {
    	Object[][] params;
    	try {
	    	String documentRoot = System.getProperty(
	    			TestConstants.PROP_DOCUMENT_ROOT);
	    	File localFile = new File(documentRoot + "/index.xhtml");
	    	String localResource = localFile.getAbsolutePath();
	    	URL localUrl = localFile.toURI().toURL();
	    	URL remoteUrl = new URL(REMOTE_RESOURCE);
	    	params = new Object[][] { 
	    			{ localResource, localUrl }, 
	    			{ REMOTE_RESOURCE, remoteUrl } 
	    	};
    	} catch (MalformedURLException e) {
    		params = new Object[0][0];
    	}
    	return Arrays.asList(params);
    }
    
    private Mockery mockery = new Mockery();

    private MockFacesEnvironment facesEnv;
    private MockJRFacesContext jrContext;

    private ResourceResolver resourceResolver;
    private UIReport component;
    private Resource resource;

    private String resourceName;
    private URL url;
    
    public FacesFileResolverTest(String resourceName, URL url) {
    	this.resourceName = resourceName;
    	this.url = url;
    }
    
    @Before
    public void init() throws Exception {
        facesEnv = new MockFacesServletEnvironment();
        component = new DummyUIReport();
        resourceResolver = mockery.mock(ResourceResolver.class);
        resource = mockery.mock(Resource.class);
        jrContext = new MockJRFacesContext(facesEnv.getFacesContext());
        jrContext.setResourceResolver(resourceResolver);
    }

    @After
    public void dispose() {
        jrContext = null;

        resourceResolver = null;
        component = null;

        facesEnv.release();
        facesEnv = null;
    }

	@Test
	@SuppressWarnings("unused")
    public void nullReportCompThrowsIllegalArgEx() {
        try {
            FacesFileResolver fr = new FacesFileResolver(null);
            fail("An illegal argument exception should be thrown");
        } catch (Exception e) {
            assertThat(e, is(IllegalArgumentException.class));
        }
    }
	
	@Test
	public void resolveLocalFile() throws Exception {
		assumeThat(url.getProtocol(), not(equalTo("http")));
		
		mockery.checking(new Expectations() {{ 
			oneOf(resourceResolver).resolveResource(facesEnv.getFacesContext(),
                    component, resourceName);
            will(returnValue(resource));
            oneOf(resource).getLocation();
            will(returnValue(url));
            oneOf(resource).getName();
            will(returnValue(resourceName));
		}});
		
		FileResolver fr = new FacesFileResolver(component);
        File file = fr.resolveFile(resourceName);
        
        mockery.assertIsSatisfied();
        
        assertThat(file, not(nullValue()));
        assertThat(file.getAbsolutePath(), equalTo(resourceName));
        assertTrue("File '" + file + "' doesn't exists", file.exists());
	}

	@Test
	public void resolveRemoteFile() throws Exception {
		assumeThat(url.getProtocol(), equalTo("http"));
		
		final InputStream stream = createStream();
		
		mockery.checking(new Expectations() {{ 
			oneOf(resourceResolver).resolveResource(facesEnv.getFacesContext(),
                    component, resourceName);
            will(returnValue(resource));
            atMost(2).of(resource).getLocation();
            will(returnValue(url));
            oneOf(resource).getSimpleName();
            will(returnValue("foo"));
            oneOf(resource).getInputStream();
            will(returnValue(stream));
		}});
		
		FileResolver fr = new FacesFileResolver(component);
        File file = fr.resolveFile(resourceName);
        
        mockery.assertIsSatisfied();
        
        assertThat(file, not(nullValue()));
        assertTrue("File '" + file + "' doesn't exists", file.exists());
	}

    private InputStream createStream() {
        Random random = new Random();
        byte[] buff = new byte[2048 * 2];
        random.nextBytes(buff);
        return new ByteArrayInputStream(buff);
    }

}
