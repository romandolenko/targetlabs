package com.targetlabs.rest.dao;

import com.targetlabs.rest.entity.DocumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 13.08.2017.
 */
@Repository
public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {

    @Transactional
    DocumentEntity saveDocument(DocumentEntity entity);

    @Transactional
    DocumentEntity findByID(String id);
}
