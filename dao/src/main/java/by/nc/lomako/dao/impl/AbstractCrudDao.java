/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.CrudDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * @author Lomako
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCrudDao<T extends Serializable, ID extends Serializable>
        implements CrudDao<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class entityType;

    protected AbstractCrudDao(Class entityType) {
        this.entityType = entityType;
    }

    @Override
    public <S extends T> S save(S entity) {
        return em.merge(entity);
    }

    @Override
    public T findOne(ID primaryKey) {
        return (T) em.find(entityType, primaryKey);
    }

    @Override
    public Iterable<T> findAll() {
        return em.createQuery("from " + entityType.getName()).getResultList();
    }

    @Override
    public Long count() {
        return (Long) em
                .createQuery("select count(1) from " + entityType.getName())
                .getSingleResult();
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    public boolean exists(ID primaryKey) {
        return em
                .createQuery("select 1 from " + entityType.getName() + " t where t.id = :id")
                .setParameter("id", primaryKey)
                .getResultList().size() > 0;
    }

    @Override
    public Iterable<T> findPage(int start, int limit) {
        return em
                .createQuery("from " + entityType.getName())
                .setFirstResult(start)
                .setMaxResults(start + limit)
                .getResultList();
    }

}
