package net.sf.jasperreports.jsf.test.webapp;

import java.io.File;

class MockWebappPortletContainer extends MockWebappServletContainer {

	public MockWebappPortletContainer(File contextDir, String contextPath,
			String webXml) {
		super(contextDir, contextPath, webXml);
	}

}
