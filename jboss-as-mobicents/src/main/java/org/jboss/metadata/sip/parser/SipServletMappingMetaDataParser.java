package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.SipServletMappingMetaData;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;

public class SipServletMappingMetaDataParser extends MetaDataElementParser {

    public static SipServletMappingMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        SipServletMappingMetaData servletMapping = new SipServletMappingMetaData();

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
                case SERVLET_NAME:
                    servletMapping.setServletName(getElementText(reader));
                    break;
                case PATTERN:
                    //throw unexpectedElement(reader);
                    servletMapping.setPattern(SipServletMappingPatternMetaDataParser.parse(reader));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return servletMapping;
    }

}
