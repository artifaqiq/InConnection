/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao;

import by.nc.lomako.dao.utils.DaoTestsHelper;
import by.nc.lomako.pojos.Role;
import by.nc.lomako.pojos.RoleType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Lomako
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:dao-context.test.xml")
@ActiveProfiles("test")
@Transactional
public class RoleDaoIT {

    @Autowired
    private DaoTestsHelper daoTestsHelper;

    @Autowired
    private RoleDao roleDao;

    private static final long NOT_EXIST_ID = 99999L;

    @Before
    public void setUp() {
        daoTestsHelper.persistRoles();
    }

    @After
    public void tearDown() {
        daoTestsHelper.dropRoles();
    }

    @Test
    public void allRolesHasInTable() {
        for (RoleType type : RoleType.values()) {
            assertThat(
                    type,
                    equalTo(roleDao.findByName(type).getRoleType())
            );
        }
    }

    @Test
    public void findByName() {
        for (RoleType roleType : RoleType.values()) {
            assertThat(
                    roleDao.findByName(roleType).getRoleType(),
                    equalTo(roleType)
            );
        }
    }

    @Test
    public void findById() {
        assertThat(
                roleDao.findOne(NOT_EXIST_ID),
                equalTo(null)
        );

        for (RoleType roleType : RoleType.values()) {
            Role roleExpected = roleDao.findByName(roleType);
            Role roleActual = roleDao.findOne(roleExpected.getId());

            assertThat(
                    roleActual,
                    equalTo(roleExpected)
            );
        }

    }

    @Test
    public void findAll() {
        List<Role> roles = roleDao.findAll();

        for (RoleType roleType : RoleType.values()) {
            Role role = roleDao.findByName(roleType);

            assertThat(
                    true,
                    equalTo(roles.contains(role))
            );
        }

        assertThat(
                roleDao.count(),
                equalTo(((long) roles.size()))
        );
    }

    @Test
    public void count() {
        assertThat(
                roleDao.count(),
                equalTo(((long) roleDao.findAll().size()))
        );
    }

    @Test
    public void exists() {

        for (RoleType roleType : RoleType.values()) {
            Role role = roleDao.findByName(roleType);

            assertThat(
                    true,
                    equalTo(roleDao.exist(role.getId()))
            );
        }

        assertThat(
                false,
                is(equalTo(roleDao.exist(NOT_EXIST_ID)))
        );
    }

    @Test
    public void delete() {
        Role role = roleDao.findByName(RoleType.USER);
        roleDao.delete(role);
        assertThat(
                roleDao.findByName(RoleType.USER),
                equalTo(null)
        );
    }

}
