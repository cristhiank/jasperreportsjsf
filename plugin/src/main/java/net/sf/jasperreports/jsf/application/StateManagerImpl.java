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
package net.sf.jasperreports.jsf.application;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.application.StateManagerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;

import net.sf.jasperreports.jsf.Constants;
import net.sf.jasperreports.jsf.util.Util;

/**
 * JSF's StateManagerWrapper implementation used to cache the
 * current view state so it can be restored in the future.
 *
 * @author A. Alonso Dominguez
 */
public class StateManagerImpl extends StateManagerWrapper {

	private static final Logger logger = Logger.getLogger(
			StateManagerImpl.class.getPackage().getName(), 
			Constants.LOG_MESSAGES_BUNDLE);
	
    /** Established size for the buffer used when parsing the response. */
    private static final int BUFFER_SIZE = 128;

    /** First delegate in our StateManager implementation chain. */
    private final StateManager delegate;

    /**
     * Constructor for the StateManagerWrapper implmentation.
     *
     * @param delegate first delegate in the implementation chain.
     */
    public StateManagerImpl(final StateManager delegate) {
        this.delegate = delegate;
    }

    /**
     * Writes the given state into the current response.
     * <p>
     * This implementation of the <tt>writeState</tt> method buffers the
     * current response before bypassing the responsiveness of writing the
     * response state. This allows to the method to cache the current state
     * date into the request attribute map so it can be restored in the future.
     *
     * @param context current FacesContext instance.
     * @param state current view state.
     * @throws IOException when some output errors happen when
     *         encding the state.
     *
     */
    @Override
    public final void writeState(final FacesContext context, final Object state)
    throws IOException {
    	Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
    	if (!Boolean.TRUE.equals(
                requestMap.get(Constants.ATTR_REPORT_VIEW))) {
    		super.writeState(context, state);
    		return;
    	}
    	
        final ResponseWriter oldWriter = context.getResponseWriter();
        final StringWriter sw = new StringWriter(BUFFER_SIZE);
        final ResponseWriter newWriter = oldWriter.cloneWithWriter(sw);
        
        if (logger.isLoggable(Level.FINE)) {
        	logger.log(Level.FINE, "JRJSF_0043");
        }
        
        // Replace the ResponseWriter in order to intercept the view state value
        context.setResponseWriter(newWriter);
        super.writeState(context, state);
        // Restore the original ResponseWriter
        context.setResponseWriter(oldWriter);

        // Obtain the written state
        newWriter.flush();
        final StringBuffer buffer = sw.getBuffer();
        oldWriter.write(buffer.toString());
        
        final String viewState = getViewState(buffer.toString());
        if (viewState != null) {
        	if (logger.isLoggable(Level.FINER)) {
        		logger.log(Level.FINER, "JRJSF_0044", viewState);
        	}
            requestMap.put(Constants.ATTR_VIEW_STATE, viewState);
        }
    }

	@Override
	@SuppressWarnings("deprecation")
	public void writeState(FacesContext context, SerializedView state)
			throws IOException {
    	Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
    	if (!Boolean.TRUE.equals(
                requestMap.get(Constants.ATTR_REPORT_VIEW))) {
    		super.writeState(context, state);
    		return;
    	}
    	
        final ResponseWriter oldWriter = context.getResponseWriter();
        final StringWriter sw = new StringWriter(BUFFER_SIZE);
        final ResponseWriter newWriter = oldWriter.cloneWithWriter(sw);
        
        if (logger.isLoggable(Level.FINE)) {
        	logger.log(Level.FINE, "JRJSF_0043");
        }
        
        // Replace the ResponseWriter in order to intercept the view state value
        context.setResponseWriter(newWriter);
        super.writeState(context, state);
        // Restore the original ResponseWriter
        context.setResponseWriter(oldWriter);

        // Obtain the written state
        newWriter.flush();
        final StringBuffer buffer = sw.getBuffer();
        oldWriter.write(buffer.toString());
        
        final String viewState = getViewState(buffer.toString());
        if (viewState != null) {
        	if (logger.isLoggable(Level.FINER)) {
        		logger.log(Level.FINER, "JRJSF_0044", viewState);
        	}
            requestMap.put(Constants.ATTR_VIEW_STATE, viewState);
        }
	}

	/**
     * Obtains the first delegate in the implementation chain.
     *
     * @return first delegate in the chain.
     */
    @Override
    protected final StateManager getWrapped() {
        return delegate;
    }

    /**
     * Parses the view state data from the response buffer.
     *
     * @param buffer the response buffer as a string.
     * @return the encoded view state.
     */
    private String getViewState(String buffer) {
        int i = buffer.indexOf(ResponseStateManager.VIEW_STATE_PARAM);
        if (i < 0) {
            return null;
        }

        int end = buffer.indexOf("/>", i);
        if (end < 0) {
            return null;
        }

        int valStart = buffer.indexOf("value", i);
        if (valStart < 0 || valStart >= end) {
            buffer = buffer.substring(0, end);
            end = buffer.length() - 1;
            i = buffer.lastIndexOf('<');
            if (i < 0) {
                return null;
            }
            valStart = buffer.indexOf("value", i);
            if (valStart < 0) {
                return null;
            }
        }

        // Really locate the value boundaries
        valStart = buffer.indexOf('"', valStart);
        if (valStart < 0) {
            return null;
        }
        final int valEnd = buffer.indexOf('"', valStart + 1);
        if (valEnd < 0 || valEnd > end) {
            return null;
        }

        // Extract the value
        return buffer.substring(valStart + 1, valEnd);
    }

}
