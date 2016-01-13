package de.muenchen.vaadin.guilib.security.services;

import com.netflix.discovery.DiscoveryClient;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.apilib.local.Permission;
import de.muenchen.vaadin.demo.apilib.rest.Authority_RestClient;
import de.muenchen.vaadin.demo.apilib.rest.Authority_RestClientImpl;
import de.muenchen.vaadin.demo.apilib.rest.Permission_RestClient;
import de.muenchen.vaadin.demo.apilib.rest.Permission_RestClientImpl;
import de.muenchen.vaadin.demo.apilib.rest.User_RestClientImpl;
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
public class Permission_ServiceImpl implements Permission_Service, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Permission_ServiceImpl.class);
    public static final String TIMEOUT_I18N = "timeout";
	public static final int TIMEOUT_VAL = 5;
    
    private Permission_RestClient client;
    private RestTemplate template;
    private SecurityService securityService;
    private final ExecutorService executor;

    @Autowired
    public Permission_ServiceImpl(DiscoveryClient discoveryClient, SecurityService securityService) {
        this.securityService=securityService;
        URI secUri = null;
        try {
            String base_url = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
            secUri = new URI(base_url + "uaa/");
        } catch (RuntimeException | URISyntaxException e) {
            LOG.error(e.getMessage());
        }
        this.client = new Permission_RestClientImpl(getTemplate(), secUri);
        executor = Executors.newCachedThreadPool();
    }

	/**
	 * creates one Permission_
	 * @param permission the one who shall be createed
	 * @return permission
	 */
    @Override
    public Permission create(Permission permission) {
    	Permission returnPermission;
        Future<Permission> result = executor.submit(() -> client.create(permission));
        try {
            returnPermission = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.create);
        } catch (HttpClientErrorException e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } catch (TimeoutException e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create, TIMEOUT_I18N);
        } catch (Exception e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } finally {
            result.cancel(true);
        }
        return returnPermission;
    }

	/**
	 * updates one Permission_
	 * @param permission the one who shall be updated
	 * @return permission
	 */
    @Override
    public Permission update(Permission permission) {
    	Permission returnPermission;
        Future<Permission> result = executor.submit(() -> client.update(permission));
        try {
            returnPermission = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.update);
        } catch (HttpClientErrorException e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } catch (TimeoutException e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update, TIMEOUT_I18N);
        } catch (Exception e) {
            returnPermission = Permission_FallbackDataGenerator.createPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } finally {
            result.cancel(true);
        }
        return returnPermission;
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
	 * Returns all Permission_
	 * @return permissions the results
	 */
    @Override
    public List<Permission> findAll() {
        List<Permission> permissions;
        Future<List<Permission>> result = Executors.newCachedThreadPool().submit((Callable<List<Permission>>) client::findAll);
        try {
            permissions = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            return permissions;
        } catch (HttpClientErrorException e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return permissions;
    }

	/**
	 * Returns all Permission_ of a specified relation
	 * @param relation the relation
	 * @return permissions the results
	 */
    @Override
    public List<Permission> findAll(Link relation) {
        List<Permission> permissions;
        Future<List<Permission>> result = executor.submit(() -> client.findAll(relation));
        try {
            permissions = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return permissions;
    }

	/**
	 * Returns an Optional of Permission_ to one Link
	 * @param link the link
	 * @return the found Permission_ 
	 */
    @Override
    public Optional<Permission> findOne(Link link) {
        Optional<Permission> permission;
        Future<Optional<Permission>> result = executor.submit(() -> client.findOne(link));
        try {
            permission = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            permission = Permission_FallbackDataGenerator.createOptionalPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            permission = Permission_FallbackDataGenerator.createOptionalPermissionFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            permission = Permission_FallbackDataGenerator.createOptionalPermissionFallback();
			if(link.getRel()==null){
            	LOG.error(e.getMessage());
            	showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            }
        } finally {
            result.cancel(true);
        }
        return permission;
    }

	/**
	 * Query to given string
	 * @param query
	 * @return permissions the results
	 */
    @Override
    public List<Permission> queryPermission(String query) {
        List<Permission> permissions;
        Future<List<Permission>> result = executor.submit(() -> client.findFullTextFuzzy(query));
        try {
            permissions = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e){
	          permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
	          LOG.error(e.getMessage());
	        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            permissions = Permission_FallbackDataGenerator.createPermissionsFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
		} finally {
			result.cancel(true);
		}
        return permissions;
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
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

	/**
     * Shows an error notification
     * @param type the type of the notification
     * @param action the type of action performed
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.text)));
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
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                BaseUI.getCurrentI18nResolver().resolveRelative(Permission.class, getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
