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

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author antonio.alonso
 */
public final class MessagesFactory {

    private static final String BUILTIN_MESSAGE_BUNDLE =
            "net.sf.jasperreports.jsf.FacesMessages";

    public static FacesMessage createMessage(FacesContext context,
            FacesMessage.Severity severity, String messageId, Object... args) {
        ResourceBundle bundle = getResourceBundle(context);
        String message = bundle.getString(messageId + "_detail");
        String detail = formatMessage(message, args);
        String summary = bundle.getString(messageId);

        return new FacesMessage(severity, summary, detail);
    }

    public static FacesMessage createMessage(FacesMessage.Severity severity,
            String msg, Object... args) {
        String text = formatMessage(msg, args);

        FacesMessage message = new FacesMessage();
        message.setSeverity(severity);
        message.setDetail(text);
        message.setSummary(text);
        return message;
    }

    private static ResourceBundle getResourceBundle(FacesContext context) {
        String messageBundle = context.getApplication().getMessageBundle();
        if (messageBundle == null || messageBundle.length() == 0) {
            messageBundle = BUILTIN_MESSAGE_BUNDLE;
        }
        return ResourceBundle.getBundle(messageBundle);
    }

    private static String formatMessage(String text, Object... args) {
        return MessageFormat.format(text, args);
    }

    private MessagesFactory() { }

}
