package com.targetlabs.rest.facade;

import com.targetlabs.rest.error.RestValidationError;
import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
@Validated
public class DocumentServiceController {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceController.class);


    @Autowired
    private DocumentService documentService;

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
            @Size(max = 100) @RequestParam(value = "userName", required = true) String userName,
            @Size(max = 100) @RequestParam(value = "localization", required = true) String localization) {

        try {
            Date uploadDate = new Date(System.currentTimeMillis());
            MetadataDocument metadataDocument = getDocumentService().saveDocument(file, userName, localization, uploadDate);
            return new ResponseEntity<>(metadataDocument, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException e) {
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
    public ResponseEntity<?> findMetadataDocuments(@Size(max = 100) @RequestParam(value = "userName", required = false) String userName,
                                                   @Size(max = 100) @RequestParam(value = "localization", required = false) String localization) {
        try {
            List<MetadataDocument> metadataDocumentList = getDocumentService().findMetadataDocuments(userName, localization);
            if (metadataDocumentList == null || metadataDocumentList.isEmpty()) {
                return new ResponseEntity<>("Meta data are not found!", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(metadataDocumentList, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<?> findDocumentIds(@Size(max = 100) @RequestParam(value = "userName", required = false) String userName,
                                             @Size(max = 100) @RequestParam(value = "localization", required = false) String localization) {
        try {
            List<MetadataDocument> metadataDocumentList = getDocumentService().findMetadataDocuments(userName, localization);
            List<String> documentIds = metadataDocumentList.stream().map(MetadataDocument::getId).collect(Collectors.toList());
            if (documentIds == null || documentIds.isEmpty()) {
                return new ResponseEntity<>("Documents identifications is not found!", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(documentIds, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    @ResponseBody
    public ResponseEntity<?> getDocument(@Size(min = 36, max = 36) @PathVariable String id) {
        try {
            Document document = getDocumentService().findDocumentById(id);
            if (document == null) {
                return new ResponseEntity<>("Document is not found!", HttpStatus.NOT_FOUND);
            }
            HttpHeaders headers = buildHeader(document.getDocumentName());
            return new ResponseEntity<>(document.getDocumentData(), headers, HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestValidationError> processValidationError(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        RestValidationError restValidationError = new RestValidationError();
        constraintViolations.forEach(constraintViolation ->
                restValidationError.addFieldError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));

        return new ResponseEntity<RestValidationError>(restValidationError, HttpStatus.BAD_REQUEST);
    }


    public DocumentService getDocumentService() {
        return documentService;
    }

    private HttpHeaders buildHeader(String documentName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData(documentName, documentName);
        return headers;
    }
}
