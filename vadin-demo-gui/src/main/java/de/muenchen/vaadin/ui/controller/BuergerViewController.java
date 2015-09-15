package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.BuergerHistoryView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.GenericSuccessNotification;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.factorys.BuergerViewFactory;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.muenchen.vaadin.ui.util.I18nPaths.NotificationType;
import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getNotificationPath;
import static reactor.bus.selector.Selectors.object;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Buerger abzubilden.
 * 
 * @author claus.straube
 */
@SpringComponent @UIScope
public class BuergerViewController implements Serializable, ControllerContext<Buerger>, Consumer<Event<AppEvent<LocalBuerger>>> {


    // TODO entweder hier oder im I18nServiceConfigImpl angeben
    public static final String I18N_BASE_PATH = "buerger";
    private static final long serialVersionUID = 1L;
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);

    /**
     * Die Service Klasse
     */
    private final BuergerService service;
    
    /**
     * Werkzeuge für Vaadin
     */
    private final VaadinUtil util;
    
    /**
     * Event Bus zur Kommunikation
     */
    @Autowired
    private EventBus eventbus;

    /**
     * {@link MessageService} zur Auflösung der Platzhalter
     */
    @Autowired private MessageService msg;
    /**
     * {@link UI} {@link Navigator}
     */
    private Navigator navigator;
    /** BuergerViewFactory zum erstellen der Components **/
    @Autowired
    private BuergerViewFactory buergerViewFactory;
    // item cache
    private BeanItem<LocalBuerger> current;

    @Autowired
    public BuergerViewController(BuergerService service, VaadinUtil util) {
        this.service = service;
        this.util = util;
    }

    @PostConstruct
    public void init() {
        //Set Controller in Factory after Contruct.
        //to prevent circular reference
        buergerViewFactory.setController(this);
        registerToAllAppEvents(this);
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

    public BeanItem<LocalBuerger> getCurrent() {
        return current;
    }

    public BuergerViewFactory getViewFactory() {
        return buergerViewFactory;
    }


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
     *
     * The base path will be appended at start and then read from the properties.
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public String resolveRelative(String relativePath) {
        return msg.get(I18N_BASE_PATH + "." + relativePath);
    }

    @Override
    public void postEvent(Object event) {
        getEventbus().notify(event, Event.wrap(event));
    }

    public void registerToAppEvent(EventType type,Consumer consumer) {
        getEventbus().on(object(buildAppEvent(type)), consumer);
    }

    public void registerToAllAppEvents(Consumer consumer) {
        Stream.of(EventType.values()).forEach(type -> registerToAppEvent(type, consumer));
    }

    public void registerToComponentEvent(EventType type,Consumer consumer) {
        getEventbus().on(object(buildComponentEvent(type)), consumer);
    }

    public void registerToAllComponentEvents(Consumer consumer) {
        Stream.of(EventType.values()).forEach(type -> registerToComponentEvent(type, consumer));
    }

    @Override
    public AppEvent<LocalBuerger> buildAppEvent(EventType eventType) {
        return new AppEvent<LocalBuerger>(LocalBuerger.class, eventType);
    }

    @Override
    public ComponentEvent<LocalBuerger> buildComponentEvent(EventType eventType) {
        return new ComponentEvent<>(LocalBuerger.class, eventType);
    }

    @Override
    public String getBasePath() {
        return I18N_BASE_PATH;
    }

    /**
     * Resolve the relative path (e.g. ".asdf.label") to a icon.
     *
     * The base path will be appended at start and then read from the properties.
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    public FontAwesome resolveIcon(String relativePath) {
        return msg.getFontAwesome(I18N_BASE_PATH + "." + relativePath + ".icon");
    }
    
    ////////////////////////
    // Service Operations //
    ////////////////////////

    /**
     * Kopiert eine vorhandene Instanz eines {@link Buerger} Objektes in eine
     * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
     * 
     * @param id Buerger der kopiert werden soll
     */
    public LocalBuerger copyBuerger(Link id) {
        return service.copy(id);
    }
    
    /**
     * Speichert ein {@link Buerger} Objekt in der Datenbank.
     *
     * @param buerger Buerger der gespeichert werden soll
     */
    public LocalBuerger save(LocalBuerger buerger) {
        return service.create(buerger);
    }
    
    /**
     * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Zu speicherndes Kind
     * @return Buerger
     */
    public void saveBuergerKind(LocalBuerger entity) {
        save(entity);
        addBuergerKind(entity);
    }

    /**
     * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
     *
     * @param entity Zu speicherndes Kind
     * @return Buerger
     */
    public void saveBuergerPartner(LocalBuerger entity) {
        save(entity);
        addBuergerPartner(entity);
    }

    /**
     * Zerstört die Verbindung zwischen einem Bürger und seinem Kind
     *
     * @param event
     */
    private void releaseParent(AppEvent<LocalBuerger> event) {
        //service.releaseElternteil(getCurrent().getBean(), event.getEntity());
    }

    /**
     * Speichert eine Kindbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Kind
     * @return Buerger
     */
    public void addBuergerKind(LocalBuerger entity) {
        Link link = entity.getLink(BuergerResource.Rel.kinder.name());
        List<Link> kinder = Stream.concat(
                service.findAll(link)
                        .stream()
                        .map(LocalBuerger::getId),
                Stream.of(entity.getId()))

                .collect(Collectors.toList());

        service.setRelations(entity.getId(), kinder);
    }
    
    /**
     * Speichert eine Partnerschaftesbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
     *
     * @param entity Kind
     * @return Buerger
     */
    public void addBuergerPartner(LocalBuerger entity) {
        Link link = entity.getLink(BuergerResource.Rel.kinder.name());
        List<Link> kinder = Stream.concat(
                service.findAll(link)
                        .stream()
                        .map(LocalBuerger::getId),
                Stream.of(entity.getId()))

                .collect(Collectors.toList());

        service.setRelations(entity.getId(), kinder);
    }

    /**
     * Speichert die Änderungen an einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Buerger
     * @return Buerger
     */
    public LocalBuerger updateBuerger(LocalBuerger entity) {
        return service.update(entity);
    }
    
    /**
     * Löscht ein {@link Buerger} Objekt.
     * 
     * @param entity Buerger
     */
    public void deleteBuerger(LocalBuerger entity) {
        service.delete(entity.getId());
    }

    public List<LocalBuerger> queryBuerger() {
        return service.findAll().stream().collect(Collectors.toList());
    }

    public List<LocalBuerger> queryBuerger(String query) {
        return queryBuerger(); //TODO
    }

    public List<LocalBuerger> queryKinder(LocalBuerger entity) {
        return service.findAll(entity.getLink(BuergerResource.Rel.kinder.name())).stream().collect(Collectors.toList());
    }

    public List<LocalBuerger> queryPartner(LocalBuerger entity) {

        return service.findAll(entity.getLink(BuergerResource.Rel.partner.name())).stream().collect(Collectors.toList());
    }

    public List<LocalBuerger> queryHistory(LocalBuerger entity) {
        return new ArrayList<>(); //TODO
    }
    
    /////////////////////
    // Event Steuerung //
    /////////////////////
    
    /**
     * In dieser Methode wird zentral gesteuert, was bei bestimmten Ereignissen passiert.
     * Ausgehend von den Ereignissen werden innerhalb der UI Komponenten Operationen
     * ausgeführt. So ist es möglich eine Kommunnikation zwischen den Komponenten zu
     * schaffen, ohne dass diese sich untereinander kennen müssen. 
     * @param eventWrapper AppEvent<Buerger>
     */

    public void accept(Event<AppEvent<LocalBuerger>> eventWrapper) {
        AppEvent<LocalBuerger> event = eventWrapper.getData();

        LOG.debug("Event handled: " + event.getType());

        switch (event.getType()) {
            case CREATE:
                //TODO
                break;
            case UPDATE:
                updateEventHandler(event);
                break;
            case SAVE:
                saveEventHandler(event);
                break;
            case DELETE:
                deleteEventHandler(event);
                break;
            case SELECT2UPDATE:
                select2UpdateEventHandler(event);
                break;
            case SELECT2READ:
                select2ReadEventHandler(event);
                break;
            case COPY:
                copyEventHandler(event);
                break;
            case QUERY:
                queryEventHandler(event);
                break;
            case QUERY_CHILD:
                queryChildEventHandler(event);
                break;
            case SAVE_CHILD:
                saveChildEventHandler(event);
                break;
            case SAVE_PARTNER:
                savePartnerEventHandler(event);
                break;
            case SAVE_AS_CHILD:
                saveAsChildEventHandler(event);
                break;
            case ADD_SEARCHED_CHILD:
                addSearchedChildEventHandler();
                break;
            case RELEASE_PARENT:
                releaseParentHandler(event);
                break;
            case HISTORY:
                historyHandler(event);
                break;
            case SAVE_AS_PARTNER:
                saveAsPartnerEventHandler(event);
                break;
            case ADD_PARTNER:
                addPartnerEventHandler();
                break;
            case QUERY_PARTNER:
                queryPartner(event.getEntity());
                break;
            default:
                LOG.debug("No matching handler found.");
        }

    }

    private void addPartnerEventHandler() {
        navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateBuergerPartnerSearchTable()));
        postEvent(buildAppEvent(EventType.QUERY));
    }

    private void saveAsPartnerEventHandler(AppEvent<LocalBuerger> event) {
        this.addBuergerPartner(event.getEntity());
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label,"partner")),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text,"partner")));
        succes.show(Page.getCurrent());
        postEvent(buildComponentEvent(EventType.UPDATE_PARTNER).addEntity(event.getEntity()));
    }

    private void historyHandler(AppEvent<LocalBuerger> event) {
        postEvent(buildComponentEvent(EventType.HISTORY).addEntities(this.queryHistory(event.getEntity())));
        this.current = event.getItem();

        // UI Komponente aktualisieren
        //this.eventbus.post(new BuergerComponentEvent(event.getItem().getBean(), EventType.HISTORY));

        // Zur Seite wechseln
        navigator.navigateTo(BuergerHistoryView.NAME);
    }


    private void releaseParentHandler(AppEvent<LocalBuerger> event) {
        // Service Operationen ausführen
        this.releaseParent(event);
        // UI Komponenten aktualisieren

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label,"child")),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text,"child")));
        succes.show(Page.getCurrent());

        postEvent(buildComponentEvent(EventType.DELETE).setItemID(event.getItemId()));

    }

    private void addSearchedChildEventHandler() {

        navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateChildSearchTable()));
        postEvent(buildAppEvent(EventType.QUERY));
    }

    private void saveAsChildEventHandler(AppEvent<LocalBuerger> event) {

        this.addBuergerKind(event.getEntity());
        GenericSuccessNotification succes = new GenericSuccessNotification(
                 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label, "child")),
                 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text, "child")));
        succes.show(Page.getCurrent());
        postEvent(buildComponentEvent(EventType.UPDATE_CHILD).addEntity(event.getEntity()));
    }

    private void queryChildEventHandler(AppEvent<LocalBuerger> event) {


        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.QUERY_CHILD).addEntities(this.queryKinder(event.getEntity())));
    }

    private void queryEventHandler(AppEvent<LocalBuerger> event) {

        List<LocalBuerger> currentEntities;
        if(event.getQuery().isPresent()) {
            currentEntities = this.queryBuerger(event.getQuery().get());
        } else {
            currentEntities = this.queryBuerger();
        }

        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.QUERY).addEntities(currentEntities));
    }

    private void select2ReadEventHandler(AppEvent<LocalBuerger> event) {

        this.current = event.getItem();

        // UI Komponente aktualisieren
        postEvent(buildComponentEvent(EventType.SELECT2READ).addEntity(event.getItem().getBean()));


    }

    private void select2UpdateEventHandler(AppEvent<LocalBuerger> event) {


        // Das ist notwendig, weil beim ersten Aufruf der UPDATE
        // Funktion erst die Komponente erstellt wird. Das Event
        // läuft also zuerst ins Leere und muss deshalb nochmal
        // wiederholt werden.
        this.current = event.getItem();

        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.SELECT2UPDATE).addEntity(event.getItem().getBean()));

    }

    private void copyEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operationen ausführen
        LocalBuerger copy = this.copyBuerger(event.getEntity().getId());

        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.COPY).addEntity(copy));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void deleteEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operationen ausführen
        this.deleteBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        ComponentEvent<LocalBuerger> componentEvent = buildComponentEvent(EventType.DELETE);
        componentEvent.setItemID(event.getItemId());
        postEvent(componentEvent);

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void saveChildEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operation ausführen
        this.saveBuergerKind(event.getEntity());

        postEvent(buildComponentEvent(EventType.SAVE_CHILD).addEntity(event.getEntity()));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void savePartnerEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operation ausführen
        this.saveBuergerPartner(event.getEntity());

        postEvent(buildComponentEvent(EventType.SAVE_PARTNER).addEntity(event.getEntity()));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void saveEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operationen ausführen
        LocalBuerger newBuerger = this.save(event.getEntity());

        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.SAVE).addEntity(newBuerger));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());

    }

    private void updateEventHandler(AppEvent<LocalBuerger> event) {

        // Service Operationen ausführen
        this.updateBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        postEvent(buildComponentEvent(EventType.UPDATE).addEntity(event.getEntity()));


        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
        succes.show(Page.getCurrent());

    }

    public EventBus getBus(){
        return eventbus;
    }
}
