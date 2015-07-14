/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.util;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 *
 * @author claus.straube
 */
public class QueryService<E> {
    
    String[] fields;
    EntityManager em;
    Class<E> entity;

    public QueryService(EntityManager em, Class<E> entity, String... fields) {
        this.fields = fields;
        this.em = em;
        this.entity = entity;
    }
    
    public List<E> query(String text) {
        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager
                = org.hibernate.search.jpa.Search.
                getFullTextEntityManager(em);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder
                = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(entity).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query
                = queryBuilder
                .keyword()
                .onFields(fields)
                .matching(text)
                .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, entity);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List results = jpaQuery.getResultList();
        return results;
    }
    
}
