package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.protocol.MetadataProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface RestService {

    MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument);

    List<MetadataDocument> findDocuments(String user, String docType, Date date);

    String getDocumentFile(String id);
}
