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

/**
 * Resource abstraction which can be used to load reports from any kind of
 * source
 * 
 * @author antonio.alonso
 * 
 */
public interface Resource {

    /**
     * Name of the resource.
     */
    public String getName();

    public String getSimpleName();
    
    /**
     * Obtains the URL location of this resource.
     *
     * @return The location of the resource
     * @throws IOException
     */
    public URL getLocation() throws IOException;

    /**
     * Obtains an InputStream which can be used to read the resource data.
     * <p>
     * Users of this stream are responsible of closing it
     *
     * @return An InputStream to read the resource data
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException;

    /**
     * The path part of the resource.
     *
     * @return the absolute path to the resource.
     */
    public String getPath();
}
