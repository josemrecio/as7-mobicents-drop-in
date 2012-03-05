/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.ConditionMetaData;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;

/**
 * @author josemrecio@gmail.com
 *
 */
public class ConditionMetaDataParser extends MetaDataElementParser {

    public static ConditionMetaData parse(XMLStreamReader reader) throws XMLStreamException {

        ConditionMetaData conditionMetaData = null;

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case AND:
                    conditionMetaData = AndMetaDataParser.parse(reader);
                    break;
                case OR:
                    conditionMetaData = OrMetaDataParser.parse(reader);
                    break;
                case NOT:
                    // TODO: josemrecio
                    throw unexpectedElement(reader);
                    // break;
                case EQUAL:
                    conditionMetaData = EqualMetaDataParser.parse(reader);
                    break;
                case CONTAINS:
                    // TODO: josemrecio
                    throw unexpectedElement(reader);
                    // break;
                case EXISTS:
                    // TODO: josemrecio
                    throw unexpectedElement(reader);
                    // break;
                case SUBDOMAIN_OF:
                    // TODO: josemrecio
                    throw unexpectedElement(reader);
                    // break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return conditionMetaData;
    }

}
