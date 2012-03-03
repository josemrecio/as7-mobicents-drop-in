package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.Attribute;
import org.jboss.metadata.sip.spec.ProxyConfigMetaData;
import org.jboss.metadata.sip.spec.Element;

/**
 * @author josemrecio@gmail.com
 *
 */
public class ProxyConfigMetaDataParser extends MetaDataElementParser {

    public static ProxyConfigMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        ProxyConfigMetaData proxyConfigMetaData = new ProxyConfigMetaData();

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
                    proxyConfigMetaData.setId(value);
                    break;
                default:
                    throw unexpectedAttribute(reader, i);
            }
        }

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case PROXY_TIMEOUT:
                    proxyConfigMetaData.setProxyTimeout(Integer.parseInt(reader.getElementText()));
                    break;
                default:
                    throw unexpectedElement(reader);
            }
        }

        return proxyConfigMetaData;
    }

}
