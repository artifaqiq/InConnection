/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.pojos.AccessLevel;
import by.nc.lomako.pojos.AccessLevelType;
import by.nc.lomako.pojos.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.test.xml")
@Transactional
public class AccessLevelDaoIT {

    @Autowired
    private AccessLevelDao accessLevelDao;

    @Autowired
    private UserDao userDao;

    private AccessLevel randomAccessLevel;

    @Before
    public void initTestPojos() {
        randomAccessLevel = new AccessLevel();
        randomAccessLevel.setAccessLevelType(AccessLevelType.ADMIN);
    }

    @Test
    public void allRolesHasInTable() {
        List<AccessLevelType> types = Arrays.asList(AccessLevelType.values());

        for (AccessLevelType type : types) {
            assertEquals(
                    type,
                    accessLevelDao.findByName(type).getAccessLevelType()
            );
        }
    }

    @Test
    public void addRoleToUser() throws InterruptedException {
        User user = userDao.findAll().iterator().next();
        AccessLevel role = accessLevelDao.findByName(AccessLevelType.MODERATOR);
    }

    @Test
    public void findById() {
        assertNull(accessLevelDao.findOne(12314142L));
    }

    @Test
    public void delete() {
        accessLevelDao.delete(randomAccessLevel);
    }

    @Test
    public void findAll() {
        assertNotNull(accessLevelDao.findAll());
    }

    @Test
    public void count() {
        assertEquals(new Long(3), accessLevelDao.count());
    }

    @Test
    public void exists() {
        assertFalse(accessLevelDao.exists(1231414L));
    }

}
