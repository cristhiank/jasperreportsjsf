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
package net.sf.jasperreports.jsf.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class URLResource implements Resource {

    private final URL location;

    protected URLResource(final URL location) {
        if (location == null) {
        	throw new IllegalArgumentException("'location' can't be null");
        }
        this.location = location;
    }

    public String getName() {
    	return location.toExternalForm();
    }
    
    public String getSimpleName() {
    	int slash = location.getPath().lastIndexOf('/');
    	return location.getPath().substring(slash);
    }
    
    public InputStream getInputStream() throws IOException {
        return location.openStream();
    }

    public URL getLocation() throws IOException {
        return location;
    }

    public String getPath() {
    	int slash = location.getPath().lastIndexOf('/');
        return location.getPath().substring(0, slash);
    }
    
    public String toString() {
    	return location.toString();
    }

}
