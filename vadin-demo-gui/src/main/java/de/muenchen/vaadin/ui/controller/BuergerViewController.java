package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.BuergerHistoryView;
import de.muenchen.vaadin.ui.app.views.ChildSelectWindow;
import de.muenchen.vaadin.ui.app.views.PartnerSelectWindow;
import de.muenchen.vaadin.ui.app.views.BuergerTableView;
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
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

import static de.muenchen.vaadin.ui.util.I18nPaths.NotificationType;
import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getNotificationPath;
import static reactor.bus.selector.Selectors.T;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Buerger abzubilden.
 * 
 * @author claus.straube
 */
@SpringComponent @UIScope
public class BuergerViewController implements Serializable, ControllerContext<Buerger>, Consumer<Event<AppEvent<Buerger>>> {


    // TODO entweder hier oder im I18nServiceConfigImpl angeben
    public static final String I18N_BASE_PATH = "buerger";
    private static final long serialVersionUID = 1L;
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);
    private static final String PENDING_FROM = BuergerTableView.NAME;

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
    private BeanItem<Buerger> current;
    private List<Buerger> currentEntities;

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
        registerToAppEvent(this);
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

    public BeanItem<Buerger> getCurrent() {
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
        getEventbus().notify(event.getClass(), Event.wrap(event));
    }

    public void registerToAppEvent(Consumer consumer){
        getEventbus().on(T(AppEvent.class), consumer);
    }

    public void registerToComponentEvent(Consumer consumer){
        getEventbus().on(T(ComponentEvent.class),consumer);
    }


    @Override
    public AppEvent<Buerger> buildEvent(EventType eventType) {
        return new AppEvent<Buerger>(eventType);
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
     * Erstellt die Instanz eines {@link Buerger} Objektes. Dieses
     * Objekt wird noch nicht in der DB gespeichert.
     * 
     * @return neue Instanz einer Person
     */
    public Buerger createBuerger() {
        return this.service.createBuerger();
    }
    
    /**
     * Kopiert eine vorhandene Instanz eines {@link Buerger} Objektes in eine
     * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
     * 
     * @param entity Buerger der kopiert werden soll
     * @return kopierte Instanz einer Person
     */
    public Buerger copyBuerger(Buerger entity) {
        return this.service.copyBuerger(entity);
    }
    
    /**
     * Speichert ein {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Buerger der gespeichert werden soll
     * @return Buerger
     */
    public Buerger saveBuerger(Buerger entity) {
        return service.saveBuerger(entity);
    }
    
    /**
     * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Zu speicherndes Kind
     * @return Buerger
     */
    public Buerger saveBuergerKind(Buerger entity) {
        return service.saveKind(getCurrent().getBean(), entity);
    }

    /**
     * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
     *
     * @param entity Zu speicherndes Kind
     * @return Buerger
     */
    public Buerger saveBuergerPartner(Buerger entity) {
        return service.savePartner(getCurrent().getBean(), entity);
    }

    /**
     * Zerstört die Verbindung zwischen einem Bürger und seinem Kind
     *
     * @param event
     */
    private void releaseParent(AppEvent<Buerger> event) {
        service.releaseElternteil(getCurrent().getBean(), event.getEntity());
    }

    /**
     * Speichert eine Kindbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Kind
     * @return Buerger
     */
    public Buerger addBuergerKind(Buerger entity) {
        return service.addKind(getCurrent().getBean(), entity);
    }
    
    /**
     * Speichert eine Partnerschaftesbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
     *
     * @param entity Kind
     * @return Buerger
     */
    public Buerger addBuergerPartner(Buerger entity) {
        return service.addPartner(getCurrent().getBean(), entity);
    }

    /**
     * Speichert die Änderungen an einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity Buerger
     * @return Buerger
     */
    public Buerger updateBuerger(Buerger entity) {
        return service.updateBuerger(entity);
    }
    
    /**
     * Löscht ein {@link Buerger} Objekt.
     * 
     * @param entity Buerger
     */
    public void deleteBuerger(Buerger entity) {
        service.deleteBuerger(entity);
    }
    
    public List<Buerger> queryBuerger() {
        return service.queryBuerger();
    }
    
    public List<Buerger> queryBuerger(String query) {
        return service.queryBuerger(query);
    }
    
    public List<Buerger> queryKinder(Buerger entity) {
        return service.queryKinder(entity);
    }

    public List<Buerger> queryPartner(Buerger entity) {

        return service.queryPartner(entity);
    }

    public List<Buerger> queryHistory(Buerger entity) {

        return service.queryHistory(entity);
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

    public void accept(Event<AppEvent<Buerger>> eventWrapper) {
        AppEvent<Buerger> event = eventWrapper.getData();

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
                addSearchedChildEventHandler(event);
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
                addPartnerEventHandler(event);
                break;
            case QUERY_PARTNER:
                queryPartner(event.getEntity());
                break;
            default:
                LOG.debug("No matching handler found.");
        }

    }

    private void addPartnerEventHandler(AppEvent<Buerger> event) {
        navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateBuergerPartnerSearchTable(PENDING_FROM)));
        postEvent(new AppEvent<Buerger>(EventType.QUERY));
    }

    private void saveAsPartnerEventHandler(AppEvent<Buerger> event) {
        this.addBuergerPartner(event.getEntity());
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text)));
        succes.show(Page.getCurrent());
        postEvent(new AppEvent<Buerger>(event.getEntity(), EventType.UPDATE_PARTNER));
        navigateEventHandler(event);
    }

    private void historyHandler(BuergerAppEvent event) {
        this.eventbus.post(new BuergerComponentEvent(EventType.HISTORY).addEntities(this.queryHistory(event.getEntity())));
        this.current = event.getItem();

        // UI Komponente aktualisieren
        //this.eventbus.post(new BuergerComponentEvent(event.getItem().getBean(), EventType.HISTORY));

        // Verlauf protokollieren
        this.pushFrom(event);

        // Zur Seite wechseln
        navigator.navigateTo(BuergerHistoryView.NAME);
    }


    private void releaseParentHandler(AppEvent<Buerger> event) {
        // Service Operationen ausführen
        this.releaseParent(event);
        // UI Komponenten aktualisieren

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.release, Type.text)));
        succes.show(Page.getCurrent());

        ComponentEvent<Buerger> componentEvent = new ComponentEvent<Buerger>(EventType.DELETE);
        componentEvent.setItemID(event.getItemId());
        postEvent(componentEvent);

    }

    private void addSearchedChildEventHandler(AppEvent<Buerger> event){

        navigator.getUI().addWindow(new TableSelectWindow(this, getViewFactory().generateChildSearchTable(PENDING_FROM)));
        postEvent(new AppEvent<Buerger>(EventType.QUERY));
    }

    private void saveAsChildEventHandler(AppEvent<Buerger> event) {

        this.addBuergerKind(event.getEntity());
        GenericSuccessNotification succes = new GenericSuccessNotification(
                 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label)),
                 resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text)));
        succes.show(Page.getCurrent());
        postEvent(new ComponentEvent<Buerger>(event.getEntity(), EventType.UPDATE));
    }

    private void queryChildEventHandler(AppEvent<Buerger> event) {


        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(EventType.QUERY_CHILD).addEntities(this.queryKinder(event.getEntity())));
    }

    private void queryEventHandler(AppEvent<Buerger> event) {

        if(event.getQuery().isPresent()) {
            this.currentEntities = this.queryBuerger(event.getQuery().get());
        } else {
            this.currentEntities = this.queryBuerger();
        }

        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(EventType.QUERY).addEntities(this.currentEntities));
    }

    private void select2ReadEventHandler(AppEvent<Buerger> event) {

        this.current = event.getItem();

        // UI Komponente aktualisieren
        postEvent(new ComponentEvent<Buerger>(event.getItem().getBean(), EventType.SELECT2READ));


    }

    private void select2UpdateEventHandler(AppEvent<Buerger> event) {


        // Das ist notwendig, weil beim ersten Aufruf der UPDATE
        // Funktion erst die Komponente erstellt wird. Das Event
        // läuft also zuerst ins Leere und muss deshalb nochmal
        // wiederholt werden.
        this.current = event.getItem();

        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(event.getItem().getBean(), EventType.SELECT2UPDATE));

    }

    private void copyEventHandler(AppEvent<Buerger> event) {

        // Service Operationen ausführen
        Buerger copy = this.copyBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(copy, EventType.COPY));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void deleteEventHandler(AppEvent<Buerger> event) {

        // Service Operationen ausführen
        this.deleteBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        ComponentEvent<Buerger> componentEvent= new ComponentEvent<Buerger>(EventType.DELETE);
        componentEvent.setItemID(event.getItemId());
        postEvent(componentEvent);

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void saveChildEventHandler(AppEvent<Buerger> event) {

        // Service Operation ausführen
        this.saveBuergerKind(event.getEntity());

        postEvent(new ComponentEvent<Buerger>(event.getEntity(), EventType.SAVE_CHILD));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void savePartnerEventHandler(AppEvent<Buerger> event) {

        // Service Operation ausführen
        this.saveBuergerPartner(event.getEntity());

        postEvent(new ComponentEvent<Buerger>(event.getEntity(), EventType.SAVE_PARTNER));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());
    }

    private void saveEventHandler(AppEvent<Buerger> event) {

        // Service Operationen ausführen
        Buerger newBuerger = this.saveBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(newBuerger, EventType.SAVE));

        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
        succes.show(Page.getCurrent());

    }

    private void updateEventHandler(AppEvent<Buerger> event) {

        // Service Operationen ausführen
        this.updateBuerger(event.getEntity());

        // UI Komponenten aktualisieren
        postEvent(new ComponentEvent<Buerger>(event.getEntity(), EventType.UPDATE));


        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
                resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
        succes.show(Page.getCurrent());

    }

    public EventBus getBus(){
        return eventbus;
    }
}
