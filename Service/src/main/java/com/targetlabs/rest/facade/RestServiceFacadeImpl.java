/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
package com.targetlabs.rest.facade;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

/**
 *
 */
@RestController
@RequestMapping("/service")
public class RestServiceFacadeImpl {

    private static final Logger log = LoggerFactory.getLogger(RestServiceFacadeImpl.class);


    @Autowired
    private RestService restService;

    /**
     * Upload a file with a few meta-data fields.
     * <p>
     * Url: /service/upload?file={file}&userName={userName}&localization={localization}&date={date} [POST]
     *
     * @param file         uploaded file
     * @param userName     uploading userName
     * @param localization type of document
     * @param date         date of document
     * @return The meta data of uploaded document
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileUpload(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "localization", required = true) String localization,
            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        try {
            System.out.println(String.format("fileName %s | userName %s | Localization %s | Date %s", file.getOriginalFilename(), userName, localization, date.toString()));
            MetadataDocument metadataDocument = getRestService().saveDocument(file, userName, localization, date);
            return new ResponseEntity<>(metadataDocument, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get collection of document metadata by search criteria (userName, localization, date)
     * <p>
     * Url: /service/documents?userName={userName}&localization={localization}&date={date} [GET]
     *
     * @param userName     uploading userName
     * @param localization type of document
     * @param date         date of document
     * @return Collection of document meta data
     */
    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    public ResponseEntity<List<MetadataDocument>> findMetadataDocuments(@RequestParam(value = "userName", required = false) String userName,
                                                                        @RequestParam(value = "localization", required = false) String localization,
                                                                        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<MetadataDocument> metadataDocumentList = getRestService().findMetadataDocuments(userName, localization, date);
            return new ResponseEntity<>(metadataDocumentList, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns the document by ID.
     * <p>
     * Url: /service/documents/{id} [GET]
     *
     * @param id The ID of a document
     * @return The document file
     */
    @RequestMapping(value = "/documents/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDocument(@PathVariable String id) {
        try {
            Document document = getRestService().findDocumentById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String fileName = document.getDocumentName();
            headers.setContentDispositionFormData(fileName, fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<byte[]>(document.getDocumentData(), headers, HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public RestService getRestService() {
        return restService;
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
