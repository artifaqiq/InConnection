/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.AvatarDao;
import by.nc.lomako.pojos.Avatar;
import org.springframework.stereotype.Repository;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class AvatarDaoImpl extends AbstractCrudDao<Avatar, Long> implements AvatarDao {

    public AvatarDaoImpl() {
        super(Avatar.class);
    }
}
