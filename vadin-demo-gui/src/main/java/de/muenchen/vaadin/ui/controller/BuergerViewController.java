package de.muenchen.vaadin.ui.controller;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.vaadin.guilib.services.MessageService;
import de.muenchen.vaadin.guilib.util.VaadinUtil;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.model.BuergerModel;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.factorys.BuergerViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext BuergerDTO abzubilden.
 *
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerViewController implements Serializable, I18nResolver {

    // TODO entweder hier oder im I18nServiceConfigImpl angeben
    public static final String I18N_BASE_PATH = "buerger";
    private static final long serialVersionUID = 1L;
    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);
    /** Die Service Klasse */
    private final BuergerService service;
    /** Model der Daten für den Eventbus */
    private final BuergerModel model = new BuergerModel();
    /** Werkzeuge für Vaadin */
    private final VaadinUtil util;
    /** Event Bus zur Kommunikation */
    @Autowired
    private EventBus eventbus;
    /** {@link MessageService} zur Auflösung der Platzhalter */
    @Autowired
    private MessageService msg;
    /** {@link UI} {@link Navigator} */
    private Navigator navigator;
    /** BuergerViewFactory zum erstellen der Components */
    @Autowired
    private BuergerViewFactory buergerViewFactory;

    @Autowired
    public BuergerViewController(BuergerService service, VaadinUtil util) {
        this.service = service;
        this.util = util;
    }

    @PostConstruct
    public void init() {
        //Set Controller in Factory after Construct to prevent circular reference
        buergerViewFactory.setController(this);
        initEventhandlers();
    }

    /**
     * Die Main wird über die {@link de.muenchen.vaadin.ui.app.views.DefaultBuergerView} registriert. Dies
     * ist notwendig, da die Vaadin abhängigen Beans zum Zeitpunkt der Instanzierung
     * noch nicht vorhanden sind. Aus der Main UI wird der {@link Navigator} geholt,
     * um über ihn eine Navigation zwischen einzelnen Views zu ermöglichen.
     *
     * @param ui MainUI
     */
    public void registerUI(MainUI ui) {
        this.navigator = ui.getNavigator();
    }

    public EventBus getEventbus() {
        return eventbus;
    }

    public VaadinUtil getUtil() {
        return util;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public BuergerViewFactory getViewFactory() {
        return buergerViewFactory;
    }

    public EventBus getBus() {
        return eventbus;
    }

    private BuergerModel getModel() {
        return model;
    }

    ////////////////////////
    // Service Operations //
    ////////////////////////

    private void releaseKind(Buerger kind) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.kinder.name());
        List<Link> kinder = service.findAll(link)
                .stream()
                .map(Buerger::getId)
                .filter(id -> !id.equals(kind.getId()))
                .collect(Collectors.toList());

        service.setRelations(link, kinder);
    }

    private void releasePartner(Buerger event) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.partner.name());
        service.delete(link);
    }

    private void addBuergerKind(Buerger kindEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.kinder.name());
        List<Link> kinder = Stream.concat(
                service.findAll(link)
                        .stream()
                        .map(Buerger::getId),
                Stream.of(kindEntity.getId()))

                .collect(Collectors.toList());

        service.setRelations(link, kinder);
    }

    private void setBuergerPartner(Buerger partnerEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.partner.name());
        service.setRelation(link, partnerEntity.getId());
    }

    private List<Buerger> queryBuerger() {
        return service.findAll().stream().collect(Collectors.toList());
    }

    private List<Buerger> queryBuerger(String query) {
        return queryBuerger(); //TODO
    }

    private List<Buerger> queryKinder(Buerger entity) {
        return service.findAll(entity.getLink(Buerger.Rel.kinder.name())).stream().collect(Collectors.toList());
    }

    private Buerger queryPartner(Buerger entity) {
        return service.findOne(entity.getLink(Buerger.Rel.partner.name())).orElse(null);
    }


    /////////////////////
    // Event Steuerung //
    /////////////////////

    /**
     * Register all event handlers on the RequestEntityKey.
     */
    public void initEventhandlers() {
        eventbus.on(getRequestKey(RequestEvent.CREATE).getSelector(), this::create);
        eventbus.on(getRequestKey(RequestEvent.DELETE).getSelector(), this::delete);
        eventbus.on(getRequestKey(RequestEvent.UPDATE).getSelector(), this::update);
        eventbus.on(getRequestKey(RequestEvent.ADD_ASSOCIATION).getSelector(), this::addAssociation);
        eventbus.on(getRequestKey(RequestEvent.REMOVE_ASSOCIATION).getSelector(), this::removeAssociation);
        eventbus.on(getRequestKey(RequestEvent.READ_LIST).getSelector(), this::readList);
        eventbus.on(getRequestKey(RequestEvent.READ_SELECTED).getSelector(), this::readSelected);
    }

    /**
     * Remove the specified Association from the specified Relation and update the DataStore.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Association} as {@link Event#getData()}.
     */
    private void removeAssociation(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");

        if (data instanceof Association) {
            @SuppressWarnings("unchecked") final Association<Buerger> association = (Association<Buerger>) event.getData();

            final Buerger.Rel rel = Buerger.Rel.valueOf(association.getRel());
            if (Buerger.Rel.kinder == rel)
                releaseKind(association.getAssociation());
            if (Buerger.Rel.partner == rel)
                releasePartner(association.getAssociation());

            refreshModelAssociations();
            showNotification(NotificationType.success, SimpleAction.release, rel);

        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
    }

    /**
     * Add the specified Association to the specified Relation and update the DataStore.
     * <p>
     * If the {@link Association#getAssociation()} has no {@link ResourceSupport#getId()} the Resouce will be created
     * on the DataStore first.
     * </p>
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Association} as {@link Event#getData()}.
     */
    private void addAssociation(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");

        if (data instanceof Association) {
            @SuppressWarnings("unchecked") final Association<Buerger> association = (Association<Buerger>) event.getData();
            final Buerger buerger = association.getAssociation().getId() == null ? service.create(association.getAssociation()) : association.getAssociation();
            final Buerger.Rel rel = Buerger.Rel.valueOf(association.getRel());
            if (Buerger.Rel.kinder == rel)
                addBuergerKind(buerger);
            if (Buerger.Rel.partner == rel)
                setBuergerPartner(buerger);

            refreshModelAssociations();
            showNotification(NotificationType.success, SimpleAction.add, rel);
        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
    }

    /**
     * Create a new Buerger on the DataStore.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void create(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");


        if (event.getData() instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            service.create(buerger);
        } else {
            throw new IllegalArgumentException("The event must not be of Class " + event.getData().getClass());
        }

        refreshModelList();
        notifyComponents();
        showNotification(NotificationType.success, SimpleAction.create);
    }


    /**
     * Delete the Buerger on the DataStore.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void delete(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            if (buerger.getId() == null)
                throw new IllegalArgumentException("The Buerger must have an ID.");
            service.delete(buerger.getId());

            getModel().getSelectedBuerger().ifPresent(selectedBuerger -> {
                if (selectedBuerger.equals(buerger)) {
                    getModel().setSelectedBuerger(null);
                    getModel().getSelectedBuergerKinder().clear();
                    getModel().getSelectedBuergerPartner().clear();
                }
            });

            refreshModelList();

        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
        showNotification(NotificationType.success, SimpleAction.delete);
    }

    /**
     * Update the Buerger on the DataStore.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} as {@link Event#getData()}.
     */
    private void update(Event<?> event) {
        final Object data = event.getData();
        if (data == null) throw new NullPointerException("Event data must not be null!");

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            if (buerger.getId() == null)
                throw new IllegalArgumentException("The Buerger must have an ID.");
            service.update(buerger);

            refreshModelSelected();
            refreshModelList();
        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
        showNotification(NotificationType.success, SimpleAction.update);
    }

    /**
     * Refresh the {@link BuergerModel#buerger} list from the DataStore.
     * <p/>
     * <p>
     * This method also filters by the query (ifPresent).
     * </p>
     */
    private void refreshModelList() {
        final Optional<String> query = getModel().getQuery();
        if (query.isPresent()) {
            getModel().setBuergers(queryBuerger(query.get()));
        } else {
            getModel().setBuergers(queryBuerger());
        }
    }

    /**
     * Refresh *all* the associations of the selected Buerger in the model.
     */
    private void refreshModelAssociations() {
        getModel().getSelectedBuerger().ifPresent(buerger -> {
            final List<Buerger> kinder = queryKinder(buerger);
            final List<Buerger> partner = Collections.singletonList(queryPartner(buerger));
            getModel().setSelectedBuergerKinder(kinder);
            getModel().setSelectedBuergerPartner(partner);
        });
    }

    /**
     * Refresh the current selected Buerger, but *not* its associations.
     */
    private void refreshModelSelected() {
        getModel().getSelectedBuerger().ifPresent(buerger -> getModel().setSelectedBuerger(service.findOne(buerger.getId()).orElse(null)));
    }

    /**
     * Set the query based on the String sent in the Event.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with a {@link String} query as {@link Event#getData()}.
     */
    private void readList(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof String) {
            final String filter = (String) event.getData();
            getModel().setQuery(filter);
        } else {
            getModel().setQuery(null);
        }

        refreshModelList();
        notifyComponents();
    }

    /**
     * Read the Buerger in the Event from the DataStore and set it as the current selected Buerger.
     * If called with null, the current selected Buerger will only be refreshed from the DataStore.
     * Update the Model and send it on the ResponseEntityKey if necessary.
     *
     * @param event The event with an {@link Buerger} or *null* as {@link Event#getData()}.
     */
    private void readSelected(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            getModel().setSelectedBuerger(buerger);
            refreshModelSelected();
            refreshModelAssociations();
        } else if (data == null) {
            refreshModelSelected();
            refreshModelAssociations();
        } else {
            throw new IllegalArgumentException("The event cannot be of Class " + event.getData().getClass());
        }

        notifyComponents();
    }

    /**
     * Notify all the Components.
     */
    public void notifyComponents() {
        eventbus.notify(getResponseKey(), Event.wrap(getModel()));
    }

    /**
     * Get the RequestEntityKey for this Entity.
     *
     * @param event The disered event the Key will have.
     * @return The RequestEntityKey with the chosen RequestEvent.
     */
    public RequestEntityKey getRequestKey(RequestEvent event) {
        return new RequestEntityKey(event, Buerger.class);
    }

    /**
     * Get the ResponseEntityKey for this Entity.
     *
     * @return The ResponseEntityKey.
     */
    public ResponseEntityKey getResponseKey() {
        return new ResponseEntityKey(Buerger.class);
    }

    private void showNotification(NotificationType type, SimpleAction action, Buerger.Rel relation) {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(type, action, Type.label, relation.name())),
                resolveRelative(getNotificationPath(type, action, Type.text, relation.name())));
        succes.show(Page.getCurrent());
    }

    private void showNotification(NotificationType type, SimpleAction action) {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(type, action, Type.label)),
                resolveRelative(getNotificationPath(type, action, Type.text)));
        succes.show(Page.getCurrent());
    }

    ////////////////////////
    // I18n Operations //
    ////////////////////////

    /**
     * Resolve the path (e.g. "asdf.label").
     *
     * @param path the path.
     * @return the resolved String.
     */
    @Override
    public String resolve(String path) {
        return msg.get(path);
    }

    /**
     * Resolve the relative path (e.g. "asdf.label").
     * <p/>
     * The base path will be appended at start and then read from the properties.
     *
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public String resolveRelative(String relativePath) {
        return msg.get(I18N_BASE_PATH + "." + relativePath);
    }

    @Override
    public String getBasePath() {
        return I18N_BASE_PATH;
    }

    /**
     * Resolve the relative path (e.g. ".asdf.label") to a icon.
     * <p/>
     * The base path will be appended at start and then read from the properties.
     *
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public FontAwesome resolveIcon(String relativePath) {
        return msg.getFontAwesome(I18N_BASE_PATH + "." + relativePath + ".icon");
    }
}
