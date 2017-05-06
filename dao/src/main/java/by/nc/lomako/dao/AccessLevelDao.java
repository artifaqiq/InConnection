/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.AccessLevel;
import by.nc.lomako.pojos.AccessLevelType;

/**
 * @author Lomako
 * @version 1.0
 */
public interface AccessLevelDao extends CrudDao<AccessLevel, Long> {
    AccessLevel findByName(AccessLevelType role);
}
