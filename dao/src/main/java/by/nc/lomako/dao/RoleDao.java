/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;

/**
 * @author Lomako
 * @version 1.0
 */
public interface RoleDao extends CrudDao<Role, Long> {
    Role findByName(RoleType role);
}
