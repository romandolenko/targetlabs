package com.targetlabs.rest.entity;

import javax.persistence.*;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 15.08.2017.
 */
@Entity
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String fileData;

    @OneToOne
    private MetadataEntity metadataEntity;

    public DocumentEntity(String fileName, String fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public MetadataEntity getMetadataEntity() {
        return metadataEntity;
    }

    public void setMetadataEntity(MetadataEntity metadataEntity) {
        this.metadataEntity = metadataEntity;
    }
}
