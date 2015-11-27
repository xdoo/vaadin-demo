package de.muenchen.auth;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author claus.straube
 */
@Repository
@Transactional
public class QueryService {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    TenantService tenantService;

    @SuppressWarnings("unchecked")
    @PostFilter(TenantService.IS_TENANT_FILTER)
    public <E extends BaseEntity> List<E> query(String text, Class<E> entity, String[] properties) {
        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager
                = org.hibernate.search.jpa.Search.
                getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder
                = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(entity).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query
                = queryBuilder
                .keyword()
                .fuzzy()
                .onFields(properties)
                .matching(text)
                .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, entity);


        // execute search and return results (sorted by relevance as default)
        return jpaQuery.getResultList();
    }
}
