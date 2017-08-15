package com.targetlabs.rest.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 13.08.2017.
 */
@Repository
public class RestDAOImpl implements RestDAO {

    @PersistenceContext
    private EntityManager entityManager;
}
