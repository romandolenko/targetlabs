package com.targetlabs.rest.dao;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;

import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
public interface FileSystemDocumentDAO {

    String saveDocument(Document data);

    void saveMetadataDocument(MetadataDocument metadataDocument);

    List<MetadataDocument> findDocuments(String user, String localization, Date date);

    byte[] findDocumentById(String id);



}
