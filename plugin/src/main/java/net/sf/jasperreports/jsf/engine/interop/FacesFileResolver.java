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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.JRFacesException;
import net.sf.jasperreports.jsf.component.UIReport;
import net.sf.jasperreports.jsf.context.ExternalContextHelper;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.resource.Resource;

/**
 * Integration of the JasperReports' <tt>FileResolver</tt>
 * with the plugin's resource resolving mechanism.
 * 
 * @author A. Alonso Dominguez
 */
public class FacesFileResolver implements FileResolver {

	/** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            FacesFileResolver.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    private static final int BUFFER_SIZE = 2048;
    
    private final UIReport report;

    public FacesFileResolver(final UIReport report) {
        super();
        if (report == null) {
            throw new IllegalArgumentException("'context' can't be null");
        }
        this.report = report;
    }

    public File resolveFile(final String name) {
        File resultFile;

        Resource resource;
        try {
            resource = resolveResource(name);
            if (isRemote(resource)) {
                resultFile = downloadResource(resource);
            } else {
                resultFile = new File(resource.getName());
            }
        } catch (final IOException e) {
            throw new JRFacesException(e);
        }
        
        if (logger.isLoggable(Level.FINE)) {
        	logger.log(Level.FINE, "JRJSF_0038", new Object[]{ 
        			resultFile.getAbsolutePath(),
        			report.getClientId(getFacesContext())
        	});
        }

        return resultFile;
    }

    protected Resource resolveResource(String name) throws IOException {
        return getJRFacesContext().createResource(
                getFacesContext(), report, name);
    }

    protected File downloadResource(Resource resource) throws IOException {
    	File tempFile = File.createTempFile(resource.getSimpleName(), null);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "JRJSF_0035", new Object[]{
                resource.getLocation(), tempFile
            });
        }

        tempFile.createNewFile();
        
        InputStream is = resource.getInputStream();
        OutputStream os = new FileOutputStream(tempFile);
        
        try {
            int read;
            byte[] buff = new byte[BUFFER_SIZE];
            while (0 < (read = is.read(buff))) {
                os.write(buff, 0, read);
            }
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) { ; }
            try {
                os.close();
                os = null;
            } catch (IOException e) { ; }
        }

        return tempFile;
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

    protected boolean isRemote(Resource resource) throws IOException {
        URL resourceURL = resource.getLocation();
        if (!"file".equals(resourceURL.getProtocol())) {
            ExternalContextHelper helper = getJRFacesContext()
                    .getExternalContextHelper(getFacesContext());
            return !(resourceURL.getHost().equals(helper.getRequestServerName(
                    getFacesContext().getExternalContext())));
        }
        return false;
    }
    
}
