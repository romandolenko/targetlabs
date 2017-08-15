package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.protocol.MetadataProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface RestService {

    MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument);

    MetadataDocument getMetadataDocumentById(String id);
}
