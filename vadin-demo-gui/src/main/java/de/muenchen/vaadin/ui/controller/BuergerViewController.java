package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.components.BuergerCreateForm;
import de.muenchen.vaadin.ui.components.BuergerReadForm;
import de.muenchen.vaadin.ui.components.BuergerSearchTable;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.components.GenericSuccessNotification;
import de.muenchen.vaadin.ui.components.BuergerUpdateForm;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Buerger abzubilden.
 * 
 * @author claus.straube
 */
@SpringComponent @UIScope
public class BuergerViewController implements Serializable {
    
    public static final String I18N_BASE_PATH = "m1.buerger";
    
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
    private Stack<String> from = new Stack<>();
    
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

    public String getI18nBasePath() {
        return I18N_BASE_PATH;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public BeanItem<Buerger> getCurrent() {
        return current;
    }

    public MessageService getMsg() {
        return msg;
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
        LOG.debug("creating 'create' buerger form");
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo);
        return form;
    }
    
    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        BuergerCreateForm form = new BuergerCreateForm(this, navigateTo);
        form.setType(EventType.SAVE_CHILD);
        return form;
    }
    
    public BuergerCreateForm generateCreateChildForm() {
        return this.generateCreateChildForm(this.from.pop());
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String from) { 
        LOG.debug("creating 'update' buerger form");
        BuergerUpdateForm form = new BuergerUpdateForm(this, navigateTo, this.from.pop(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2UPDATE));
        return form;
    }
    
    public BuergerUpdateForm generateUpdateForm(String from) { 
        return this.generateUpdateForm(this.from.peek(), from);
    }
    
    public BuergerReadForm generateReadForm(String navigateToUpdate, String from) {
        LOG.debug("creating 'read' buerger form");
        BuergerReadForm form = new BuergerReadForm(this, navigateToUpdate, this.from.pop(), from);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current, EventType.SELECT2READ));
        return form;
    }
    
    public BuergerSearchTable generateSearchTable(String navigateToForEdit, String navigateToForSelect, String navigateForCreate, String navigateFrom) {
        LOG.debug("creating 'search' table for buerger");
        return new BuergerSearchTable(this, navigateToForEdit, navigateToForSelect, navigateForCreate, navigateFrom);
    }
    
    public BuergerTable generateChildTable(String navigateToForEdit, String navigateToForSelect, String from) {
        BuergerTable table = this.createTable(navigateToForEdit, navigateToForSelect, from, this.queryKinder(this.current.getBean()));
        table.setCopy(Boolean.FALSE);
        return table;
    }

    public BuergerTable generateTable(String navigateToForEdit, String navigateToForSelect, String from) { 
        return this.createTable(navigateToForEdit, navigateToForSelect, from, this.queryBuerger());
    }
    
    private BuergerTable createTable(String navigateToForEdit, String navigateToForSelect, String from, List<Buerger> entities) {
        LOG.debug("creating table for buerger");
        BuergerTable table = new BuergerTable(this);
        
        table.setNavigateToForEdit(navigateToForEdit);
        table.setNavigateToForSelect(navigateToForSelect);
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
     */
    @Subscribe
    public void onEvent(BuergerAppEvent event) {
        
        // create
        if(event.getType().equals(EventType.CREATE)) {
            LOG.debug("create event");
            
            // Verlauf protokollieren
            this.stack(event);
            
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
            this.stack(event);
            
            GenericSuccessNotification succes = new GenericSuccessNotification("Bürger angepasst", "Der Bürger wurde erfolgreich angepasst und gespeichert."); // TODO i18n
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
            
            GenericSuccessNotification succes = new GenericSuccessNotification("Bürger erstellt", "Der Bürger wurde erfolgreich erstellt und gespeichert."); // TODO i18n
            succes.show(Page.getCurrent());
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // save child
        if(event.getType().equals(EventType.SAVE_CHILD)) {
            LOG.debug("save child event");
            // Service Operation ausführen
            this.saveBuergerKind(event.getEntity());
            
            GenericSuccessNotification succes = new GenericSuccessNotification("Bürger erstellt", "Der Bürger wurde erfolgreich erstellt und gespeichert."); // TODO i18n
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
            
            GenericSuccessNotification succes = new GenericSuccessNotification("Bürger gelöscht", "Der Bürger wurde erfolgreich gelöscht."); // TODO i18n
            succes.show(Page.getCurrent());
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Buerger copy = this.copyBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(copy, EventType.COPY));
            
            GenericSuccessNotification succes = new GenericSuccessNotification("Bürger kopiert", "Der Bürger wurde erfolgreich kopiert."); // TODO i18n
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
            this.stack(event);
            
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
            this.stack(event);
            
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
            this.eventbus.post(new BuergerComponentEvent(EventType.QUERY).addEntities(this.queryKinder(event.getEntity())));
        }
        
        // cancel
        if(event.getType().equals(EventType.CANCEL)) {
            LOG.debug("cancel event");
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo()); 
        }
    }
    
    private void stack(BuergerAppEvent event) {
        if(event.getFrom().isPresent()) {
            this.from.push(event.getFrom().get());
        }
    }
    
}
