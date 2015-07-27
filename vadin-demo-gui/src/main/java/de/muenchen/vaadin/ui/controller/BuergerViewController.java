package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import com.google.common.eventbus.Subscribe;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.components.CreateBuergerForm;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.components.UpdateBuergerForm;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.VaadinUtil;
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
public class BuergerViewController {
    
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
    
    //////////////////////////////////////////////
    // Setter und Getter für die UI Komponenten //
    //////////////////////////////////////////////

    public CreateBuergerForm generateCreatePersonForm(String navigateTo) {
        CreateBuergerForm form = new CreateBuergerForm(this, navigateTo);
        this.createBuergerForms.add(form);
        
        return form;
    }

    public UpdateBuergerForm generateUpdatePersonForm(String navigateTo) {      
        UpdateBuergerForm form = new UpdateBuergerForm(this, navigateTo);
        this.updateBuergerForms.add(form);
        
        return form;
    }

    public BuergerTable generatePersonTable(String navigateToAfterEdit) {      
        BuergerTable table = new BuergerTable(this);
        this.buergerTables.add(table);
        
        table.setNavigateToAfterEdit(navigateToAfterEdit);
        
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
            this.updateBuerger(event.getBuerger());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.add(event.getBuerger());
            });
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // save
        if(event.getType().equals(EventType.SAVE)) {
            LOG.debug("save event");
            // Service Operationen ausführen
            this.saveBuerger(event.getBuerger());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.add(event.getBuerger());
            });
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // delete
        if(event.getType().equals(EventType.DELETE)) {
            LOG.debug("delete event");
            // Service Operationen ausführen
            this.deleteBuerger(event.getBuerger());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.delete(event.getItemId());
            });
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Buerger copy = this.copyBuerger(event.getBuerger());
            
            // UI Komponenten aktualisieren
            this.buergerTables.stream().forEach((table) -> {
                table.add(copy);
            });
        }
        
        // select
        if(event.getType().equals(EventType.SELECT)) {
            LOG.debug("select event");
            
            // UI Komponenten aktualisieren
            updateBuergerForms.stream().forEach((form) -> {
                form.select(event.getItem());
            });
            
            // Wenn eine Seite das erste mal aufgerufen wird, dann
            // ist die UI Komponente noch nicht in der 'updatePersons'
            // Liste. In diesem Fall kann die ausgewählte Person
            // nicht aktiv an die UI Komponente übergeben werden. Deshalb
            // wird die Person unter 'current' gespeichert, damit sich
            // die Komponente selbst versorgen kann.
            this.current = event.getItem();
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
    }
    
}
