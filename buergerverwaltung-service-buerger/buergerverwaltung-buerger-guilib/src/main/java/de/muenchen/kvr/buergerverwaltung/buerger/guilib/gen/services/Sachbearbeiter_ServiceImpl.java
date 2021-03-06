package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Sachbearbeiter_RestClient;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Sachbearbeiter_RestClientImpl;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.services.Sachbearbeiter_FallbackDataGenerator;

import de.muenchen.vaadin.guilib.services.SecurityService;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
@SpringComponent @UIScope
public class Sachbearbeiter_ServiceImpl implements Sachbearbeiter_Service, Serializable {
    
    private Sachbearbeiter_RestClient client;
    private SecurityService securityService;

    @Autowired
    public Sachbearbeiter_ServiceImpl(SecurityService securityService, @Value("${BUERGER.microservice.basePath}") String basePath) {
        this.securityService = securityService;
        final URI baseUri = URI.create(basePath);
		this.client = new Sachbearbeiter_RestClientImpl(getTemplate(), baseUri);
    }

	/**
	 * creates one Sachbearbeiter_
	 * @param sachbearbeiter the one who shall be created
	 * @return sachbearbeiter
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultCreate")
    public Sachbearbeiter_ create(Sachbearbeiter_ sachbearbeiter) {
        return client.create(sachbearbeiter);
    }
    public Sachbearbeiter_ defaultCreate(Sachbearbeiter_ bean) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
		return Sachbearbeiter_FallbackDataGenerator.createSachbearbeiterFallback();
	}

	/**
	 * updates one Sachbearbeiter_
	 * @param sachbearbeiter the one who shall be updated
	 * @return sachbearbeiter
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultUpdate")
    public Sachbearbeiter_ update(Sachbearbeiter_ sachbearbeiter) {
        return client.update(sachbearbeiter);
    }
    public Sachbearbeiter_ defaultUpdate(Sachbearbeiter_ bean) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
		return Sachbearbeiter_FallbackDataGenerator.createSachbearbeiterFallback();
	}

	/**
	 * Method to delete a certain link
	 * @param link the link
	 * @return successful
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultDelete")
    public boolean delete(Link link) {
    	try {
    		client.delete(link);
    	} catch (HttpClientErrorException e) {
    		final HttpStatus statusCode = e.getStatusCode();
			if (!HttpStatus.CONFLICT.equals(statusCode))
				throw e;
			showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, statusCode.toString());
			return false;
		}
		return true;
    }
	public boolean defaultDelete(Link link) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
		return false;
	}

	/**
	 * Returns all Sachbearbeiter_
	 * @return sachbearbeiters the results
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultFindAll")
    public List<Sachbearbeiter_> findAll() {
        return client.findAll();
    }
    public List<Sachbearbeiter_> defaultFindAll() {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
		return Sachbearbeiter_FallbackDataGenerator.createSachbearbeitersFallback();
	}

	/**
	 * Returns all Sachbearbeiter_ of a specified relation
	 * @param relation the relation
	 * @return sachbearbeiters the results
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultFindAll")
    public List<Sachbearbeiter_> findAll(Link relation) {
        return client.findAll(relation);
    }
    public List<Sachbearbeiter_> defaultFindAll(Link relation) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
		return Sachbearbeiter_FallbackDataGenerator.createSachbearbeitersFallback();
	}

	/**
	 * Returns an Optional of Sachbearbeiter_ to one Link
	 * @param link the link
	 * @return the found Sachbearbeiter_ 
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultFindOne")
    public Optional<Sachbearbeiter_> findOne(Link link) {
        return client.findOne(link);
    }
    public Optional<Sachbearbeiter_> defaultFindOne(Link link) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
		return Sachbearbeiter_FallbackDataGenerator.createOptionalSachbearbeiterFallback();
	}
    

	/**
	 * Query to given string
	 * @param query
	 * @return sachbearbeiters the results
	 */
    @Override
    @HystrixCommand(fallbackMethod = "defaultQuerySachbearbeiter")
    public List<Sachbearbeiter_> querySachbearbeiter(String query) {
        return client.findFullTextFuzzy(query);
    }
    public List<Sachbearbeiter_> defaultQuerySachbearbeiter(String query) {
		showErrorNotification(I18nPaths.NotificationType.error,SimpleAction.read);
		return Sachbearbeiter_FallbackDataGenerator.createSachbearbeitersFallback();
	}

	/**
     * Sets relations to entities
     * @param link the endpoint of the relation
     * @param links collection of links that are set to be related
     * @return successful
     */
    @Override
    @HystrixCommand(fallbackMethod = "defaultSetRelations")
    public void setRelations(Link link, List<Link> links) {
        client.setRelations(link, links);
    }
    public void defaultSetRelations(Link link, List<Link> links) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
	}

	/**
     * Sets a relation to an entity
     * @param link the endpoint of the relation
     * @param relation the link that is set to be related
     * @return successful
     */
    @Override
    @HystrixCommand(fallbackMethod = "defaultSetRelation")
    public void setRelation(Link link, Link relation) {
        client.setRelation(link, relation);
    }
    public void defaultSetRelation(Link link, Link relation) {
		showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
	}

    /**
     * Gets the resttemplate from the security if not present
     * @return resttemplate of this session
     */
    public RestTemplate getTemplate() {
        return securityService.getRestTemplate().orElse(null);
    }

	/**
     * Shows an error notification
     * @param type   the type of the notification
     * @param action the type of action performed
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Sachbearbeiter_.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Sachbearbeiter_.class, getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

	/**
     * Shows an error notification specified by a status code
     * @param type       the type of the notification
     * @param action     the type of action performed
     * @param statusCode the status code
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action, String statusCode) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Sachbearbeiter_.class, getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Sachbearbeiter_.class, getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
