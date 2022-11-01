package com.metrostate.edu.decentrovote.services;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class AbstractDataAccessService<T> {
    @PersistenceContext
    protected EntityManager entityManager;

    protected AbstractDataAccessService() {}

    public T findByNaturalId(Object naturalId) {
        Session session = entityManager.unwrap(Session.class);
        return session.bySimpleNaturalId(getEntityClass())
                .load(naturalId);
    }

    protected abstract Class<T> getEntityClass();
}
