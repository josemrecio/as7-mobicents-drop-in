package org.jboss.metadata.sip.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.sip.spec.ExistsMetaData;
import org.jboss.metadata.sip.spec.VarMetaData;

/**
 * @author josemrecio@gmail.com
 *
 */
public class ExistsMetaDataParser extends MetaDataElementParser {

    public static ExistsMetaData parse(XMLStreamReader reader) throws XMLStreamException {
        ExistsMetaData existsMetaData = new ExistsMetaData();
        VarMetaData varMetaData = VarMetaDataParser.parse(reader);
        existsMetaData.setFromVarMetaData(varMetaData);
        return existsMetaData;
    }

}