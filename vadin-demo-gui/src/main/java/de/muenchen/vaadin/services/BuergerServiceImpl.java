package de.muenchen.vaadin.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.contrib.javanica.command.HystrixCommandBuilder;
import com.netflix.hystrix.contrib.javanica.command.HystrixCommandFactory;
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

    @HystrixCommand(fallbackMethod = "createFallback")
    @Override
    public Buerger create(Buerger buerger) {
        final Buerger buerger1 = client.create(buerger);
        showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.create);
        return buerger1;
    }

    @HystrixCommand(fallbackMethod = "updateFallback")
    @Override
    public Buerger update(Buerger buerger) {
        final Buerger buerger1 = client.update(buerger);
        showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.update);
        return buerger1;
    }

    @HystrixCommand(fallbackMethod = "deleteFallback")
    @Override
    public boolean delete(Link link) {
        client.delete(link);
        showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.delete);
        return true;
    }

    @HystrixCommand(fallbackMethod = "copyFallback")
    @Override
    public Buerger copy(Link link) {
        Optional<Buerger> original = findOne(link);
        if (!original.isPresent())
            return null;

        return create(original.get());
    }

    @HystrixCommand(fallbackMethod = "findAllFallback")
    @Override
    public List<Buerger> findAll() {
        return client.findAll();
    }

    @HystrixCommand(fallbackMethod = "findAllFallback")
    @Override
    public List<Buerger> findAll(Link relation) {
        return client.findAll(relation);
    }

    @HystrixCommand(fallbackMethod = "findOneFallback")
    @Override
    public Optional<Buerger> findOne(Link link) {
        return client.findOne(link);
    }

    @HystrixCommand(fallbackMethod = "queryFallback")
    @Override
    public List<Buerger> queryBuerger(String query) {
        //    Link link = this.infoService.getUrl("buerger_query");
        //    ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        //    return client.queryBuerger(query, links, getTemplate());
        return new ArrayList<>();
    }


    @HystrixCommand(fallbackMethod = "setRelationsFallback")
    @Override
    public boolean setRelations(Link link, List<Link> links) {
        client.setRelations(link, links);
        showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
        return true;
    }

    @HystrixCommand(fallbackMethod = "setRelationFallback")
    @Override
    public boolean setRelation(Link link, Link relation) {
        client.setRelation(link, relation);
        showSuccessNotification(I18nPaths.NotificationType.success, SimpleAction.association);
        return true;
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

    //////////////////////
    // Fallback Methods //
    //////////////////////

    public Buerger createFallback(Buerger buerger) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.create);
        return null;
    }

    public Buerger updateFallback(Buerger buerger) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.update);
        return null;
    }

    public boolean deleteFallback(Link link) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.delete);
        return false;
    }

    public Buerger copyFallback(Link link) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.copy);
        return null;
    }

    public List<Buerger> findAllFallback() {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        return new ArrayList<>();
    }

    public List<Buerger> findAllFallback(Link relation) {
        return findAllFallback();
    }

    public Optional<Buerger> findOneFallback(Link link) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.read);
        return Optional.empty();
    }

    public List<Buerger> queryFallback(Link link) {
        return findAllFallback();
    }

    public boolean setRelationsFallback(Link link, List<Link> links){
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
        return false;
    }

    public boolean setRelationFallback(Link link, Link relation) {
        showErrorNotification(I18nPaths.NotificationType.error, SimpleAction.association);
        return false;
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
}
