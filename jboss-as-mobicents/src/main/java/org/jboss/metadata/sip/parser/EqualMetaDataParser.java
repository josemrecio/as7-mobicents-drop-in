package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.EqualMetaData;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;
import org.jboss.metadata.sip.spec.VarMetaData;

/**
 * @author josemrecio@gmail.com
 *
 */
public class EqualMetaDataParser extends MetaDataElementParser {

    public static EqualMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        EqualMetaData equalMetaData = new EqualMetaData();

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
                    if (equalMetaData.getVar() != null) {
                        throw unexpectedElement(reader);
                    }
                    VarMetaData var = VarMetaDataParser.parse(reader);
                    equalMetaData.setVar(var.getVar());
                    equalMetaData.setIgnoreCase(var.isIgnoreCase());
                    break;
                case VALUE:
                    if (equalMetaData.getValue() != null) {
                        throw unexpectedElement(reader);
                    }
                    equalMetaData.setValue(reader.getElementText());
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return equalMetaData;
    }

}
