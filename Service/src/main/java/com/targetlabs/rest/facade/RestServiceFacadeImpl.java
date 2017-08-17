/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
package com.targetlabs.rest.facade;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<MetadataDocument> handleFileUpload(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "localization", required = true) String localization,
            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        try {
            MetadataDocument metadataDocument = new MetadataDocument(userName, file.getOriginalFilename(), localization, date);
            return new ResponseEntity<>(getRestService().saveDocument(file, metadataDocument), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get collection of document metadata by search criteria (user, localization, date)
     * <p>
     * Url: /service/documents?user={user}&localization={localization}&date={date} [GET]
     *
     * @param user         uploading user
     * @param localization type of document
     * @param date         date of document
     * @return Collection of document meta data
     */
    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public ResponseEntity<List<MetadataDocument>> findMetadataDocuments(@RequestParam(value = "user", required = false) String user,
                                                                        @RequestParam(value = "localization", required = false) String localization,
                                                                        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            List<MetadataDocument> metadataDocumentList = getRestService().findMetadataDocuments(user, localization, date);
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
    public ResponseEntity<Document> getDocument(@PathVariable String id) {
        try {
            Document document = getRestService().findDocumentById(id);
            return new ResponseEntity<Document>(document, new HttpHeaders(), HttpStatus.OK);
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public RestService getRestService() {
        return restService;
    }
}
