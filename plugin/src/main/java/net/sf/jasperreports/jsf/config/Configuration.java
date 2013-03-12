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
package net.sf.jasperreports.jsf.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import net.sf.jasperreports.jsf.Constants;

import net.sf.jasperreports.jsf.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Webapp singleton instance used to hold the plugin's relevant
 * information about current webapp faces' configuration.
 *
 * @author A. Alonso Dominguez
 */
public final class Configuration {

    /** Singleton instance key. */
    protected static final String INSTANCE_KEY =
            Configuration.class.getPackage().getName();

    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(
            Configuration.class.getPackage().getName(),
            Constants.LOG_MESSAGES_BUNDLE);

    /** Faces' servlet class name. */
    private static final String FACES_SERVLET_CLASS =
            FacesServlet.class.getName();

    /** <tt>url-pattern</tt> tag. */
    private static final String TAG_URL_PATTERN = "url-pattern";
    /** <tt>servlet</tt> tag. */
    private static final String TAG_SERVLET = "servlet";
    /** <tt>servlet-class</tt> tag. */
    private static final String TAG_SERVLET_CLASS = "servlet-class";
    /** <tt>servlet-mapping</tt> tag. */
    private static final String TAG_SERVLET_MAPPING = "servlet-mapping";
    /** <tt>servlet-name</tt> tag. */
    private static final String TAG_SERVLET_NAME = "servlet-name";
    /** Web application config file. */
    private static final String WEB_XML = "/WEB-INF/web.xml";

    /**
     * Obtains the singleton configuration instance.
     * <p>
     * If there is not any configuration instance preloaded, the
     * application configuration files will be parsed and loaded
     * into the application scope map.
     *
     * @param context current faces' external context
     * @return the singleton instance
     * @throws ConfigurationException if application is not properly configured.
     */
    public static Configuration getInstance(final ExternalContext context)
            throws ConfigurationException {
        if (context == null) {
            throw new IllegalArgumentException("context");
        }

        Configuration instance = (Configuration) context
                .getApplicationMap().get(INSTANCE_KEY);
        if (instance == null) {
            instance = new Configuration(context);
            context.getApplicationMap().put(INSTANCE_KEY,
                    instance);
        }
        return instance;
    }

    /** Faces' servlet mappings. */
    private final List<String> facesMappings = new ArrayList<String>();
    /** Faces' default mapping. */
    private String defaultMapping;

    /**
     * Constructor from a servlet context.
     *
     * @param context a servlet context.
     * @throws ConfigurationException if application is not properly configured.
     */
    protected Configuration(final ServletContext context)
    throws ConfigurationException {
        final InputStream is = context.getResourceAsStream(WEB_XML);
        loadMappings(is);
    }

    /**
     * Constructor from a faces' external context.
     *
     * @param context a faces' external context.
     * @throws ConfigurationException if application is not properly configured.
     */
    protected Configuration(final ExternalContext context)
            throws ConfigurationException {
        final InputStream is = context
                .getResourceAsStream(WEB_XML);
        loadMappings(is);
    }

    /**
     * Obtains the default faces' mapping.
     *
     * @return a faces' mapping.
     */
    public String getDefaultMapping() {
        return defaultMapping;
    }

    /**
     * Obtains the collection of faces' servlet mappings.
     *
     * @return the faces' mappings.
     */
    public Collection<String> getFacesMappings() {
        return Collections.unmodifiableList(facesMappings);
    }

    /**
     * Parses the <tt>web.xml</tt> file from a stream.
     *
     * @param stream the stream which holds the <tt>web.xml</tt> file.
     * @throws ConfigurationException if application is not properly configured.
     */
    private void loadMappings(final InputStream stream)
    throws ConfigurationException {
        Document webXml;
        try {
            webXml = XmlHelper.loadDocument(stream);
        } catch (final Exception e) {
            throw new ConfigurationException(e);
        }

        loadMappings(webXml);
    }

    /**
     * Parses the <tt>web.xml</tt> file.
     *
     * @param webXml the <tt>web.xml</tt> document.
     * @throws ConfigurationException if application is not properly configured
     */
    private void loadMappings(final Document webXml)
    throws ConfigurationException {
        final List<Element> servletList = XmlHelper.getChildElements(webXml
                .getDocumentElement(), TAG_SERVLET);
        for (final Element servlet : servletList) {
            final String servletName = XmlHelper.getChildText(servlet,
                    TAG_SERVLET_NAME);
            final String servletClass = XmlHelper.getChildText(servlet,
                    TAG_SERVLET_CLASS);
            if (!FACES_SERVLET_CLASS.equals(servletClass)) {
                continue;
            }

            if (logger.isLoggable(Level.CONFIG)) {
                logger.log(Level.CONFIG, "JRJSF_0024", servletName);
            }

            final List<Element> servletMappingList = XmlHelper.getChildElements(
                    webXml.getDocumentElement(),
                    TAG_SERVLET_MAPPING);
            for (final Element servletMapping : servletMappingList) {
                final String mappingName = XmlHelper.getChildText(
                        servletMapping, TAG_SERVLET_NAME);
                if (!servletName.equals(mappingName)) {
                    continue;
                }

                final String urlPattern = XmlHelper.getChildText(
                        servletMapping, TAG_URL_PATTERN);
                if ("/*".equals(urlPattern)) {
                    throw new IllegalFacesMappingException(urlPattern);
                }

                final String mapping = stripMapping(urlPattern);
                if (defaultMapping == null) {
                    defaultMapping = mapping;
                    if (logger.isLoggable(Level.CONFIG)) {
                        logger.log(Level.CONFIG, "JRJSF_0026", new Object[]{
                            servletName, mapping
                        });
                    }
                } else if (logger.isLoggable(Level.CONFIG)) {
                    logger.log(Level.CONFIG, "JRJSF_0025", new Object[]{
                        servletName, mapping
                    });
                }
                facesMappings.add(mapping);
            }
        }

        if (defaultMapping == null) {
            throw new FacesServletNotFoundException();
        }
    }

    /**
     * Extract's the base URI mapping of the specified pattern.
     *
     * @param urlPattern the faces' servlet url pattern.
     * @return the faces' servlet uri mapping.
     */
    private String stripMapping(final String urlPattern) {
        String mapping;
        if (Util.isPrefixMapped(urlPattern)) {
            mapping = urlPattern.substring(0, urlPattern.lastIndexOf("/"));
        } else {
            mapping = urlPattern.substring(urlPattern.indexOf("."));
        }
        return mapping;
    }

}
