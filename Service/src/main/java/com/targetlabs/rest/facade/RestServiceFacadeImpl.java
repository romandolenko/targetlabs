package com.targetlabs.rest.facade;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RESTFul API spring-boot application that provides the following APIs:
 * API to upload a file with a few meta-data fields. Persist meta-data in persistence store (In memory DB or file system and store the content on a file system)
 * API to get file meta-data
 * API to download content stream (Optional)
 * API to search for file IDs with a search criterion (Optional)
 *
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
@RestController
@RequestMapping("/service")
public class RestServiceFacadeImpl implements RestServiceFacade {

    private static final Logger log = LoggerFactory.getLogger(RestServiceFacadeImpl.class);


    @Autowired
    private RestService restService;

    /**
     * Upload a file with a few meta-data fields.
     * <p>
     * Url: /service/upload?file={file}&userName={userName}&localization={localization} [POST]
     *
     * @param file         uploaded file
     * @param userName     uploading userName
     * @param localization type of document
     * @return The meta data of uploaded document
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileUpload(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "localization", required = true) String localization) {

        try {
            Date uploadDate = new Date(System.currentTimeMillis());
            System.out.println(String.format("fileName %s | userName %s | Localization %s | Date %s", file.getOriginalFilename(), userName, localization, uploadDate));
            MetadataDocument metadataDocument = getRestService().saveDocument(file, userName, localization, uploadDate);
            return new ResponseEntity<>(metadataDocument, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get collection of document metadata by search criteria (userName, localization)
     * <p>
     * Url: /service/metadata?userName={userName}&localization={localization} [GET]
     *
     * @param userName     uploading userName
     * @param localization type of document
     * @return Collection of document meta data
     */
    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    public ResponseEntity<List<MetadataDocument>> findMetadataDocuments(@RequestParam(value = "userName", required = false) String userName,
                                                                        @RequestParam(value = "localization", required = false) String localization) {
        try {
            List<MetadataDocument> metadataDocumentList = getRestService().findMetadataDocuments(userName, localization);
            return new ResponseEntity<>(metadataDocumentList, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Search for file IDs with a search criterion (userName, localization)
     * <p>
     * Url: /service/documents?userName={userName}&localization={localization} [GET]
     *
     * @param userName     uploading userName
     * @param localization type of document
     * @return Collection of document meta data
     */
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public ResponseEntity<List<String>> findDocumentIds(@RequestParam(value = "userName", required = false) String userName,
                                                        @RequestParam(value = "localization", required = false) String localization) {
        try {
            List<MetadataDocument> metadataDocumentList = getRestService().findMetadataDocuments(userName, localization);
            List<String> documentIds = metadataDocumentList.stream().map(MetadataDocument::getId).collect(Collectors.toList());
            ;
            return new ResponseEntity<>(documentIds, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Download content stream by ID.
     * <p>
     * Url: /service/documents/{id} [GET]
     *
     * @param id The ID of a document
     * @return The document file
     */
    @RequestMapping(value = "/documents/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDocument(@PathVariable String id) {
        try {
            Document document = getRestService().findDocumentById(id);
            if (document == null) {
                return new ResponseEntity<>("Document did not found!", HttpStatus.OK);
            }
            HttpHeaders headers = buildHeader(document.getDocumentName());
            return new ResponseEntity<>(document.getDocumentData(), headers, HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public RestService getRestService() {
        return restService;
    }

    private HttpHeaders buildHeader(String documentName) {
        HttpHeaders headers = new HttpHeaders();
        String extension = getExtension(documentName);
        MediaType mediaType = getMediaType(extension);
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData(documentName, documentName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    private MediaType getMediaType(String extension) {
        switch (extension) {
            case "pdf": {
                return MediaType.APPLICATION_PDF;
            }
            case "xml": {
                return MediaType.APPLICATION_XML;
            }
            case "gif": {
                return MediaType.IMAGE_GIF;
            }
            case "jpeg": {
                return MediaType.IMAGE_JPEG;
            }
            default: {
                return MediaType.ALL;
            }
        }

    }

    private String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
