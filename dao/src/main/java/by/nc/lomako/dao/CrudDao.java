/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import java.io.Serializable;

/**
 * @author Lomako
 * @version 1.0
 */
public interface CrudDao<T extends Serializable, ID extends Serializable> {

    <S extends T> S save(S entity);

    T findOne(ID primaryKey);

    Iterable<T> findAll();

    Long count();

    void delete(T entity);

    boolean exists(ID primaryKey);

    Iterable<T> findPage(int start, int limit);
}