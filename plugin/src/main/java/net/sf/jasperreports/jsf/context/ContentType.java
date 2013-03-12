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
package net.sf.jasperreports.jsf.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ContentType implements Serializable, Comparable<ContentType> {

    /**
     *
     */
    private static final long serialVersionUID = 5337880747998334976L;

    private static final Pattern PATTERN = 
            Pattern.compile("(.+)\\/(.+)(;(.+)=(.+))*");

    public static boolean isContentType(String value) {
        return PATTERN.matcher(value).matches();
    }

    private String type;
    private String subtype;

    private Map<String, String> parameters =
            new HashMap<String, String>();
    
    public ContentType(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Value '" + value
                                               + "' is not a valid MIME type.");
        }
        
        this.type = matcher.group(1);
        this.subtype = matcher.group(2);
        
        for (int i = 3;(i + 2) < matcher.groupCount();i+=3) {
            String pname = matcher.group(i + 1);
            String pvalue = matcher.group(i + 2);
            parameters.put(pname, pvalue);
        }
    }

    public int compareTo(ContentType o) {
        if (this.equals(o)) {
            return 0;
        }
        if (this.implies(o)) {
            return 1;
        }
        if (o.implies(this)) {
            return -1;
        }
        return 0;
    }

    public boolean implies(ContentType other) {
        if (null == other) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (equals(other)) {
            return true;
        }

        if (!type.equals("*") && (!type.equals(other.type))) {
            return false;
        }
        if (!subtype.equals("*") && (!subtype.equals(other.subtype))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (7 * type.hashCode()) + 
                (11 * subtype.hashCode()) +
                (13 * parameters.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentType)) {
            return false;
        }

        final ContentType other = (ContentType) o;
        return type.equals(other.type)
               && subtype.equals(other.subtype)
               && parameters.equals(other.parameters);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(type).append("/").append(subtype);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            str.append(";");
            str.append(entry.getKey());
            str.append("=");
            str.append(entry.getValue());
        }
        return str.toString();
    }
}
