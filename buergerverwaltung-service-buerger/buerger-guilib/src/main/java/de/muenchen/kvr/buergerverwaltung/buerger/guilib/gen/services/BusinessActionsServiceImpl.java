package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services;


import de.muenchen.kvr.buergerverwaltung.buerger.client.businessactions.BusinessActionsRestClientImpl;

import de.muenchen.vaadin.guilib.services.SecurityService;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * Provides a simple Implementation for a GUI-Service that can perform BusinessActions.
 *
 * @author p.mueller
 * @version 1.0
 */
@SpringComponent("buerger_BusinessActionsService")
@UIScope
public class BusinessActionsServiceImpl implements BusinessActionsService {
	/** The Logger of the Class*/
    private static final Logger LOG = LoggerFactory.getLogger(BusinessActionsServiceImpl.class);
    /** Constant String for the timeout*/
    public static final String TIMEOUT_I18N = "timeout";
    /** Constant int for the timeout*/
	public static final int TIMEOUT_VAL = 5;

	/** The service used for getting the right RestTemplate. */
	private final SecurityService securityService;
	/** The Client used for the BusinessActions Operations. */
	private final BusinessActionsRestClientImpl client;
	/** The Template used to make the REST calls. */
	private RestTemplate template;
	/** The ExecuterService */
	private final ExecutorService executor;
	
	/**
	 * Create a new BusinessActionsService to execute the BusinessActions in the GUI.
	 * @param securityService The securityService.
	 */
	@Autowired
	public BusinessActionsServiceImpl(SecurityService securityService, @Value("${BUERGER.microservice.basePath}") String basePath) {
		this.securityService=securityService;
		
		final URI baseUri = URI.create(basePath);
		this.client = new BusinessActionsRestClientImpl(getTemplate(), baseUri);
		executor = Executors.newCachedThreadPool();
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
     * Shows an error notification
     * @param type the type of the notification
     * @param action the type of action performed
     */
    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                BaseUI.getCurrentI18nResolver().resolve("buisnessAction"+getNotificationPath(type, action, I18nPaths.Type.label)),
                BaseUI.getCurrentI18nResolver().resolve("buisnessAction"+getNotificationPath(type, action, I18nPaths.Type.text)));
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
                BaseUI.getCurrentI18nResolver().resolve("buisnessAction"+getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                BaseUI.getCurrentI18nResolver().resolve("buisnessAction"+getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
