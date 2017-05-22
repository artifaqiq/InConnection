/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.impl;

import by.nc.lomako.dao.PostDao;
import by.nc.lomako.pojos.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Lomako
 * @version 1.0
 */
@Repository
public class PostDaoImpl extends AbstractCrudDao<Post, Long> implements PostDao {

    public PostDaoImpl() {
        super(Post.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> findLastByUser(long userId, int start, int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();

        Root<Post> root = q.from(Post.class);
        q
                .select(root)
                .where(cb.equal(root.get("user").get("id"), cb.parameter(Long.class, "userId")))
                .orderBy(cb.desc(root.get("createdDate")), cb.desc((root.get("id"))));

        return em.createQuery(q)
                .setParameter("userId", userId)
                .setFirstResult(start)
                .setMaxResults(start + limit)
                .getResultList();

    }

    @Override
    public long countByUser(long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();

        Root<Post> root = q.from(Post.class);
        q
                .select(cb.count(root))
                .where(cb.equal(root.get("user").get("id"), cb.parameter(Long.class, "userId")));

        return (Long) em.createQuery(q)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
