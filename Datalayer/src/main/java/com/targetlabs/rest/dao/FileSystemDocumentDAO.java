package com.targetlabs.rest.dao;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
public interface FileSystemDocumentDAO {

    String saveDocument(Document data) throws IOException;

    void saveMetadataDocument(MetadataDocument metadataDocument) throws IOException;

    List<MetadataDocument> findMetadataDocuments(String userName, String localization, Date date) throws IOException, ParseException;

    Document findDocumentById(String id) throws IOException, ParseException;



}
