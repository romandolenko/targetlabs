package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface RestService {

    MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument) throws IOException;

    List<MetadataDocument> findMetadataDocuments(String user, String localization, Date date);

    byte[] findDocumentById(String id);
}
