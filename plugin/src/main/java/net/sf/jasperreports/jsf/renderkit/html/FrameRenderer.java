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
package net.sf.jasperreports.jsf.renderkit.html;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.jasperreports.jsf.Constants;

import static net.sf.jasperreports.jsf.util.ComponentUtil.*;

/**
 * Renderer for report frames.
 * <p>
 * Draws an inline HTML <tt>iframe</tt> element in the
 * containing view. Report contents will be shown inside
 * that nested frame element.
 * 
 * @author A. Alonso Dominguez
 */
public final class FrameRenderer extends HtmlReportRenderer {

    /** The constant representing the renderer's content disposition. */
    public static final String CONTENT_DISPOSITION = "inline";

    /** The renderer type. */
    public static final String RENDERER_TYPE =
            Constants.PACKAGE_PREFIX + ".Frame";

    /**
     * HTML Component attributes that will be rendered as they have
     * been establised.
     */
    private static final String[] PASSTHRU_ATTRS = {
        "frameborder", "marginheight", "marginwidth", "height", "width"
    };

    /** The logger. */
    private static final Logger logger = Logger.getLogger(
            FrameRenderer.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /**
     * Obtains the default content disposition for this renderer.
     *
     * @return the default content disposition
     */
    @Override
	public String getContentDisposition() {
        return CONTENT_DISPOSITION;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent)
     */
    @Override
    public void encodeBegin(final FacesContext context,
            final UIComponent component)
    throws IOException {
        logger.log(Level.FINE, "JRJSF_0002", component.getClientId(context));

        final String reportURI = encodeReportURL(context, component);

        final ResponseWriter writer = context.getResponseWriter();

        String layout = getStringAttribute(component, "layout", "inline");

        if ("block".equals(layout)) {
            writer.startElement("br", component);
        }

        writer.startElement("iframe", component);
        renderIdAttribute(context, component);
        writer.writeURIAttribute("src", reportURI, null);

        renderAttributes(writer, component);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent)
     */
    @Override
    public void encodeEnd(final FacesContext context,
            final UIComponent component)
    throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.endElement("iframe");

        String layout = getStringAttribute(component, "layout", "inline");

        if ("block".equals(layout)) {
            writer.startElement("br", component);
        }

        registerReportView(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * net.sf.jasperreports.jsf.renderkit.AbstractReportRenderer#
     * renderAttributes(javax.faces.context.ResponseWriter,
     * javax.faces.component.UIComponent)
     */
    @Override
    protected void renderAttributes(final ResponseWriter writer,
            final UIComponent report)
    throws IOException {
        super.renderAttributes(writer, report);

        for (final String attrName : PASSTHRU_ATTRS) {
            final Object value = report.getAttributes().get(attrName);
            if (value != null) {
                writer.writeAttribute(attrName, value, null);
            }
        }
    }
}
