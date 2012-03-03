package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.Element;
import org.jboss.metadata.sip.spec.PatternMetaData;

/**
 * @author josemrecio@gmail.com
 *
 */
public class SipServletMappingPatternMetaDataParser extends MetaDataElementParser {

    public static PatternMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        PatternMetaData patternMetaData = new PatternMetaData();

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
                    patternMetaData.setId(reader.getAttributeValue(i));
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case AND:
                    patternMetaData.setCondition(AndMetaDataParser.parse(reader));
                    break;
                case OR:
                    patternMetaData.setCondition(OrMetaDataParser.parse(reader));
                    break;
                case NOT:
                    patternMetaData.setCondition(NotMetaDataParser.parse(reader));
                    break;
                case EQUAL:
                    patternMetaData.setCondition(EqualMetaDataParser.parse(reader));
                    break;
                case CONTAINS:
                    patternMetaData.setCondition(ContainsMetaDataParser.parse(reader));
                    break;
                case EXISTS:
                    patternMetaData.setCondition(ExistsMetaDataParser.parse(reader));
                    break;
                case SUBDOMAIN_OF:
                    patternMetaData.setCondition(SubdomainOfMetaDataParser.parse(reader));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return patternMetaData;
    }

}
