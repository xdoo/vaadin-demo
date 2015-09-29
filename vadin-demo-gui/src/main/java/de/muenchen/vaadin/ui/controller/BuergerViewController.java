package de.muenchen.vaadin.ui.controller;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import de.muenchen.eventbus.Util.Association;
import de.muenchen.eventbus.selector.RequestKey;
import de.muenchen.eventbus.selector.ResponseKey;
import de.muenchen.eventbus.types.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.vaadin.guilib.services.MessageService;
import de.muenchen.vaadin.guilib.util.VaadinUtil;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.model.BuergerModel;
import de.muenchen.vaadin.services.model.BuergerReadOnlyModel;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.factorys.BuergerViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;
import static reactor.bus.selector.Selectors.$;

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

    public BuergerReadOnlyModel getReadOnlyModel() {
        return model;
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

    public void addBuergerKind(Buerger kindEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.kinder.name());
        List<Link> kinder = Stream.concat(
                service.findAll(link)
                        .stream()
                        .map(Buerger::getId),
                Stream.of(kindEntity.getId()))

                .collect(Collectors.toList());

        service.setRelations(link, kinder);
    }

    public void setBuergerPartner(Buerger partnerEntity) {
        Link link = getModel().getSelectedBuerger().get().getLink(Buerger.Rel.partner.name());
        service.setRelation(link, partnerEntity.getId());
    }

    public List<Buerger> queryBuerger() {
        return service.findAll().stream().collect(Collectors.toList());
    }

    public List<Buerger> queryBuerger(String query) {
        return queryBuerger(); //TODO
    }

    public List<Buerger> queryKinder(Buerger entity) {
        return service.findAll(entity.getLink(Buerger.Rel.kinder.name())).stream().collect(Collectors.toList());
    }

    public Buerger queryPartner(Buerger entity) {
        return service.findOne(entity.getLink(Buerger.Rel.partner.name())).orElse(null);
    }


    /////////////////////
    // Event Steuerung //
    /////////////////////

    public void initEventhandlers() {
        eventbus.on($(getRequestKey(RequestEvent.CREATE)), this::create);
        eventbus.on($(getRequestKey(RequestEvent.DELETE)), this::delete);
        eventbus.on($(getRequestKey(RequestEvent.UPDATE)), this::update);
        eventbus.on($(getRequestKey(RequestEvent.READ_LIST)), this::readList);
        eventbus.on($(getRequestKey(RequestEvent.READ_SELECTED)), this::readSelected);
    }

    private void create(Event<?> event) {
        if (event.getData() instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            service.create(buerger);
        } else {
            throw new AssertionError();
        }

        refreshModelList();
        notifyComponents();
    }

    private void delete(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            service.delete(buerger.getId());

            getModel().getSelectedBuerger().ifPresent(selectedBuerger -> {
                if (selectedBuerger.equals(buerger)) {
                    getModel().setSelectedBuerger(null);
                    getModel().getSelectedBuergerAssociations().clear();
                }
            });

            refreshModelList();

        } else if (data instanceof Association) {
            @SuppressWarnings("unchecked") final Association<Buerger> association = (Association<Buerger>) event.getData();
            if (Buerger.Rel.kinder.name().equals(association.getRel()))
                releaseKind(association.getAssociation());
            if (Buerger.Rel.partner.name().equals(association.getRel()))
                releasePartner(association.getAssociation());

            refreshModelAssociations();
        } else {
            throw new AssertionError();
        }

        notifyComponents();
    }

    //TODO Assoziation nur hinzufügen können?
    private void update(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof Buerger) {
            //TODO update Buerger
            final Buerger buerger = (Buerger) event.getData();
            service.update(buerger);

            refreshModelSelected();
        } else if (data instanceof Association) {
            @SuppressWarnings("unchecked") final Association<Buerger> association = (Association<Buerger>) event.getData();
            if (Buerger.Rel.kinder.name().equals(association.getRel()))
                addBuergerKind(association.getAssociation());
            if (Buerger.Rel.partner.name().equals(association.getRel()))
                setBuergerPartner(association.getAssociation());

            refreshModelAssociations();
        } else {
            throw new AssertionError();
        }

        refreshModelList();
        notifyComponents();
    }

    private void refreshModelList() {
        final Optional<String> query = getModel().getQuery();
        if (query.isPresent()) {
            getModel().setBuerger(queryBuerger(query.get()));
        } else {
            getModel().setBuerger(queryBuerger());
        }
    }

    private void refreshModelAssociations() {
        getModel().getSelectedBuerger().ifPresent(buerger -> {
            final Map<String, List<Buerger>> associations = new HashMap<>();
            associations.put(Buerger.Rel.kinder.name(), queryKinder(buerger));
            associations.put(Buerger.Rel.partner.name(), Collections.singletonList(queryPartner(buerger)));
            getModel().setSelectedBuergerAssociations(associations);
        });
    }

    private void refreshModelSelected() {
        getModel().getSelectedBuerger().ifPresent(buerger -> {
            getModel().setSelectedBuerger(service.findOne(buerger.getId()).orElse(null));
        });
    }

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

    private void readSelected(Event<?> event) {
        final Object data = event.getData();

        if (data instanceof Buerger) {
            final Buerger buerger = (Buerger) event.getData();
            getModel().setSelectedBuerger(buerger);
            refreshModelSelected();
            refreshModelAssociations();
        } else if(data==null){
            refreshModelSelected();
            refreshModelAssociations();
        } else {
            throw new AssertionError();
        }

        notifyComponents();
    }

    public void notifyComponents() {
        eventbus.notify(getResponseKey(), Event.wrap(getModel()));
    }

    public RequestKey getRequestKey(RequestEvent event) {
        return new RequestKey(event, Buerger.class);
    }

    public ResponseKey getResponseKey() {
        return new ResponseKey(Buerger.class);
    }

    private void showNotification(NotificationType type, SimpleAction action, Buerger.Rel relation) {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(type, action, Type.label, relation.name())),
                resolveRelative(getNotificationPath(type, action, Type.text, relation.name())));
        succes.show(Page.getCurrent());
    }

