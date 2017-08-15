package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public class RestServiceImpl implements RestService {

    public MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument) {

        return null;
    }

    public List<MetadataDocument> findDocuments(String user, String docType, Date date) {
        return null;
    }

    public String getDocumentFile(String id) {
        return null;
    }
}
