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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerServiceImpl implements BuergerService, Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);

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
        Buerger buerger1;
        try {
            buerger1 = client.create(buerger);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.create);
        } catch (HttpClientErrorException e){
            LOG.error(e.getMessage());
            buerger1 = null;
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buerger1 = null;
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        }
        return buerger1;
    }

    @Override
    public Buerger update(Buerger buerger) {
        Buerger buerger1;
        try {
            buerger1 = client.update(buerger);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.update);
        } catch (HttpClientErrorException e){
            LOG.error(e.getMessage());
            buerger1 = null;
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buerger1 = null;
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        }
        return buerger1;
    }

    @Override
    public boolean delete(Link link) {
        try {
            client.delete(link);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.delete);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            String statusCode = e.getStatusCode().toString();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete, statusCode);
            return false;
        } catch (Exception e){
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
            return false;
        }
    }

    @Override
    public List<Buerger> findAll() {
        List<Buerger> buergers;
        try {
            buergers = client.findAll();
            return buergers;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        }
        return buergers;
    }

    @Override
    public List<Buerger> findAll(Link relation) {
        List<Buerger> buergers;
        try {
            buergers = client.findAll(relation);
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        }
        return buergers;
    }

    @Override
    public Optional<Buerger> findOne(Link link) {
        Optional<Buerger> buerger;
        try {
            buerger = client.findOne(link);
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            buerger = Optional.empty();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buerger = Optional.empty();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        }
        return buerger;
    }

    @Override
    public List<Buerger> queryBuerger(String query) {
        List<Buerger> buergers;
        try {
            buergers = new ArrayList<>();
            //    Link link = this.infoService.getUrl("buerger_query");
            //    ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
            //    return client.queryBuerger(query, links, getTemplate());
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        } catch (Exception e){
            LOG.error(e.getMessage());
            buergers = new ArrayList<>();
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        }
        return buergers;
    }

    @Override
    public boolean setRelations(Link link, List<Link> links) {
        try {
            client.setRelations(link, links);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
        } catch (Exception e){
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        }
        return true;
    }

    @Override
    public boolean setRelation(Link link, Link relation) {
        try {
            client.setRelation(link, relation);
            showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
            return true;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
        } catch (Exception e){
            LOG.error(e.getMessage());
            showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
            return false;
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
