package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface RestService {

    MetadataDocument saveDocument(MultipartFile file, String userName, String localization, Date date) throws IOException;

    List<MetadataDocument> findMetadataDocuments(String userName, String localization, Date date) throws IOException, ParseException;

    List<MetadataDocument> findAllMetadataDocumentsByPeriod(Long ms) throws IOException, ParseException;

    Document findDocumentById(String id) throws IOException, ParseException;
}
