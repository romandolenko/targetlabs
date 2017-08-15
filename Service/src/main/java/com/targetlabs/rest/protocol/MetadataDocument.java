package com.targetlabs.rest.protocol;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
public class MetadataDocument implements Serializable{

    private String id;

    private String user;

    private String docType;

    private Date date;

    public MetadataDocument(String user, String docType, Date date) {
        this.user = user;
        this.docType = docType;
        this.date = date;
    }

    public MetadataDocument(String id, String user, String docType, Date date) {
        this.id = id;
        this.user = user;
        this.docType = docType;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
