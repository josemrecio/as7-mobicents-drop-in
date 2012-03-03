package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.NotMetaData;
import org.jboss.metadata.sip.spec.Attribute;

/**
 * @author josemrecio@gmail.com
 *
 */
public class NotMetaDataParser extends MetaDataElementParser {

    public static NotMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        NotMetaData notMetaData = new NotMetaData();

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

        notMetaData.setCondition(ConditionMetaDataParser.parse(reader));
        return notMetaData;

    }

}
