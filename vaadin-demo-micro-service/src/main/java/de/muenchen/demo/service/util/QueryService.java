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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.services.UserService;

/**
 *
 * @author claus.straube
 */
public class QueryService<E> {
    
    private UserService userService;
	
    String[] fields;
    EntityManager em;
    Class<E> entity;
    
    public QueryService(UserService userService, EntityManager em, Class<E> entity, String... fields) {
        this.fields = fields;
        this.em = em;
        this.entity = entity;
        this.userService = userService;
    }
    
    @SuppressWarnings("unchecked")
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
                	.fuzzy()
                .onFields(fields)
                // Dates muessen umgewandelt oder ignoriert werden
//                	.ignoreFieldBridge()
                .matching(text)
                .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, entity);

        // limit query to tenant
        jpaQuery.enableFullTextFilter("tenantSearchFilter").setParameter("mandantoid", readTenantOid());
        
        // execute search and return results (sorted by relevance as default)
        return jpaQuery.getResultList();
    }
    
    private String readTenantOid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User readByUsername = userService.readByUsername(authentication.getName());
		String mid = readByUsername.getMandant().getOid();
		
		if (mid == null) {
			throw new IllegalStateException("tenat-id must not be null");
		}
		
		return mid;
    }
}
