package de.muenchen.demo.service;

import de.muenchen.auditing.AuditingConfiguration;
import de.muenchen.auditing.AuditingService;
import de.muenchen.demo.service.domain.AuditingUserRepositoryImpl;
import de.muenchen.eventbus.EventBus;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	 private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private EventBus eventBus;
	@Autowired
	AuditingUserRepositoryImpl repo;

	/*
	 * This method is called during Spring's startup.
	 * 
	 * @param event Event raised when an ApplicationContext gets initialized or
	 * refreshed.
	 */
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {

		new AuditingConfiguration(entityManager.getEntityManagerFactory(), eventBus).registerListeners();
		new AuditingService(eventBus, repo);

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			LOG.error("Lucene indexing interrupted, index might be incomlpete!", e);
		}

		return;
	}

}