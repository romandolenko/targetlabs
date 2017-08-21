package com.targetlabs.rest.service;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * A service to save and find Documents and MetadataDocuments
 *
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
public interface DocumentService {

    /**
     * Saves document.
     * @param file         uploaded file
     * @param userName     uploading userName
     * @param localization type of document
     * @param date         uploaded time
     * @return             The meta data of uploaded document
     * @throws IOException if the file does not exist,
     *                     is a directory rather than a regular file,
     *                     or for some other reason cannot be opened for
     *                     saving.
     */
    MetadataDocument saveDocument(MultipartFile file, String userName, String localization, Date date) throws IOException;

    /**
     * Finds meta data documents by parameters.
     *
     * @param userName User name
     * @param localization document localization
     * @return List of meta data documents.
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     * @throws ParseException if property file can not be parsed.
     */
    List<MetadataDocument> findMetadataDocuments(String userName, String localization) throws IOException, ParseException;

    /**
     *
     * @param ms Finds meta data documents by schedule delay in milliseconds.
     * @return List of meta data documents.
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     * @throws ParseException if property file can not be parsed.
     */
    List<MetadataDocument> findAllMetadataDocumentsByPeriod(Long ms) throws IOException, ParseException;

    /**
     * Returns the document from the file system with the given id.
     * The document file and meta data is returned.
     * Returns null if no document was found.
     *
     * @param id The id of the document
     * @return A document incl. file and meta data
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     * @throws ParseException if property file can not be parsed.
     */
    Document findDocumentById(String id) throws IOException, ParseException;
}
