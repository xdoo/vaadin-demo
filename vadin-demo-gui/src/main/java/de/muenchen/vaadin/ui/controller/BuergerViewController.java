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
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.components.CreateBuergerForm;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.components.Success;
import de.muenchen.vaadin.ui.components.UpdateBuergerForm;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private BuergerService service;
    
    /**
     * Werkzeuge für Vaadin
     */
    private VaadinUtil util;
    
    /**
     * Event Bus zur Kommunikation
     */
    private EventBus eventbus;
    
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
    
    List<CreateBuergerForm> createBuergerForms = new ArrayList<>();
    List<UpdateBuergerForm> updateBuergerForms = new ArrayList<>();
    List<BuergerTable> buergerTables = new ArrayList<>();
    
    // item cache
    BeanItem<Buerger> current;
    
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
    
    //////////////////////////////////////////////
    // Setter und Getter für die UI Komponenten //
    //////////////////////////////////////////////

    public CreateBuergerForm generateCreateBuergerForm(String navigateTo) {
        CreateBuergerForm form = new CreateBuergerForm(this, navigateTo);
        this.createBuergerForms.add(form);
        
        return form;
    }

    public UpdateBuergerForm generateUpdateBuergerForm(String navigateTo) { 
        LOG.info("creating update buerger form");
        UpdateBuergerForm form = new UpdateBuergerForm(this, navigateTo);
        this.eventbus.register(form);
        this.eventbus.post(new BuergerComponentEvent(this.current));
        return form;
    }
    
    public BuergerTable generateBuergerTable(String navigateToForUpdateAndSelect){
        return this.generateBuergerTable(navigateToForUpdateAndSelect, navigateToForUpdateAndSelect);
    }

    public BuergerTable generateBuergerTable(String navigateToForEdit, String navigateToForSelect) {      
        BuergerTable table = new BuergerTable(this);
        this.buergerTables.add(table);
        
        table.setNavigateToForEdit(navigateToForEdit);
        table.setNavigateToForSelect(navigateToForSelect);
        
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
    public void onEvent(BuergerEvent event) {
        
        // create
        if(event.getType().equals(EventType.CREATE)) {
            LOG.debug("create event");
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        } 
        
        // update
        if(event.getType().equals(EventType.UPDATE)) {
            LOG.debug("update event");
            // Service Operationen ausführen
            this.updateBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.add(event.getEntity());
            });
            
            Success succes = new Success("Bürger angepasst", "Der Bürger wurde erfolgreich angepasst und gespeichert."); // TODO i18n
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
            this.buergerTables.stream().forEach((table) -> {
                table.add(event.getEntity());
            });
            
            Success succes = new Success("Bürger erstellt", "Der Bürger wurde erfolgreich erstellt und gespeichert."); // TODO i18n
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
            this.buergerTables.stream().forEach((table) -> {
                table.delete(event.getItemId());
            });
            
            Success succes = new Success("Bürger gelöscht", "Der Bürger wurde erfolgreich gelöscht."); // TODO i18n
            succes.show(Page.getCurrent());
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Buerger copy = this.copyBuerger(event.getEntity());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.add(copy);
            });
            
            Success succes = new Success("Bürger kopiert", "Der Bürger wurde erfolgreich kopiert."); // TODO i18n
            succes.show(Page.getCurrent());
        }
        
        // select um den Bürger zu bearbeiten
        if(event.getType().equals(EventType.SELECT2UPDATE)) {
            LOG.debug("select to update event");
            
            // Das ist notwendig, weil beim ersten Aufruf der UPDATE
            // Funktion erst die Komponente erstellt wird. Das Event
            // läuft also zuerst ins Leere und muss deshalb nochmal 
            // wiederholt werden.
            this.current = event.getItem();
            
            // UI Komponenten aktualisieren
            this.eventbus.post(new BuergerComponentEvent(event.getItem().getBean()));
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // select um den Bürger anzusehen
        if(event.getType().equals(EventType.SELECT2READ)) {
            LOG.debug("select to read event");
            
            // TODO
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // query
        if(event.getType().equals(EventType.QUERY)) {
            LOG.debug("query event");
            
            List<Buerger> buerger = this.queryBuerger(event.getQuery());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {table.addAll(buerger);});
        }
        
        // cancel
        if(event.getType().equals(EventType.CANCEL)) {
            LOG.debug("cancel event");
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo()); 
        }
    }
    
}
