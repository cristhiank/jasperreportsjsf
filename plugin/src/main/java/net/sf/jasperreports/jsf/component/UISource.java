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
package net.sf.jasperreports.jsf.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.context.JRFacesContext;
import net.sf.jasperreports.jsf.convert.SourceConverter;
import net.sf.jasperreports.jsf.engine.Source;
import net.sf.jasperreports.jsf.validation.MissingAttributeException;
import net.sf.jasperreports.jsf.validation.SourceValidatorBase;

import static net.sf.jasperreports.jsf.util.MessagesFactory.*;

/**
 * Faces component (non-rendered) used to feed the report
 * with external data.
 *
 * @author A. Alonso Dominguez
 */
public class UISource extends UIComponentBase {

    /** Specific family name used with report sources. */
    public static final String COMPONENT_FAMILY =
            Constants.PACKAGE_PREFIX + ".Source";

    /** Specific component type name. */
    public static final String COMPONENT_TYPE =
            Constants.PACKAGE_PREFIX + ".Source";

    // Fields

    /** The query used to filter the results. */
    private String query = null;

    /** The type of this source instance. */
    private String type = null;
    /** Flag to identify if type attribute has been locally set. */
    private boolean typeSet = false;

    /** Flag to determine if this source instance is valid. */
    private boolean valid = true;

    /** The source value. */
    private Object value;
    /** Flag to identify if value attribute has been locally set. */
    private boolean valueSet = false;

    /** The source converter instance. */
    private SourceConverter converter;

    /** The source validator instance. */
    private Validator validator;

    /** The interpretted source value. */
    private Source submittedSource;

    /**
     * Instantiates a new data source.
     */
    public UISource() {
        super();
        setRendererType(null);
    }

    // Properties

    /**
     * Gets the query.
     *
     * @return the query
     */
    public final String getQuery() {
        if (query != null) {
            return query;
        }
        final ValueExpression ve = getValueExpression("query");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return query;
        }
    }

    /**
     * Sets the query.
     *
     * @param query the new query
     */
    public final void setQuery(final String query) {
        this.query = query;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public final String getType() {
        if (typeSet) {
            return type;
        }
        final ValueExpression ve = getValueExpression("type");
        if (ve != null) {
            try {
                return (String) ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return type;
        }
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public final void setType(final String type) {
        this.type = type;
        typeSet = true;
    }

    /**
     * Checks if this source configuration is valid.
     *
     * @return if this source instance is valid.
     */
    public final boolean isValid() {
        return valid;
    }

    /**
     * Establishes a new value for the <tt>valid</tt> state.
     *
     * @param valid new valid value.
     */
    protected final void setValid(final boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public final Object getValue() {
        if (valueSet) {
            return value;
        }
        final ValueExpression ve = getValueExpression("value");
        if (ve != null) {
            try {
                return ve.getValue(
                        getFacesContext().getELContext());
            } catch (final ELException e) {
                throw new FacesException(e);
            }
        } else {
            return value;
        }
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public final void setValue(final Object value) {
        this.value = value;
        valueSet = true;
    }

    /**
     * Obtains the <tt>SourceConverter</tt> instance.
     *
     * @return current <tt>SourceConverter</tt> instance.
     */
    public final SourceConverter getConverter() {
        if (converter != null) {
            return converter;
        }
        ValueExpression ve = getValueExpression("converter");
        if (ve != null) {
            try {
                return (SourceConverter) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return converter;
        }
    }

    /**
     * Establishes a new <tt>SourceConverter</tt> instance.
     *
     * @param converter the new <tt>SourceConverter</tt> instance.
     */
    public final void setConverter(final SourceConverter converter) {
        this.converter = converter;
    }

    public Validator getValidator() {
        if (validator != null) {
            return validator;
        }
        ValueExpression ve = getValueExpression("validator");
        if (ve != null) {
            try {
                return (Validator) ve.getValue(
                        getFacesContext().getELContext());
            } catch (ELException e) {
                throw new FacesException(e);
            }
        } else {
            return validator;
        }
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Source getSubmittedSource() {
        return submittedSource;
    }

    public void setSubmittedSource(Source submittedSource) {
        this.submittedSource = submittedSource;
    }

    // UIComponent

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void resetValue() {
        setValid(false);
        setSubmittedSource(null);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.component.UIComponentBase#restoreState(javax.faces.context
     * .FacesContext, java.lang.Object)
     */
    @Override
    public void restoreState(final FacesContext context, final Object state) {
        final Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        query = (String) values[1];
        type = (String) values[2];
        typeSet = ((Boolean) values[3]).booleanValue();
        converter = (SourceConverter) values[4];
        validator = (Validator) values[5];
        valid = ((Boolean) values[6]).booleanValue();
        value = values[7];
        valueSet = ((Boolean) values[8]).booleanValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.
     * FacesContext)
     */
    @Override
    public Object saveState(final FacesContext context) {
        final Object[] values = new Object[9];
        values[0] = super.saveState(context);
        values[1] = query;
        values[2] = type;
        values[3] = Boolean.valueOf(typeSet);
        values[4] = converter;
        values[5] = validator;
        values[6] = valid;
        values[7] = value;
        values[8] = valueSet;
        return values;
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        resetValue();
        super.processDecodes(context);

        try {
            executeValidate(context);
            decodeValue(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        super.processValidators(context);
        executeValidate(context);
    }

    public void decodeValue(FacesContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        SourceConverter aConverter = getConverter();
        if (aConverter == null) {
            aConverter = getJRFacesContext()
                    .createSourceConverter(context, this);
        }

        Source source = aConverter.convertFromValue(
                context, this, getValue());
        setSubmittedSource(source);
    }

    public void validate(FacesContext context) throws ValidatorException {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        String aType = getType();
        if (aType == null) {
            FacesMessage message = createMessage(context,
                    FacesMessage.SEVERITY_FATAL, "MISSING_ATTRIBUTE", "type");
            throw new MissingAttributeException(message);
        }

        Object aValue = getValue();
        if (aValue == null) {
            FacesMessage message = createMessage(context,
                    FacesMessage.SEVERITY_FATAL, "MISSING_ATTRIBUTE", "value");
            throw new MissingAttributeException(message);
        }

        Validator aValidator = getValidator();
        if (aValidator == null) {
            aValidator = getFacesContext().getApplication()
                    .createValidator(SourceValidatorBase.VALIDATOR_TYPE);
        }

        if (aValidator != null) {
            aValidator.validate(context, this, getValue());
        }
    }

    protected void executeValidate(FacesContext context)
            throws ValidatorException {
//        Object providedValue = getValue();
        if (!isValid()) {
            try {
                validate(context);
                setValid(true);
            } catch(ValidatorException e) {
                setValid(false);
                context.renderResponse();
                throw e;
            }
        }
    }

    /**
     * Obtains the current <tt>JRFacesContext</tt> instance.
     *
     * @return current <tt>JRFacesContext</tt> instance.
     */
    protected final JRFacesContext getJRFacesContext() {
        return JRFacesContext.getInstance(getFacesContext());
    }

}
