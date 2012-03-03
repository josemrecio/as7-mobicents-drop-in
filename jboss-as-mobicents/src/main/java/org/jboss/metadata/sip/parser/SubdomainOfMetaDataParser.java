package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.SubdomainOfMetaData;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;

/**
 * @author josemrecio@gmail.com
 *
 */
public class SubdomainOfMetaDataParser extends MetaDataElementParser {

    public static SubdomainOfMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        SubdomainOfMetaData subdomainOfMetaData = new SubdomainOfMetaData();

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
                    if (subdomainOfMetaData.getVar() != null) {
                        throw unexpectedElement(reader);
                    }
                    subdomainOfMetaData.setFromVarMetaData(VarMetaDataParser.parse(reader));
                    break;
                case VALUE:
                    if (subdomainOfMetaData.getValue() != null) {
                        throw unexpectedElement(reader);
                    }
                    subdomainOfMetaData.setValue(reader.getElementText());
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return subdomainOfMetaData;
    }

}
