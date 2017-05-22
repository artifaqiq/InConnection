/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
public interface CrudDao<T extends Serializable, ID extends Serializable> {

    <S extends T> S save(S entity);

    T findOne(ID primaryKey);

    List<T> findAll();

    Long count();

    void delete(T entity);

    void deleteById(ID id);

    boolean exist(ID primaryKey);

    List<T> findPage(int start, int limit);
}