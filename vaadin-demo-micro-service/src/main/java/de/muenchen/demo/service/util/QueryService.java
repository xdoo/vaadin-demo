
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.util;

import de.muenchen.demo.service.domain.BaseEntity;
import de.muenchen.demo.service.security.TenantService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
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

    private String readTenantOid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String currentTenantId = tenantService.getCurrentTenantId(authentication);
        return currentTenantId;
    }

    public Filter getFilter(String tenant) {
        Query query = new TermQuery(new Term("mandant", tenant));
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }
}