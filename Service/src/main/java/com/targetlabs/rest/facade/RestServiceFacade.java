package com.targetlabs.rest.facade;

import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * RESTFul API spring-boot application that provides the following APIs:
 * API to upload a file with a few meta-data fields. Persist meta-data in persistence store (In memory DB or file system and store the content on a file system)
 * API to get file meta-data
 * API to download content stream (Optional)
 * API to search for file IDs with a search criterion (Optional)
 *
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface RestServiceFacade {

    /**
     * API to upload a file with a few meta-data fields.
     * <p>
     * Url: /service/upload?file={file}&userName={userName}&localization={localization} [POST]
     *
     * @param file         uploaded file
     * @param userName     uploading userName
     * @param localization type of document
     * @return The meta data of uploaded document
     */
    ResponseEntity<?> handleFileUpload(MultipartFile file, String userName, String localization);

    /**
     * API to get file meta-data.
     * Return all document meta data if all parameters not defined.
     * <p>
     * Url: /service/documents?userName={userName}&localization={localization} [GET]
     *
     * @param userName     uploading userName
     * @param localization type of document
     * @return Collection of document meta data
     */
    ResponseEntity<List<MetadataDocument>> findMetadataDocuments(String userName, String localization);

    /**
     * Search for file IDs with a search criterion (userName, localization)
     * <p>
     * Url: /service/documents?userName={userName}&localization={localization} [GET]
     *
     * @param userName     uploading userName
     * @param localization type of document
     * @return Collection of document meta data
     */
    ResponseEntity<List<String>> findDocumentIds(String userName, String localization);

    /**
     * API to download content stream.
     * <p>
     * Url: /service/documents/{id} [GET]
     *
     * @param id The ID of a document
     * @return The document file
     */
    ResponseEntity<?> getDocument(String id);

}
