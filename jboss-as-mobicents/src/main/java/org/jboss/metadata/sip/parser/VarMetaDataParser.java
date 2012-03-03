package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.VarMetaData;
import org.jboss.metadata.sip.spec.Attribute;

/**
 * @author josemrecio@gmail.com
 *
 */
public class VarMetaDataParser extends MetaDataElementParser {

    public static VarMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        VarMetaData varMetaData = new VarMetaData();

        // Handle attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String value = reader.getAttributeValue(i);
            if (attributeHasNamespace(reader, i)) {
                continue;
            }
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case IGNORE_CASE:
                    varMetaData.setIgnoreCase(Boolean.parseBoolean(value));
                    break;
                case ID:
                    varMetaData.setId(value);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        varMetaData.setVar(reader.getElementText());

        return varMetaData;
    }

}
