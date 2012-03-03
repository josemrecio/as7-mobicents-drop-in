package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.javaee.spec.EmptyMetaData;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.Attribute;

/**
 * @author josemrecio@gmail.com
 *
 */
public class EmptyMetaDataParser extends MetaDataElementParser {

    public static EmptyMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        EmptyMetaData emptyMetaData = new EmptyMetaData();

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case ID:
                    emptyMetaData.setId(value);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        return emptyMetaData;
    }

}
