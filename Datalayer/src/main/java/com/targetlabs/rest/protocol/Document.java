package com.targetlabs.rest.protocol;

import java.io.Serializable;

/**
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
public class Document implements Serializable{

    private String documentName;

    private byte[] documentData;

    public Document(String documentName, byte[] documentData) {
        this.documentName = documentName;
        this.documentData = documentData;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }
}
