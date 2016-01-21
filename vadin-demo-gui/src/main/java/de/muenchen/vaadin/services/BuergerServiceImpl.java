package de.muenchen.vaadin.services;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.domain.Services;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClient;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClientImpl;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.bus.Event;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerServiceImpl implements BuergerService, Serializable {

    public static final String TIMEOUT_I18N = "timeout";
    public static final int TIMEOUT_VAL = 5;
    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    private BuergerRestClient client;
    private RestTemplate template;
    private SecurityService securityService;
    private I18nResolverImpl resolver;
    private ExecutorService executor;



    @Autowired
    public BuergerServiceImpl(SecurityService securityService, I18nResolverImpl resolver) {
        this.securityService = securityService;
        //TODO
        this.client = new BuergerRestClientImpl(getTemplate());
        this.resolver = resolver;
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public Buerger create(Buerger buerger) {
        Buerger returnBuerger;
        Future<Buerger> result = executor.submit(() -> client.create(buerger));

        try {
            returnBuerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return returnBuerger;
    }

    @Override
    public Buerger update(Buerger buerger) {
        Buerger returnBuerger;
        Future<Buerger> result = executor.submit(() -> client.update(buerger));
        try {
            returnBuerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return returnBuerger;
    }

    @Override
    public boolean delete(Link link) {
        Future<?> result = executor.submit(() -> client.delete(link));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
            return true;
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            LOG.error(e.getMessage());
            HttpStatus statusCode = exception.getStatusCode();
            if (statusCode.equals(HttpStatus.CONFLICT) || statusCode.equals(HttpStatus.NOT_FOUND))
                showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, statusCode.toString());
            else
                showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } finally {
            result.cancel(true);
        }
    }

    @Override
    public List<Buerger> findAll() {
        List<Buerger> buergers;
        Future<List<Buerger>> result = executor.submit((Callable<List<Buerger>>) client::findAll);
        try {
            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
            return buergers;
        } catch (ExecutionException e) {
          //  HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public List<Buerger> findAll(Link relation) {
        List<Buerger> buergers;
        Future<List<Buerger>> result = executor.submit(() -> client.findAll(relation));
        try {
            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public Optional<Buerger> findOne(Link link) {
        Optional<Buerger> buerger;
        Future<Optional<Buerger>> result = executor.submit(() -> client.findOne(link));
        try {
            buerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return buerger;
    }

    @Override
    public List<Buerger> queryBuerger(String query) {
        List<Buerger> buergers;
        Future<List<Buerger>> result = executor.submit(() -> client.findFullTextFuzzy(query));
        try {
            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
        } catch (HttpClientErrorException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (TimeoutException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
        } finally {
            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public boolean setRelations(Link link, List<Link> links) {
        Future<?> result = executor.submit(() -> client.setRelations(link, links));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } finally {
            result.cancel(true);
        }
    }

    @Override
    public boolean setRelation(Link link, Link relation) {
        Future<?> result = executor.submit(() -> client.setRelation(link, relation));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(false));
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (TimeoutException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association, TIMEOUT_I18N);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            BaseUI.getCurrentEventBus().send(new RequestEntityKey(RequestEvent.ERROR, boolean.class), Event.wrap(true));
            return false;
        } finally {
            result.cancel(true);
        }
    }

    /**
     * Gets the resttemplate from the security if not present
     *
     * @return resttemplate of this session
     */
    public RestTemplate getTemplate() {
        if (template != null) {
            return template;
        }
        return securityService.getRestTemplate().orElse(null);
    }

    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                resolver.resolveRelative(Buerger.class, getNotificationPath(type, action, I18nPaths.Type.label)),
                resolver.resolveRelative(Buerger.class, getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
        //throw new RuntimeException();
    }

    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action, String statusCode) {
        GenericErrorNotification succes = new GenericErrorNotification(
                resolver.resolveRelative(Buerger.class, getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                resolver.resolveRelative(Buerger.class, getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
