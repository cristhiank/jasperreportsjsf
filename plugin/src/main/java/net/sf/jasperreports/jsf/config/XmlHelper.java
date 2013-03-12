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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility method to parse the XML using DOM.
 *
 * @author A. Alonso Dominguez
 */
final class XmlHelper {

    /**
     * Obtains the first child element with the specified name.
     *
     * @param element root element.
     * @param tagName the child tag name.
     * @return the child element or null if not found.
     */
    public static Element getChildElement(final Element element,
            final String tagName) {
        return getChildElement(element, null, tagName);
    }

    /**
     * Obtains the first child element with the specified name inside
     * the specified namespace.
     *
     * @param element the root element.
     * @param namespace the namespace of the child element.
     * @param tagName the child local name.
     * @return the child element.
     */
    public static Element getChildElement(final Element element,
            final String namespace, final String tagName) {
        final NodeList childNodes = element.getChildNodes();
        final int numChildren = childNodes.getLength();

        for (int i = 0; i < numChildren; i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final Element childElement = (Element) childNode;
            String childTagName = childElement.getTagName();
            final String childPrefix = childElement.getPrefix();
            final String childNamespace = (childPrefix != null ? childElement.
                    lookupNamespaceURI(childPrefix) : null);

            if (namespace != null) {
                if (!namespace.equals(childNamespace)) {
                    continue;
                } else {
                    childTagName = childElement.getLocalName();
                }
            }

            if (!childTagName.equals(tagName)) {
                continue;
            }
            return childElement;
        }
        return null;
    }

    /**
     * Obtains the list of child elements with the specified tag name.
     *
     * @param element the root element.
     * @param tagName the child tag element name.
     * @return the child element list.
     */
    public static List<Element> getChildElements(final Element element,
            final String tagName) {
        return getChildElements(element, null, tagName);
    }

    /**
     * Obtains the list of child elements with the specified tag name
     * inside the specific namespace.
     *
     * @param element the root element.
     * @param namespace the child namespace name.
     * @param tagName the child tag name.
     * @return the child element list.
     */
    public static List<Element> getChildElements(final Element element,
            final String namespace, final String tagName) {
        final List<Element> elements = getElementList(element);
        final int numElements = elements.size();
        final List<Element> childElements = new ArrayList<Element>();
        for (int i = 0; i < numElements; i++) {
            final Element childElement = elements.get(i);
            String childTagName = childElement.getTagName();
            final String childPrefix = childElement.getPrefix();
            final String childNamespace = (childPrefix != null 
                    ? childElement.lookupNamespaceURI(childPrefix)
                    : null);

            if (namespace != null) {
                if (!namespace.equals(childNamespace)) {
                    continue;
                } else {
                    childTagName = childElement.getLocalName();
                }
            }

            if (!childTagName.equals(tagName)) {
                continue;
            }
            childElements.add(childElement);
        }
        return childElements;
    }

    /**
     * Obtains the text value inside the first child element with the
     * specified tag name.
     *
     * @param element the root element.
     * @param tagName the child element tag name.
     * @return the text inside the child element.
     */
    public static String getChildText(final Element element,
            final String tagName) {
        final Element child = getChildElement(element, tagName);
        return getText(child);
    }

    /**
     * Obtains the text of the specified element.
     *
     * @param element the text element.
     * @return the text value inside the element.
     */
    public static String getText(final Element element) {
        final Node node = element.getFirstChild();
        if (node != null && node.getNodeType() == Node.TEXT_NODE) {
            return node.getNodeValue().trim();
        }
        return null;
    }

    /**
     * Obtains the list of child element nodes.
     *
     * @param element the root element.
     * @return the list of child elements.
     */
    private static List<Element> getElementList(final Element element) {
        final List<Element> elementList = new ArrayList<Element>();

        final NodeList childNodes = element.getChildNodes();
        final int numChildren = childNodes.getLength();

        for (int i = 0; i < numChildren; i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            elementList.add((Element) childNode);
        }

        return elementList;
    }

    /**
     * Parses the XML contents specified stream.
     *
     * @param stream the XML stream.
     * @return a DOM instance.
     * @throws ParserConfigurationException if XML parser is not
     *         properly configured.
     * @throws SAXException if XML is not well formed.
     * @throws IOException if any error happens processing the stream.
     */
    public static Document loadDocument(final InputStream stream)
            throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(stream);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private XmlHelper() { }
}
