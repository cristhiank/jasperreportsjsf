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
package net.sf.jasperreports.jsf.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.ContentType;
import net.sf.jasperreports.jsf.context.JRFacesContext;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;
import static net.sf.jasperreports.jsf.util.MessagesFactory.*;

public class ReportValidatorBase implements Validator {

    public static final String VALIDATOR_TYPE =
            Constants.PACKAGE_PREFIX + ".Report";

    public void validate(FacesContext context, UIComponent component, Object value)
    throws ValidatorException {
        final JRFacesContext jrContext = JRFacesContext.getInstance(context);
        String format = getStringAttribute(component, "format", null);
        if (format != null && format.length() > 0) {
        	boolean valid = false;
        	if (ContentType.isContentType(format)) {
        		ContentType ct = new ContentType(format);
        		valid = jrContext.getSupportedContentTypes().contains(ct);
        	} else {
        		valid = jrContext.getAvailableExportFormats().contains(format);
        	}
        	
            if (!valid) {
                FacesMessage message = createMessage(context,
                        FacesMessage.SEVERITY_FATAL, "ILLEGAL_OUTPUT_FORMAT", format);
                throw new IllegalOutputFormatException(message);
            }
        }
    }
}
