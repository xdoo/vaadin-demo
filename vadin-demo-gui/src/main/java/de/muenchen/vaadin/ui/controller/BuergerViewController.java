package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.BuergerCreateChildView;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import static de.muenchen.vaadin.ui.app.views.BuergerDetailView.NAME;

import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerCreateForm;
import de.muenchen.vaadin.ui.components.BuergerReadForm;
import de.muenchen.vaadin.ui.components.BuergerSearchTable;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.components.BuergerTableButtonFactory;
import de.muenchen.vaadin.ui.components.BuergerTableCopyButton;
import de.muenchen.vaadin.ui.components.BuergerTableDeleteButton;
import de.muenchen.vaadin.ui.components.BuergerTableDetailButton;
import de.muenchen.vaadin.ui.components.BuergerTableEditButton;
import de.muenchen.vaadin.ui.components.GenericSuccessNotification;
import de.muenchen.vaadin.ui.components.BuergerUpdateForm;
import de.muenchen.vaadin.ui.components.buttons.Action;
import de.muenchen.vaadin.ui.components.ChildTable;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;
import static de.muenchen.vaadin.ui.util.I18nPaths.*;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.controller;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Buerger abzubilden.
 * 
 * @author claus.straube
 */
@SpringComponent @UIScope
public class BuergerViewController implements Serializable,ControllerContext<Buerger> {
    
