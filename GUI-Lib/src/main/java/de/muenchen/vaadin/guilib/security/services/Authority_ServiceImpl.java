package de.muenchen.vaadin.guilib.security.services;

import com.netflix.discovery.DiscoveryClient;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.apilib.local.Authority;
import de.muenchen.vaadin.demo.apilib.rest.Authority_RestClient;
import de.muenchen.vaadin.demo.apilib.rest.Authority_RestClientImpl;
import de.muenchen.vaadin.demo.apilib.rest.Permission_RestClientImpl;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.vaadin.guilib.services.InfoService;
import de.muenchen.vaadin.guilib.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
@SpringComponent @UIScope
public class Authority_ServiceImpl implements Authority_Service, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Authority_ServiceImpl.class);
    public static final String TIMEOUT_I18N = "timeout";
	public static final int TIMEOUT_VAL = 5;
    
    private Authority_RestClient client;
    private RestTemplate template;
    private SecurityService securityService;
    private final ExecutorService executor;

    @Autowired
    public Authority_ServiceImpl(DiscoveryClient discoveryClient, SecurityService securityService) {
        this.securityService=securityService;
        URI secUri = null;
        try {
            String base_url = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
            secUri = new URI(base_url + "uaa/");
        } catch (RuntimeException | URISyntaxException e) {
            LOG.error(e.getMessage());
        }
        this.client = new Authority_RestClientImpl(getTemplate(), secUri);
        executor = Executors.newCachedThreadPool();
    }

	/**
	 * creates one Authority_
	 * @param authority the one who shall be createed
	 * @return authority
	 */
    @Override
    public Authority create(Authority authority) {
    	Authority returnAuthority;
        Future<Authority> result = executor.submit(() -> client.create(authority));
        try {
            returnAuthority = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.create);
        } catch (HttpClientErrorException e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } catch (TimeoutException e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create, TIMEOUT_I18N);
        } catch (Exception e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } finally {
            result.cancel(true);
        }
        return returnAuthority;
    }

	/**
	 * updates one Authority_
	 * @param authority the one who shall be updated
	 * @return authority
	 */
    @Override
    public Authority update(Authority authority) {
    	Authority returnAuthority;
        Future<Authority> result = executor.submit(() -> client.update(authority));
        try {
            returnAuthority = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.update);
        } catch (HttpClientErrorException e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } catch (TimeoutException e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update, TIMEOUT_I18N);
        } catch (Exception e) {
            returnAuthority = Authority_FallbackDataGenerator.createAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } finally {
            result.cancel(true);
        }
        return returnAuthority;
    }

	/**
	 * Method to delete a certain link
	 * @param link the link
	 * @return successful
	 */
    @Override
    public boolean delete(Link link) {
    	Future<?> result = executor.submit(() -> client.delete(link));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.delete);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            HttpStatus statusCode = e.getStatusCode();
            if (statusCode.equals(HttpStatus.CONFLICT) || statusCode.equals(HttpStatus.NOT_FOUND))
                showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, statusCode.toString());
            else
                showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, TIMEOUT_I18N);
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
            return false;
        } finally {
            result.cancel(true);
        }
    }

	/**
	 * Returns all Authority_
	 * @return authorities the results
	 */
    @Override
    public List<Authority> findAll() {
        List<Authority> authoritys;
        Future<List<Authority>> result = Executors.newCachedThreadPool().submit((Callable<List<Authority>>) client::findAll);
        try {
            authoritys = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            return authoritys;
        } catch (HttpClientErrorException e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return authoritys;
    }

	/**
	 * Returns all Authority_ of a specified relation
	 * @param relation the relation
	 * @return authorities the results
	 */
    @Override
    public List<Authority> findAll(Link relation) {
        List<Authority> authoritys;
        Future<List<Authority>> result = executor.submit(() -> client.findAll(relation));
        try {
            authoritys = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return authoritys;
    }

	/**
	 * Returns an Optional of Authority_ to one Link
	 * @param link the link
	 * @return the found Authority_ 
	 */
    @Override
    public Optional<Authority> findOne(Link link) {
        Optional<Authority> authority;
        Future<Optional<Authority>> result = executor.submit(() -> client.findOne(link));
        try {
            authority = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            authority = Authority_FallbackDataGenerator.createOptionalAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            authority = Authority_FallbackDataGenerator.createOptionalAuthorityFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            authority = Authority_FallbackDataGenerator.createOptionalAuthorityFallback();
			if(link.getRel()==null){
            	LOG.error(e.getMessage());
            	showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            }
        } finally {
            result.cancel(true);
        }
        return authority;
    }

	/**
	 * Query to given string
	 * @param query
	 * @return authorities the results
	 */
    @Override
    public List<Authority> queryAuthority(String query) {
        List<Authority> authoritys;
        Future<List<Authority>> result = executor.submit(() -> client.findFullTextFuzzy(query));
        try {
            authoritys = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e){
	          authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
	          LOG.error(e.getMessage());
	        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            authoritys = Authority_FallbackDataGenerator.createAuthoritysFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
		} finally {
			result.cancel(true);
		}
        return authoritys;
    }

	/**
     * Sets relations to entities
     * @param link the endpoint of the relation
     * @param links collection of links that are set to be related
     * @return successful
     */
    @Override
    public boolean setRelations(Link link, List<Link> links) {
        Future<?> result = executor.submit(() -> client.setRelations(link, links));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association, TIMEOUT_I18N);
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        } finally {
            result.cancel(true);
        }
    }

	/**
     * Sets a relation to an entity
     * @param link the endpoint of the relation
     * @param relation the link that is set to be related
     * @return successful
     */
    @Override
    public boolean setRelation(Link link, Link relation) {
        Future<?> result = executor.submit(() -> client.setRelation(link, relation));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association, TIMEOUT_I18N);
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        } finally {
            result.cancel(true);
        }
    }

    /**
     * Gets the resttemplate from the security if not present
     * @return resttemplate of this session
     */
    public RestTemplate getTemplate() {
        if (template != null) {
            return template;
        }
        return securityService.getRestTemplate().orElse(null);
    }
    
    /**
     * Shows a success notification
     * @param type the type of the notification
     * @param action the type of action performed
     */
    private void showSuccessNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

	/**
     * Shows an error notification
     * @param type the type of the notification
     * @param action the type of action performed
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

	/**
     * Shows an error notification specified by a status code
     * @param type the type of the notification
     * @param action the type of action performed
     * @param statusCode the status code
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action, String statusCode) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Authority.class, getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
