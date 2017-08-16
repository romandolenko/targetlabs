package com.targetlabs.rest.dao;

import com.targetlabs.rest.protocol.Document;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
@Service("fileSystemDocumentDAO")
public class FileSystemDocumentDAOImpl implements FileSystemDocumentDAO {

    @Override
    public String saveDocument(Document data) {
        return null;
    }

    @Override
    public void saveMetadataDocument(MetadataDocument metadataDocument) {

    }

    @Override
    public List<MetadataDocument> findDocuments(String user, String localization, Date date) {
        return null;
    }

    @Override
    public byte[] findDocumentById(String id) {
        return new byte[0];
    }
}