    // TODO entweder hier oder im I18nServiceConfigImpl angeben
    public static final String I18N_BASE_PATH = "buerger";
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);
    
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
    private final EventBus eventbus;
    
    private Optional<BuergerSearchTable> searchTable = Optional.<BuergerSearchTable>empty();
    private Optional<BuergerChildTab> childTab = Optional.empty();
    private Optional<BuergerCreateForm> createForm = Optional.empty();
    private Optional<BuergerCreateForm> createChildForm = Optional.empty();
    private Optional<BuergerUpdateForm> updateForm = Optional.empty();
    private Optional<BuergerReadForm> readForm = Optional.empty();
    /**
     * {@link MessageService} zur Auflösung der Platzhalter
     */
    @Autowired private MessageService msg;
    
    /**
     * {@link UI} {@link Navigator}
     */
    Navigator navigator;

    @Autowired
    public BuergerViewController(BuergerService service, VaadinUtil util, EventBus eventBus) {
        this.service = service;
        this.util = util;
        this.eventbus = eventBus;
        // controller im Session Event Bus registrieren
        this.eventbus.register(this);
    }
    
    // item cache
    private BeanItem<Buerger> current;
    private List<Buerger> currentEntities;
    
    // navigation cache
    private final Stack<String> from = new Stack<>();
    
    /**
     * Die Main wird über die {@link DefaulPersonView} registriert. Dies
     * ist notwendig, da die Vaadin abhängigen Beans zum Zeitpunkt der Instanzierung
     * noch nicht vorhanden sind. Aus der Main UI wird der {@link Navigator} geholt,
     * um über ihn eine Navigation zwischen einzelnen Views zu ermöglichen.
     * 
     * @param ui 
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
    public void postToEventBus(AppEvent<?> appEvent) {
        getEventbus().post(appEvent);
    }

    @Override
    public AppEvent<Buerger> buildEvent(EventType eventType) {
        return new BuergerAppEvent(eventType);
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
     * Erstellt die Instanz eines {@link Person} Objektes. Dieses
     * Objekt wird noch nicht in der DB gespeichert.
     * 
     * @return neue Instanz einer Person
     */
    public Buerger createBuerger() {
        Buerger entity = this.service.createBuerger();
        return entity;
    }
    
    /**
     * Kopiert eine vorhandene Instanz eines {@link Person} Objektes in eine
     * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
     * 
     * @param entity
     * @return kopierte Instanz einer Person
     */
    public Buerger copyBuerger(Buerger entity) {
        return this.service.copyBuerger(entity);
    }
    
    /**
     * Speichert ein {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity
     * @return 
     */
    public Buerger saveBuerger(Buerger entity) {
        return service.saveBuerger(entity);
    }
    
    /**
     * Speichert ein Kind zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity
     * @return 
     */
    public Buerger saveBuergerKind(Buerger entity) {
        return service.saveKind(this.current.getBean(), entity);
    }
    
    /**
     * Speichert die Änderungen an einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity
     * @return 
     */
    public Buerger updateBuerger(Buerger entity) {
        return service.updateBuerger(entity);
    }
    
    /**
     * Löscht ein {@link Buerger} Objekt.
     * 
     * @param entity 
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
    
    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        if(!createForm.isPresent()){
            
        LOG.debug("creating 'create' buerger form");
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo);
        createForm = Optional.of(form);
        }
        return createForm.get();
    }
    
    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        if(!createChildForm.isPresent()){
        LOG.debug("creating 'create child' buerger form");
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo);
        form.setType(EventType.SAVE_CHILD);
        createChildForm = Optional.of(form);
        }
        return createChildForm.get();
    }
    
    /**
     * Erzeugt eine neue Instanz eines "Child" Tabs.
     * 
     * @param navigateToForDetail Zielseite um sich die Details des 'Child' Objektes anzeigen zu lassen
     * @param navigateForCreate Zielseite um ein neues 'Child' Objekt zu erstellen
     * @param from Ausgangsseite zu der zurück navigiert werden soll
     * @return {@link TabSheet.Tab} das Tab
     */
    public BuergerChildTab generateChildTab(String navigateToForDetail, String navigateForCreate, String from) {
        if(childTab.isPresent()){
            BuergerTable tmp = childTab.get().getTable();
           // childTab.get().setTable(this.generateChildTable(navigateToForDetail, from));
           // eventbus.unregister(tmp);
        }
        else
        {
        BuergerChildTab tab = new BuergerChildTab(this, navigateToForDetail, navigateForCreate, from);
        this.eventbus.register(tab);
        childTab=Optional.of(tab);
        }
        return childTab.get();
    }
    
    public BuergerCreateForm generateCreateChildForm() {
        return this.generateCreateChildForm(this.popFrom());
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String from) {
        if(!updateForm.isPresent()){
        LOG.debug("creating 'update' buerger form");
        BuergerUpdateForm form = new BuergerUpdateForm(this, navigateTo, this.popFrom(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2UPDATE));
        updateForm = Optional.of(form);
        }
        return updateForm.get();
    }
    
    public BuergerUpdateForm generateUpdateForm(String from) { 
        return this.generateUpdateForm(this.peekFrom(), from);
    }
    
    public BuergerReadForm generateReadForm(String navigateToUpdate, String from) {
        if(!readForm.isPresent()){
        LOG.debug("creating 'read' buerger form");
        BuergerReadForm form = new BuergerReadForm(this, navigateToUpdate, this.peekFrom(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2READ));
        readForm = Optional.of(form);
        }
        return readForm.get();
    }
    
    public BuergerSearchTable generateSearchTable(String navigateToForEdit, String navigateToForDetail, String navigateForCreate, String navigateFrom) {
        
       
        if(!searchTable.isPresent()){
            BuergerTableButtonFactory detail = BuergerTableButtonFactory.getFactory(navigateToForDetail, BuergerTableDetailButton.class);
        BuergerTableButtonFactory edit = BuergerTableButtonFactory.getFactory(navigateToForEdit, BuergerTableEditButton.class);
        BuergerTableButtonFactory copy = BuergerTableButtonFactory.getFactory(null, BuergerTableCopyButton.class);
        BuergerTableButtonFactory delete = BuergerTableButtonFactory.getFactory(null, BuergerTableDeleteButton.class);
            LOG.debug("creating 'search' table for buerger");
        searchTable = Optional.of(new BuergerSearchTable(
                this, 
                navigateToForEdit, 
                navigateToForDetail, 
                navigateForCreate, 
                navigateFrom,
                // Schaltflächen
                detail,
                edit,
                copy,
                delete
        ));}
        return searchTable.get();
        
    }
    
    public BuergerTable generateChildTable(String navigateToForDetail, String from) {
        BuergerTableButtonFactory detail = BuergerTableButtonFactory.getFactory(navigateToForDetail, BuergerTableDetailButton.class);
        
        BuergerTable table = this.createChildTable(from, this.queryKinder(this.current.getBean()), detail);
        return table;
    }

    public BuergerTable generateTable(String navigateToForEdit, String navigateToForSelect, String from, final BuergerTableButtonFactory... buttonfactory) { 
        return this.createTable(from, this.queryBuerger(), buttonfactory);
    }
    
    private BuergerTable createTable(String from, List<Buerger> entities, final BuergerTableButtonFactory... buttonfactory) {
        LOG.debug("creating table for buerger");
        BuergerTable table = new BuergerTable(this, buttonfactory);
        
        table.setFrom(from);
        
        this.eventbus.register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.QUERY);
        event.addEntities(entities);
        this.eventbus.post(event);
        
        return table;
    }
    
    
    private BuergerTable createChildTable(String from, List<Buerger> entities, final BuergerTableButtonFactory... buttonfactory) {
        LOG.debug("creating table for buerger");
        BuergerTable table = new ChildTable(this, buttonfactory);
        
        table.setFrom(from);
        
        this.eventbus.register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.CHILDQUERY);
        event.addEntities(entities);
        this.eventbus.post(event);
        
        return table;
    }
    
    /////////////////////
    // Event Steuerung //
    /////////////////////
    
    /**
     * In dieser Methode wird zentral gesteuert, was bei bestimmten Ereignissen passiert.
     * Ausgehend von den Ereignissen werden innerhalb der UI Komponenten Operationen
     * ausgeführt. So ist es möglich eine Kommunnikation zwischen den Komponenten zu
     * schaffen, ohne dass diese sich untereinander kennen müssen. 
     * @param event
     */
    @Subscribe
    public void onEvent(BuergerAppEvent event) {
        
        // create
        if(event.getType().equals(EventType.CREATE)) {
            LOG.debug("create event");
            
            // Verlauf protokollieren
            this.pushFrom(event);
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        } 
        
        // update
        if(event.getType().equals(EventType.UPDATE)) {
            LOG.debug("update event");
            // Service Operationen ausführen
            this.updateBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(event.getEntity(), EventType.UPDATE));
            
            // Verlauf protokollieren
            this.pushFrom(event);
            
            GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, Action.update, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, Action.update, Type.text)));
            succes.show(Page.getCurrent());
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // save
        if(event.getType().equals(EventType.SAVE)) {
            LOG.debug("save event");
            // Service Operationen ausführen
            this.saveBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(event.getEntity(), EventType.SAVE));
            
            GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, Action.save, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, Action.save, Type.text)));
            succes.show(Page.getCurrent());
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // save child
        if(event.getType().equals(EventType.SAVE_CHILD)) {
            LOG.debug("save child event");
            // Service Operation ausführen
            this.saveBuergerKind(event.getEntity());
            
            GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, Action.save, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, Action.save, Type.text)));
            succes.show(Page.getCurrent());
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
            
        }
        
        // delete
        if(event.getType().equals(EventType.DELETE)) {
            LOG.debug("delete event");
            // Service Operationen ausführen
            this.deleteBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            BuergerComponentEvent buergerComponentEvent = new BuergerComponentEvent(EventType.DELETE);
            buergerComponentEvent.setItemID(event.getItemId());
            this.eventbus.post(buergerComponentEvent);
            
            GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, Action.delete, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, Action.delete, Type.text)));
            succes.show(Page.getCurrent());
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Buerger copy = this.copyBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(copy, EventType.COPY));
            
            GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, Action.copy, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, Action.copy, Type.text)));
            succes.show(Page.getCurrent());
        }
        
        // update um den Bürger zu bearbeiten
        if(event.getType().equals(EventType.SELECT2UPDATE)) {
            LOG.debug("select to update event");
            
            // Das ist notwendig, weil beim ersten Aufruf der UPDATE
            // Funktion erst die Komponente erstellt wird. Das Event
            // läuft also zuerst ins Leere und muss deshalb nochmal 
            // wiederholt werden.
            this.current = event.getItem();
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(event.getItem().getBean(), EventType.SELECT2UPDATE));
            
            // Verlauf protokollieren
            this.pushFrom(event);
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // update um den Bürger anzusehen
        if(event.getType().equals(EventType.SELECT2READ)) {
            LOG.debug("select to read event");
            //eventbus.unregister(searchTable.get().getTable());
            this.current = event.getItem();
            
            // UI Komponente aktualisieren
            this.eventbus.post(new BuergerComponentEvent(event.getItem().getBean(), EventType.SELECT2READ));
            
            // Verlauf protokollieren
            this.pushFrom(event);
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // query
        if(event.getType().equals(EventType.QUERY)) {
            LOG.debug("query event");
            if(event.getQuery().isPresent()) {
                this.currentEntities = this.queryBuerger(event.getQuery().get());
            } else {
                this.currentEntities = this.queryBuerger();
            }
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(EventType.QUERY).addEntities(this.currentEntities));
        }
        
        if(event.getType().equals(EventType.CHILDQUERY)) {
            LOG.debug("query event");
            if(event.getQuery().isPresent()) {
                this.currentEntities = this.queryBuerger(event.getQuery().get());
            } else {
                this.currentEntities = this.queryBuerger();
            }
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(EventType.CHILDQUERY).addEntities(this.currentEntities));
        }
        
        if(event.getType().equals(EventType.QUERY_CHILD)) {
            LOG.debug("query child event");
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(EventType.CHILDQUERY).addEntities(this.queryKinder(event.getEntity())));
        }
        
        // cancel
        if(event.getType().equals(EventType.CANCEL)) {
            LOG.debug("cancel event");
            // Zur Seite wechseln
            //eventbus.register(searchTable.get().getTable());
            this.navigator.navigateTo(event.getNavigateTo()); 
        }
    }
    
    private void pushFrom(BuergerAppEvent event) {
        if(event.getFrom().isPresent()) {
            this.from.push(event.getFrom().get());
        }
    }
    
    private String popFrom() {
        return this.from.pop();
    }
    
    private String peekFrom() {
        return this.from.peek();
    }
    public EventBus getBus(){
        return eventbus;
    }
}
