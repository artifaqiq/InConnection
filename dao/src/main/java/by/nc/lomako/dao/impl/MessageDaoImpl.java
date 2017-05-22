/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.MessageDao;
import by.nc.lomako.pojos.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class MessageDaoImpl extends AbstractCrudDao<Message, Long> implements MessageDao {

    public MessageDaoImpl() {
        super(Message.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> findLastMessagesByUsers(long firstUserId, long secondUserId, int start, int limit) {
        return em.createQuery(
                "from Message m " +
                        "where (m.userFrom.id = :firstUserId and m.userTo.id = :secondUserId)" +
                        "or (m.userFrom.id = :secondUserId and m.userTo.id = :firstUserId)" +
                        "order by createdDate desc"
        )
                .setParameter("firstUserId", firstUserId)
                .setParameter("secondUserId", secondUserId)
                .setFirstResult(start)
                .setMaxResults(start + limit)
                .getResultList();
    }

    @Override
    public void deleteAllByUser(long firstUserId, long secondUserId) {
        em.createQuery(
                "delete from Message m " +
                        "where (m.userFrom.id = :firstUserId and m.userTo.id = :secondUserId)" +
                        "or (m.userFrom.id = :secondUserId and m.userTo.id = :firstUserId)"
        )
                .setParameter("firstUserId", firstUserId)
                .setParameter("secondUserId", secondUserId)
                .executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> findLastDialogs(long userId, int start, int limit) {
        return em.createQuery(
                "from Message m " +
                        "where m.userFrom in (select distinct m.userFrom from Message m where m.userTo.id = :userId or m.userFrom.id = :userId) " +
                        "and m.userTo in (select distinct m.userTo from Message m where m.userTo.id = :userId or m.userFrom.id = :userId)" +
                        "order by createdDate desc "
        )
                .setParameter("userId", userId)
                .setFirstResult(start)
                .setMaxResults(start + limit)
                .getResultList();
    }


    @Override
    public Long countByUsers(long firstUserId, long secondUserId) {
        return (Long) em.createQuery(
                "select count(*) from Message m " +
                        "where (m.userFrom.id = :firstUserId and m.userTo.id = :secondUserId) " +
                        "or (m.userFrom.id = :secondUserId and m.userTo.id = :firstUserId)"
        )
                .setParameter("firstUserId", firstUserId)
                .setParameter("secondUserId", secondUserId)
                .getSingleResult();
    }

    @Override
    public Long countDialogs(long userId) {
        return (Long) em.createQuery(
                "select count(*) from Message m " +
                        "where m.userFrom in (select distinct m.userFrom from Message m where m.userFrom.id = :userId) " +
                        "or m.userTo in (select distinct m.userFrom from Message m where m.userTo.id = :userId)"
        )
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
