package de.muenchen.vaadin.ui.controller;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.*;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.components.buttons.TableAction;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

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
    private Optional<ChildSearchTable> childSearchTable = Optional.empty();
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
     * Speichert eine Kindbeziehung zu einem {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity
     * @return 
     */
    public Buerger addBuergerKind(Buerger entity) {
        return service.addKind(this.current.getBean(), entity);
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
        LOG.debug("creating 'create' buerger form");
        if(!createForm.isPresent()){
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo, EventType.SAVE);
        createForm = Optional.of(form);}
        return createForm.get();
    }
    
    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        if(!createChildForm.isPresent()){
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo, EventType.SAVE_CHILD);
        createChildForm=Optional.of(form);}
        return createChildForm.get();
    }
    
    /**
     * Erzeugt eine neue Instanz eines "Child" Tabs.
     * 
     * @param navigateToForDetail Zielseite um sich die Details des 'Child' Objektes anzeigen zu lassen
     * @param navigateForCreate Zielseite um ein neues 'Child' Objekt zu erstellen
     * @param navigateForAdd Zielseite zum Hinzufügen eines Kindes
     * @param from Ausgangsseite zu der zurück navigiert werden soll
     * @return {@link TabSheet.Tab} das Tab
     */
    public BuergerChildTab generateChildTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String from) {
        if(!childTab.isPresent())
        {
        BuergerChildTab tab = new BuergerChildTab(this, navigateToForDetail, navigateForCreate, navigateForAdd, from);
        this.eventbus.register(tab);
        childTab = Optional.of(tab);}
        return childTab.get();
    }
    
    /**
     *
     * @return
     */
    public BuergerCreateForm generateCreateChildForm() {
        return this.generateCreateChildForm(this.popFrom());
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String from) { 
        LOG.debug("creating 'update' buerger form");
        if(!updateForm.isPresent()){
        BuergerUpdateForm form = new BuergerUpdateForm(this, navigateTo, this.popFrom(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2UPDATE));
        
        updateForm=Optional.of(form);}
        return updateForm.get();
    }
    
    public BuergerUpdateForm generateUpdateForm(String from) {
        
        return this.generateUpdateForm(this.peekFrom(), from);
    }
    
    public BuergerReadForm generateReadForm(String navigateToUpdate, String from) {
        LOG.debug("creating 'read' buerger form");
        if(!readForm.isPresent()){
        BuergerReadForm form = new BuergerReadForm(this, navigateToUpdate, this.peekFrom(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2READ));
        readForm=Optional.of(form);}
        return readForm.get();
    }
    
    public BuergerSearchTable generateSearchTable(String navigateToForEdit, String navigateToForDetail, String navigateForCreate, String navigateFrom) {
        LOG.debug("creating 'search' table for buerger");
        if(!searchTable.isPresent()){
        LOG.debug("new searchtabel");
        TableActionButton.Builder detail = TableActionButton.Builder.make(this, TableAction.tabledetail,navigateToForDetail,(container,id) -> {
            getEventbus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2READ).navigateTo(navigateToForDetail).from(navigateFrom));
        });
        TableActionButton.Builder edit = TableActionButton.Builder.make(this, TableAction.tableedit, navigateToForEdit, (container, id) -> {
           getEventbus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2UPDATE).navigateTo(navigateToForEdit).from(navigateFrom));
        });
        TableActionButton.Builder copy = TableActionButton.Builder.make(this, TableAction.tablecopy,null, (container, id) -> {
            getEventbus().post(new BuergerAppEvent(container.getItem(id),id,EventType.COPY));
        });
        TableActionButton.Builder delete = TableActionButton.Builder.make(this, TableAction.tabledelete,navigateToForEdit, (container, id) -> {
            getEventbus().post(new BuergerAppEvent(container.getItem(id),id,EventType.DELETE).navigateTo(navigateToForEdit).from(navigateFrom));
        });

       searchTable=Optional.of(new BuergerSearchTable(
                this,
                navigateForCreate,
                navigateFrom,
                // Schaltflächen
                detail,
                edit,
                copy,
                delete
        ));}
        return searchTable.get();}
        
    
    
    public ChildSearchTable generateChildSearchTable(String navigateToForEdit, String navigateToForDetail, String navigateForCreate, String navigateFrom) {
        
       
        if(!childSearchTable.isPresent()){
            //BuergerTableButtonFactory detail = BuergerTableButtonFactory.getFactory(navigateToForDetail, BuergerTableDetailButton.class);
        TableActionButton.Builder select = TableActionButton.Builder.make(this, TableAction.tableadd,navigateToForEdit, (container, id) -> {
            getEventbus().post(new BuergerAppEvent(container.getItem(id),id,EventType.SAVE_AS_CHILD).navigateTo(navigateToForDetail).from(navigateFrom));
        });
            LOG.debug("creating 'search' table for buerger");
        childSearchTable = Optional.of(new ChildSearchTable(
                this, 
                navigateToForEdit, 
                navigateToForDetail, 
                navigateForCreate, 
                navigateFrom,
                // Schaltflächen
                select
        ));}
        return childSearchTable.get();
        
    }
    
    public BuergerTable generateChildTable(String navigateToForDetail, String from) {
        
        TableActionButton.Builder detail = TableActionButton.Builder.make(this, TableAction.tabledetail, navigateToForDetail, (container, id) -> {
            getEventbus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2READ).navigateTo(navigateToForDetail).from(from));
        });
         LOG.debug("creating table for childs");
        BuergerTable table = new ChildTable(this, detail);
        
        table.setFrom(from);
        List<Buerger> entities = this.queryKinder(this.current.getBean());
        this.eventbus.register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.CHILDQUERY);
        event.addEntities(entities);
        this.eventbus.post(event);
        
                
        return table;
    }

    public BuergerTable generateTable(String from, final TableActionButton.Builder... buttonBuilders) {
        return this.createTable(from, this.queryBuerger(), buttonBuilders);
    }
    
    private BuergerTable createTable(String from, List<Buerger> entities, final TableActionButton.Builder... buttonBuilders) {
        LOG.debug("creating table for buerger");
        BuergerTable table = new BuergerTable(this, buttonBuilders);
        
        table.setFrom(from);
        
        this.eventbus.register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.QUERY);
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
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.update, Type.text)));
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
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
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
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.save, Type.text)));
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
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.delete, Type.text)));
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
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.copy, Type.text)));
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
        
        if(event.getType().equals(EventType.QUERY_CHILD)) {
            LOG.debug("query child event");
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(EventType.CHILDQUERY).addEntities(this.queryKinder(event.getEntity())));
        }
        
        // cancel
        if(event.getType().equals(EventType.CANCEL)) {
            LOG.debug("cancel event");
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        if(event.getType().equals(EventType.ADD_SEARCHED_CHILD)){
            LOG.debug("select Child event");

            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        if(event.getType().equals(EventType.SAVE_AS_CHILD)){
            LOG.debug("save as Child event");
            this.addBuergerKind(event.getEntity());
           GenericSuccessNotification succes = new GenericSuccessNotification(
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.label)),
                    resolveRelative(getNotificationPath(NotificationType.success, SimpleAction.add, Type.text)));
            succes.show(Page.getCurrent());
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
