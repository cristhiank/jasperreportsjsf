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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 *
 * @author aalonsodominguez
 */
public final class FileResource implements Resource {

    private final File file;

    protected FileResource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("'file' can't be null");
        }
        this.file = file;
    }

    public String getName() {
        return file.getAbsolutePath();
    }
    
    public String getSimpleName() {
    	return file.getName();
    }
    
    public InputStream getInputStream() throws IOException {
        if (file.isDirectory()) {
            throw new ResourceException("Can't get a stream from a directory.");
        }
        return new FileInputStream(file);
    }

    public URL getLocation() throws IOException {
        return new URL("file://" + file.getAbsolutePath());
    }

    public String getPath() {
        if (file.isFile()) {
            return file.getParentFile().getAbsolutePath();
        } else {
            return file.getAbsolutePath();
        }
    }

    public String toString() {
    	return file.getAbsolutePath();
    }
    
}
