/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
package com.targetlabs.rest.facade;

import com.targetlabs.rest.protocol.MetadataDocument;
import com.targetlabs.rest.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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
     *
     * @param file    uploaded file
     * @param user    uploading user
     * @param docType type of document
     * @param date    date of document
     * @return The meta data of uploaded document
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    MetadataDocument handleFileUpload(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "user", required = true) String user,
            @RequestParam(value = "docType", required = true) String docType,
            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        try {
            MetadataDocument metadataDocument = new MetadataDocument(user, docType, date);
            return getRestService().saveDocument(file, metadataDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/metadata/{id}", method = RequestMethod.GET)
    @ResponseBody
    public MetadataDocument getMetadataDocumentById(@PathVariable String id) {
        return getRestService().getMetadataDocumentById(id);
    }

    public RestService getRestService() {
        return restService;
    }
}
