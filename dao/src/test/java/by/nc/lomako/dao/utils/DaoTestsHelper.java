/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.utils;

import by.nc.lomako.dao.RoleDao;
import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class DaoTestsHelper {

    @Autowired
    private RoleDao roleDao;

    public void persistRoles() {
        for (RoleType roleType : RoleType.values()) {

            if (roleDao.findByName(roleType) != null) {
                continue;
            }

            Role role = new Role();
            role.setRoleType(roleType);
            roleDao.save(role);
        }
    }

    public void dropRoles() {
        for (Role role : roleDao.findAll()) {
            roleDao.delete(role);
        }
    }
}
