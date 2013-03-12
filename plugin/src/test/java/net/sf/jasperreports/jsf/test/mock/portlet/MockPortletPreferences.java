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
package net.sf.jasperreports.jsf.test.mock.portlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

/**
 *
 * @author aalonsodominguez
 */
public class MockPortletPreferences implements PortletPreferences {

    public boolean isReadOnly(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getValue(String key, String def) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getValues(String key, String[] def) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setValue(String key, String value) throws ReadOnlyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setValues(String key, String[] values) throws ReadOnlyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration<String> getNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, String[]> getMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reset(String key) throws ReadOnlyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void store() throws IOException, ValidatorException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
