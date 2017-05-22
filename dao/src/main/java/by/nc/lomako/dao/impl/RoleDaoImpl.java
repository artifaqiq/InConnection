/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.RoleDao;
import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class RoleDaoImpl extends AbstractCrudDao<Role, Long> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Role findByName(RoleType role) {
        return (Role) em
                .createQuery("from Role role where role.roleType = :role")
                .setParameter("role", role)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