/**
 private void releasePartnerHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();
 this.releasePartner(event.getEntity());
 // UI Komponenten aktualisieren

 showNotification(NotificationType.success, SimpleAction.release, Buerger.Rel.partner);

 postEvent(buildComponentEvent(EventType.DELETE_BUERGER).setItemID(event.getEntity()));
 }

 private void addPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateBuergerPartnerSearchTable()));
 postEvent(buildAppEvent(EventType.QUERY_BUERGER));
 }

 private void saveAsPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();
 this.setBuergerPartner(event.getEntity());
 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "partner")),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "partner")));
 succes.show(Page.getCurrent());
 postEvent(buildComponentEvent(EventType.SAVE_AS_PARTNER).addEntity(event.getEntity()));
 }

 private void releaseKindHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();
 // Service Operationen ausführen
 this.releaseKind(event.getEntity());
 // UI Komponenten aktualisieren

 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label, "child")),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text, "child")));
 succes.show(Page.getCurrent());

 postEvent(buildComponentEvent(EventType.DELETE_BUERGER).setItemID(event.getEntity()));

 }

 private void addSearchedChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateChildSearchTable()));
 postEvent(buildAppEvent(EventType.QUERY_BUERGER));
 }

 private void saveAsChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 this.addBuergerKind(event.getEntity());
 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "child")),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "child")));
 succes.show(Page.getCurrent());
 postEvent(buildComponentEvent(EventType.SAVE_CHILD).addEntity(event.getEntity()));
 }

 private void queryChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.QUERY_CHILD).addEntities(this.queryKinder(event.getEntity())));
 }

 private void queryEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 List<Buerger> currentEntities;
 if (event.getQuery().isPresent()) {
 currentEntities = this.queryBuerger(event.getQuery().get());
 } else {
 currentEntities = this.queryBuerger();
 }

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.QUERY_BUERGER).addEntities(currentEntities));
 }

 private void select2ReadEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 this.current = event.getItem();

 // UI Komponente aktualisieren
 postEvent(buildComponentEvent(EventType.SELECT_TO_READ).addEntity(event.getItem().getBean()));


 }

 private void select2UpdateEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Das ist notwendig, weil beim ersten Aufruf der UPDATE
 // Funktion erst die Komponente erstellt wird. Das Event
 // läuft also zuerst ins Leere und muss deshalb nochmal
 // wiederholt werden.
 this.current = event.getItem();

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.SELECT_TO_EDIT).addEntity(event.getItem().getBean()));

 }

 private void copyEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Service Operationen ausführen
 Buerger copy = this.copyBuerger(event.getEntity().getId());

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.COPY_BUERGER).addEntity(copy));

 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
 succes.show(Page.getCurrent());
 }

 private void deleteEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 Notification resultNotification;

 // Service Operationen ausführen
 try {
 this.deleteBuerger(event.getEntity());
 // UI Komponenten aktualisieren
 ComponentEvent<Buerger> componentEvent = buildComponentEvent(EventType.DELETE_BUERGER);
 componentEvent.setItemID(event.getEntity());
 postEvent(componentEvent);
 resultNotification = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
 } catch (HttpClientErrorException e) {
 LOG.warn("Exception: " + e.getMessage());
 if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
 resultNotification = new GenericFailureNotification(
 resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.text)));
 } else {
 resultNotification = new GenericFailureNotification(
 resolveRelative(getNotificationPath(NotificationType.failure, SimpleAction.delete, Type.label)),
 e.getLocalizedMessage()
 );
 }
 }

 resultNotification.show(Page.getCurrent());
 }

 private void saveChildEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Service Operation ausführen
 this.saveBuergerKind(event.getEntity());

 postEvent(buildComponentEvent(EventType.SAVE_AND_ADD_CHILD).addEntity(event.getEntity()));

 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
 succes.show(Page.getCurrent());
 }

 private void savePartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Service Operation ausführen
 this.saveBuergerPartner(event.getEntity());

 postEvent(buildComponentEvent(EventType.SAVE_PARTNER).addEntity(event.getEntity()));

 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
 succes.show(Page.getCurrent());
 }

 private void saveEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Service Operationen ausführen
 Buerger newBuerger = this.save(event.getEntity());

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.SAVE_BUERGER).addEntity(newBuerger));

 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
 succes.show(Page.getCurrent());

 }

 private void updateEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();

 // Service Operationen ausführen
 this.updateBuerger(event.getEntity());

 // UI Komponenten aktualisieren
 postEvent(buildComponentEvent(EventType.UPDATE_BUERGER).addEntity(event.getEntity()));


 GenericSuccessNotification succes = new GenericSuccessNotification(
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
 succes.show(Page.getCurrent());

 }

 private void queryPartnerEventHandler(Event<AppEvent<Buerger>> eventWrapper) {
 AppEvent<Buerger> event = eventWrapper.getData();
 // UI Komponenten aktualisieren
 Buerger partner;
 try {
 partner = this.queryPartner(event.getEntity());
 postEvent(buildComponentEvent(EventType.QUERY_PARTNER).addEntity(partner));
 } catch (HttpClientErrorException e) {
 if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
 postEvent(buildComponentEvent(EventType.QUERY_PARTNER).addEntities(new ArrayList<>()));
 }
 }
 }*/

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
     * <p>
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
     * <p>
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
