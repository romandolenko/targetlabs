package com.targetlabs.rest.service;

import com.targetlabs.rest.dao.FileSystemDocumentDAO;
import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
@Service("restService")
public class RestServiceImpl implements RestService {

    @Autowired
    private FileSystemDocumentDAO fileSystemDocumentDAO;

    public MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument) throws IOException {
        String id = fileSystemDocumentDAO.saveDocument(new Document(file.getOriginalFilename(), file.getBytes()));
        metadataDocument.setId(id);
        fileSystemDocumentDAO.saveMetadataDocument(metadataDocument);
        return metadataDocument;
    }

    public List<MetadataDocument> findMetadataDocuments(String user, String localization, Date date) {
        return null;
    }

    public byte[] findDocumentById(String id) {
        return fileSystemDocumentDAO.findDocumentById(id);
    }

    public FileSystemDocumentDAO getFileSystemDocumentDAO() {
        return fileSystemDocumentDAO;
    }

    public void setFileSystemDocumentDAO(FileSystemDocumentDAO fileSystemDocumentDAO) {
        this.fileSystemDocumentDAO = fileSystemDocumentDAO;
    }
}
