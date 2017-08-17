package com.targetlabs.rest.protocol;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dolenko Roman <roman.dolenko@olfatrade.com> on 15.08.2017.
 */
public class MetadataDocument implements Serializable {

    private String id;

    private String userName;

    private String documentName;

    private String localization;

    private Date date;

    public MetadataDocument(String userName, String documentName, String localization, Date date) {
        this.userName = userName;
        this.documentName = documentName;
        this.date = date;
        this.localization = localization;
    }

    public MetadataDocument(String id, String userName, String documentName, String localization, Date date) {
        this.id = id;
        this.userName = userName;
        this.documentName = documentName;
        this.date = date;
        this.localization = localization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
