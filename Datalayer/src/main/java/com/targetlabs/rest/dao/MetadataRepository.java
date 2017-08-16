package com.targetlabs.rest.dao;

import com.targetlabs.rest.entity.MetadataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 16.08.2017.
 */
@Repository
public interface MetadataRepository extends CrudRepository<MetadataEntity, Long> {

    @Transactional
    List<MetadataEntity> findAllMetadataByUser(String user);

    @Transactional
    List<MetadataEntity> findAllMetadataByDate(Date date);

    @Transactional
    List<MetadataEntity> findAllMetadataByDocType(String docType);
}
