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
package net.sf.jasperreports.jsf.test.webapp;

import java.io.File;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 *
 * @author aalonsodominguez
 */
class MockWebappServletContainer extends MockWebappContainer {

    private File contextDir;
    private String contextPath;
    private File webXml;

    private ServletRunner runner = null;
    private boolean started = false;

    public MockWebappServletContainer(File contextDir, String contextPath,
            String webXml) {
        this.contextDir = contextDir;
        this.contextPath = contextPath;
        this.webXml = new File(this.contextDir, webXml);
    }

    public File getContextDir() {
		return contextDir;
	}

	public String getContextPath() {
        return contextPath;
    }

    public File getWebXml() {
        return webXml;
    }

    public boolean isStarted() {
        return started;
    }

    public ServletUnitClient createClient() {
        if (!started) {
            throw new IllegalArgumentException("ServletRunner not started");
        }

        return runner.newClient();
    }

    public void start() throws Exception {
        if (started) {
            throw new IllegalArgumentException("ServletRunner already started");
        }

        runner = new ServletRunner(webXml, contextPath);
        started = true;
    }

    public void stop() {
        if (!started) {
            throw new IllegalArgumentException("ServletRunner not started");
        }

        runner.shutDown();
        runner = null;
        started = false;
    }

}
