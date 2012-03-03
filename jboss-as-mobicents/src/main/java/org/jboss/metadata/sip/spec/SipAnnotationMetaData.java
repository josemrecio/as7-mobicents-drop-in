package org.jboss.metadata.sip.spec;

import java.util.HashMap;

import org.jboss.as.server.deployment.AttachmentKey;

public class SipAnnotationMetaData extends HashMap<String, SipMetaData> {
    private static final long serialVersionUID = 1L;

    public static final AttachmentKey<SipAnnotationMetaData> ATTACHMENT_KEY = AttachmentKey.create(SipAnnotationMetaData.class);

}
