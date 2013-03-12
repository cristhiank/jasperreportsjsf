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
package net.sf.jasperreports.jsf.test.matchers;

import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 *
 * @author aalonsodominguez
 */
public class UnexistantResource extends BaseMatcher<String> {

    @Factory
    public static Matcher<String> unexistantResource() {
        return new UnexistantResource();
    }

    @Factory
    public static Matcher<String> unexistantResource(ClassLoader classLoader) {
        return new UnexistantResource(classLoader);
    }

    private ClassLoader classLoader;

    public UnexistantResource() { }

    public UnexistantResource(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = UnexistantResource.class.getClassLoader();
            }
        }
        return classLoader;
    }

    public boolean matches(Object item) {
        if (item == null) return false;
        
        String resourceName;
        if (item instanceof String) {
            resourceName = (String) item;
        } else {
            resourceName = item.toString();
        }
        
        ClassLoader loader = getClassLoader();
        InputStream stream = loader.getResourceAsStream(resourceName);
        
        if (stream == null) {
            return true;
        } else {
            try {
                stream.close();
            } catch (IOException e) { }
            return false;
        }
    }

    public void describeTo(Description description) {
        description.appendText("resource no exists");
    }
    
}
