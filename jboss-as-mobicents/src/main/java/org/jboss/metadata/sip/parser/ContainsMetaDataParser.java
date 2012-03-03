package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.ContainsMetaData;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;

/**
 * @author josemrecio@gmail.com
 *
 */
public class ContainsMetaDataParser extends MetaDataElementParser {

    public static ContainsMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        ContainsMetaData containsMetaData = new ContainsMetaData();

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
                case VAR:
                    if (containsMetaData.getVar() != null) {
                        throw unexpectedElement(reader);
                    }
                    containsMetaData.setFromVarMetaData(VarMetaDataParser.parse(reader));
                    break;
                case VALUE:
                    if (containsMetaData.getValue() != null) {
                        throw unexpectedElement(reader);
                    }
                    containsMetaData.setValue(reader.getElementText());
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return containsMetaData;
    }

}
