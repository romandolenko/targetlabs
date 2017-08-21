package com.targetlabs.rest.service;

import com.targetlabs.rest.dao.FileSystemDocumentDAO;
import com.targetlabs.rest.facade.DocumentServiceController;
import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
@Service("restService")
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceController.class);

    @Autowired
    private FileSystemDocumentDAO fileSystemDocumentDAO;

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
    @Override
    public MetadataDocument saveDocument(MultipartFile file, String userName, String localization, Date date) throws IOException {
        String id = getFileSystemDocumentDAO().saveDocument(new Document(file.getOriginalFilename(), file.getBytes()));
        MetadataDocument metadataDocument = new MetadataDocument(userName, file.getOriginalFilename(), localization, date);
        metadataDocument.setId(id);
        getFileSystemDocumentDAO().saveMetadataDocument(metadataDocument);
        return metadataDocument;
    }

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
    @Override
    public List<MetadataDocument> findMetadataDocuments(String userName, String localization) throws IOException, ParseException {
        return getFileSystemDocumentDAO().findMetadataDocuments(userName, localization);
    }

    /**
     *
     * @param delay Finds meta data documents by schedule delay in milliseconds.
     * @return List of meta data documents.
     * @throws IOException if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
     * @throws ParseException if property file can not be parsed.
     */
    @Override
    public List<MetadataDocument> findAllMetadataDocumentsByPeriod(Long delay) throws IOException, ParseException {
        List<MetadataDocument> metadataList = getFileSystemDocumentDAO().findAllMetadataDocuments();
        return metadataList.stream().filter(item -> (System.currentTimeMillis() - item.getDate().getTime()) < delay).collect(Collectors.toList());
    }

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
    @Override
    public Document findDocumentById(String id) throws IOException, ParseException {
        return getFileSystemDocumentDAO().findDocumentById(id);
    }

    public FileSystemDocumentDAO getFileSystemDocumentDAO() {
        return fileSystemDocumentDAO;
    }

    public void setFileSystemDocumentDAO(FileSystemDocumentDAO fileSystemDocumentDAO) {
        this.fileSystemDocumentDAO = fileSystemDocumentDAO;
    }
}
