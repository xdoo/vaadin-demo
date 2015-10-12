package de.muenchen.vaadin.services;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClient;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClientImpl;
import de.muenchen.vaadin.demo.apilib.services.SecurityService;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
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

    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    public static final String TIMEOUT_I18N = "timeout";
    public static final int TIMEOUT_VAL = 5;

    private BuergerRestClient client;
    private RestTemplate template;
    private SecurityService securityService;
    private BuergerI18nResolver resolver;

    @Autowired
    public BuergerServiceImpl(InfoService infoService, SecurityService securityService, BuergerI18nResolver resolver) {
        this.securityService = securityService;
        //TODO
        this.client = new BuergerRestClientImpl(getTemplate(), infoService.getBaseUri());
        this.resolver = resolver;
    }

    @Override
    public Buerger create(Buerger buerger) {
        Buerger returnBuerger;
        Future<Buerger> result = Executors.newCachedThreadPool().submit(() -> client.create(buerger));
        try {
            returnBuerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.create);
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } catch (TimeoutException e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create, TIMEOUT_I18N);
        } catch (Exception e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } finally {
            result.cancel(true);
        }
        return returnBuerger;
    }

    @Override
    public Buerger update(Buerger buerger) {
        Buerger returnBuerger;
        Future<Buerger> result = Executors.newCachedThreadPool().submit(() -> client.update(buerger));
        try {
            returnBuerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.update);
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } catch (TimeoutException e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update, TIMEOUT_I18N);
        } catch (Exception e) {
            returnBuerger = BuergerFallbackDataGenerator.createBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } finally {
            result.cancel(true);
        }
        return returnBuerger;
    }

    @Override
    public boolean delete(Link link) {
        Future<?> result = Executors.newCachedThreadPool().submit(() -> client.delete(link));
        try {
            result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.delete);
            return true;
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            LOG.error(e.getMessage());
            HttpStatus statusCode = exception.getStatusCode();
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

    @Override
    public List<Buerger> findAll() {
        List<Buerger> buergers;
        Future<List<Buerger>> result = Executors.newCachedThreadPool().submit((Callable<List<Buerger>>) client::findAll);
        try {
            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
            return buergers;
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public List<Buerger> findAll(Link relation) {
        List<Buerger> buergers;
        Future<List<Buerger>> result = Executors.newCachedThreadPool().submit(() -> client.findAll(relation));
        try {
            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public Optional<Buerger> findOne(Link link) {
        Optional<Buerger> buerger;
        Future<Optional<Buerger>> result = Executors.newCachedThreadPool().submit(() -> client.findOne(link));
        try {
            buerger = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            HttpClientErrorException exception = (HttpClientErrorException) e.getCause();
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(exception.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (TimeoutException e) {
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            buerger = BuergerFallbackDataGenerator.createOptionalBuergerFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
            result.cancel(true);
        }
        return buerger;
    }

    //TODO Wenn Suche gemerged wurde
    @Override
    public List<Buerger> queryBuerger(String query) {
//        Link link = this.infoService.getUrl("buerger_query");
//        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
//        return client.queryBuerger(query, links, getTemplate());

        List<Buerger> buergers;
//        Future<List<Buerger>> result = Executors.newCachedThreadPool().submit(() -> client.queryBuerger(query, links, getTemplate());
        try {
            buergers = new ArrayList<>();
//            buergers = result.get(TIMEOUT_VAL, TimeUnit.SECONDS);
        } catch (HttpClientErrorException e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
//        } catch (TimeoutException e){
//            buergers = this.createBuergersFallback();
//            LOG.error(e.getMessage());
//            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read, TIMEOUT_I18N);
        } catch (Exception e) {
            buergers = BuergerFallbackDataGenerator.createBuergersFallback();
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } finally {
//            result.cancel(true);
        }
        return buergers;
    }

    @Override
    public boolean setRelations(Link link, List<Link> links) {
        Future<?> result = Executors.newCachedThreadPool().submit(() -> client.setRelations(link, links));
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

    @Override
    public boolean setRelation(Link link, Link relation) {
        Future<?> result = Executors.newCachedThreadPool().submit(() -> client.setRelation(link, relation));
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
     *
     * @return resttemplate of this session
     */
    public RestTemplate getTemplate() {
        if (template != null) {
            return template;
        }
        return securityService.getRestTemplate().orElse(null);
    }

    private void showSuccessNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.label)),
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action) {
        GenericErrorNotification succes = new GenericErrorNotification(
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.label)),
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.text)));
        succes.show(Page.getCurrent());
    }

    private void showErrorNotification(I18nPaths.NotificationType type, SimpleAction action, String statusCode) {
        GenericErrorNotification succes = new GenericErrorNotification(
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.label, statusCode)),
                resolver.resolveRelative(getNotificationPath(type, action, I18nPaths.Type.text, statusCode)));
        succes.show(Page.getCurrent());
    }
}
