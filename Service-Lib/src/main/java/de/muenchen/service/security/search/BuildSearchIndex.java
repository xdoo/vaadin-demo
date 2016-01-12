package de.muenchen.service.security.search;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class BuildSearchIndex
        implements ApplicationListener {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an initial Lucene index for the data already present in the
     * database.
     * This method is called during Spring's startup.
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event.getClass()==ContextRefreshedEvent.class){
            try {
                FullTextEntityManager fullTextEntityManager =
                        Search.getFullTextEntityManager(entityManager);
                fullTextEntityManager.createIndexer().startAndWait();
            } catch (InterruptedException | IllegalArgumentException e) {
                System.out.println(
                        "An error occurred trying to build the serach index: " +
                                e.toString());
            }
        }
    }
}