package com.targetlabs.rest.service;

import com.targetlabs.rest.dao.DocumentRepository;
import com.targetlabs.rest.dao.MetadataRepository;
import com.targetlabs.rest.entity.DocumentEntity;
import com.targetlabs.rest.protocol.MetadataDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 12.08.2017.
 */
@Service("restService")
public class RestServiceImpl implements RestService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public MetadataDocument saveDocument(MultipartFile file, MetadataDocument metadataDocument) throws IOException {
        DocumentEntity documentEntity = new DocumentEntity(file.getName(), DatatypeConverter.printBase64Binary(file.getBytes()));
        documentRepository.saveDocument(documentEntity);
        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<MetadataDocument> findDocuments(String user, String docType, Date date) {
        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public String getDocumentFile(String id) {
        DocumentEntity entity = documentRepository.findByID(id);
        return null;
    }
}
