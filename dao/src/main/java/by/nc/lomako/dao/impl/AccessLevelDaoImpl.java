/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.AccessLevelDao;
import by.nc.lomako.pojos.AccessLevel;
import by.nc.lomako.pojos.AccessLevelType;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class AccessLevelDaoImpl extends AbstractCrudDao<AccessLevel, Long> implements AccessLevelDao {

    public AccessLevelDaoImpl() {
        super(AccessLevel.class);
    }

    @Override
    public AccessLevel findByName(AccessLevelType role) {
        return (AccessLevel) em
                .createQuery("from AccessLevel al where al.accessLevelType = :role")
                .setParameter("role", role)
                .getResultList().get(0);
    }
}
