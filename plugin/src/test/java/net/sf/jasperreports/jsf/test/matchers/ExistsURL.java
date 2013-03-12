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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 *
 * @author aalonsodominguez
 */
public class ExistsURL extends BaseMatcher<URL> {

    @Factory
    public static Matcher<URL> existsURL() {
        return new ExistsURL();
    }

    public boolean matches(Object item) {
        if (item == null) return false;

        URL url;
        if (item instanceof URL) {
            url = (URL) item;
        } else if (item instanceof String) {
            String urlAsText = (String) item;
            try {
                url = new URL(urlAsText);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(urlAsText, e);
            }
        } else {
            throw new IllegalArgumentException("illegal item type: " +
                    item.getClass().getName());
        }

        try {
            URLConnection conn = url.openConnection();
            conn.getInputStream();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void describeTo(Description description) {
        description.appendText("exists url");
    }

}
