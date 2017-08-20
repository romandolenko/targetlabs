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

    @Override
    public MetadataDocument saveDocument(MultipartFile file, String userName, String localization, Date date) throws IOException {
        String id = getFileSystemDocumentDAO().saveDocument(new Document(file.getOriginalFilename(), file.getBytes()));
        MetadataDocument metadataDocument = new MetadataDocument(userName, file.getOriginalFilename(), localization, date);
        metadataDocument.setId(id);
        getFileSystemDocumentDAO().saveMetadataDocument(metadataDocument);
        return metadataDocument;
    }

    @Override
    public List<MetadataDocument> findMetadataDocuments(String userName, String localization) throws IOException, ParseException {
        return getFileSystemDocumentDAO().findMetadataDocuments(userName, localization);
    }

    @Override
    public List<MetadataDocument> findAllMetadataDocumentsByPeriod(Long delay) throws IOException, ParseException {
        List<MetadataDocument> metadataList = getFileSystemDocumentDAO().findAllMetadataDocuments();
        return metadataList.stream().filter(item -> (System.currentTimeMillis() - item.getDate().getTime()) < delay).collect(Collectors.toList());
    }

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
