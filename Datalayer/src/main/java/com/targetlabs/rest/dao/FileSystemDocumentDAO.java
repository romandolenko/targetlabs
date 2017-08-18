package com.targetlabs.rest.dao;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * FileSystemDocumentDAO saves documents and meta data in the file system.
 *
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
public interface FileSystemDocumentDAO {

    /**
     * Save a document in the file system.
     *
     * @param document Document
     * @return The id of the document
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   saving.
     */
    String saveDocument(Document document) throws IOException;

    /**
     * Save a metadataDocument in the file system.
     *
     * @param metadataDocument MetadataDocument
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   saving.
     */
    void saveMetadataDocument(MetadataDocument metadataDocument) throws IOException;

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
     * Finds meta data documents.
     *
     * @return List of meta data documents.
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     * @throws ParseException if property file can not be parsed.
     */
    List<MetadataDocument> findAllMetadataDocuments() throws IOException, ParseException;

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
