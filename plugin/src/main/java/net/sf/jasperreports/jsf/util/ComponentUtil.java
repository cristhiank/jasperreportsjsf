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
package net.sf.jasperreports.jsf.util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

/**
 *
 * @author aalonsodominguez
 */
public final class ComponentUtil {

    public static boolean getBooleanAttribute(UIComponent component,
            String name, boolean defaultValue) {
        Object value = component.getAttributes().get(name);
        if (value == null) {
            return defaultValue;
        } else if (!(value instanceof Boolean)) {
            return defaultValue;
        } else {
            return ((Boolean) value).booleanValue();
        }
    }

    public static int getIntegerAttribute(UIComponent component,
            String name, int defaultValue) {
        Object value = component.getAttributes().get(name);
        if (value == null) {
            return defaultValue;
        } else if (!(value instanceof Integer)) {
            return defaultValue;
        } else {
            return ((Integer) value).intValue();
        }
    }

    public static String getStringAttribute(UIComponent component,
            String name, String defaultValue) {
        Object value = component.getAttributes().get(name);
        if (value == null) {
            return defaultValue;
        } else if (!(value instanceof String)) {
            return defaultValue;
        } else {
            return ((String) value);
        }
    }

    public static void setBooleanAttribute(UIComponent component,
            String name, ValueExpression ve) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }

        if (ve != null) {
            if (ve.isLiteralText()) {
                component.getAttributes().put(name,
                        Boolean.valueOf(ve.getExpressionString()));
            } else {
                component.setValueExpression(name, ve);
            }
        }
    }

    public static void setIntegerAttribute(UIComponent component,
            String name, ValueExpression ve) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }

        if (ve != null) {
            if (ve.isLiteralText()) {
                component.getAttributes().put(name,
                        Integer.valueOf(ve.getExpressionString()));
            } else {
                component.setValueExpression(name, ve);
            }
        }
    }

    public static void setStringAttribute(UIComponent component,
            String name, ValueExpression ve) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException();
        }

        if (ve != null) {
            if (ve.isLiteralText()) {
                component.getAttributes().put(name,
                        ve.getExpressionString());
            } else {
                component.setValueExpression(name, ve);
            }
        }
    }

    private ComponentUtil() { }

}
