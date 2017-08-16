package com.targetlabs.rest.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
@Entity
public class MetadataEntity {

    @Id
    private Long id;

    private String user;

    private String docType;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToOne
    private DocumentEntity documentEntity;

    public MetadataEntity(String user, String docType, Date date) {
        this.user = user;
        this.docType = docType;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public DocumentEntity getDocumentEntity() {
        return documentEntity;
    }

    public void setDocumentEntity(DocumentEntity documentEntity) {
        this.documentEntity = documentEntity;
    }
}
