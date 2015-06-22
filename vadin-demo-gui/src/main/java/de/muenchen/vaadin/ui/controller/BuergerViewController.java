package de.muenchen.vaadin.ui.controller;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.domain.Buerger;
import de.muenchen.vaadin.services.BuergerService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.components.CreatePersonForm;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.components.UpdatePersonForm;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 * Der Controller ist die zentrale Klasse um die Logik im Kontext Person abzubilden.
 * 
 * @author claus.straube
 */
@Component
public class BuergerViewController implements EventBusListener<BuergerEvent> {
    
    public static final String I18N_BASE_PATH = "m1.buerger";
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewController.class);
    
    /**
     * Die Service Klasse
     */
    BuergerService service;
    
    /**
     * Werkzeuge für Vaadin
     */
    VaadinUtil util;
    
    /**
     * Event Bus zur Kommunikation
     */
    EventBus eventbus;
    
    /**
     * {@link UI} {@link Navigator}
     */
    Navigator navigator;

    @Autowired
    public BuergerViewController(BuergerService service, VaadinUtil util) {
        this.service = service;
        this.util = util;
    }
    
    List<CreatePersonForm> createPersonForms = new ArrayList<>();
    List<UpdatePersonForm> updatePersonForms = new ArrayList<>();
    List<PersonTable> personTables = new ArrayList<>();
    
    // item cache
    BeanItem<Buerger> current;
    
    /**
     * Der Eventbus wird über die {@link DefaulPersonView} registriert. Dies
     * ist notwendig, da die Vaadin abhängigen Beans zum Zeitpunkt der Instanzierung
     * noch nicht vorhanden sind.
     * 
     * @param eventbus 
     */
    public void registerEventBus(EventBus eventbus) {
        if(this.eventbus == null) {
            this.eventbus = eventbus;
            this.eventbus.subscribe(this, true);
        }
    }
    
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
    
    ////////////////////////
    // Service Operations //
    ////////////////////////
    
    /**
     * Erstellt die Instanz eines {@link Person} Objektes. Dieses
     * Objekt wird noch nicht in der DB gespeichert.
     * 
     * @return neue Instanz einer Person
     */
    public Buerger createPerson() {
        Buerger entity = this.service.createBuerger();
        return entity;
    }
    
    /**
     * Kopiert eine vorhandene Instanz eines {@link Person} Objektes in eine
     * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
     * 
     * @param person
     * @return kopierte Instanz einer Person
     */
    public Buerger copyPerson(Buerger buerger) {
        return this.service.copyBuerger(buerger.getOid());
    }
    
    /**
     * Speichert ein {@link Buerger} Objekt in der Datenbank.
     * 
     * @param entity
     * @return 
     */
    public Buerger saveBuerger(Buerger entity) {
        return service.updateBuerger(entity);
    }
    
    public void deleteBuerger(Buerger entity) {
        service.deleteBuerger(entity.getOid());
    }
    
    public List<Buerger> findBuerger() {
        return service.findAll();
    }
    
    //////////////////////////////////////////////
    // Setter und Getter für die UI Komponenten //
    //////////////////////////////////////////////

    public CreatePersonForm generateCreatePersonForm(String navigateTo) {
        CreatePersonForm form = new CreatePersonForm(this, navigateTo);
        this.createPersonForms.add(form);
        
        return form;
    }

    public UpdatePersonForm generateUpdatePersonForm(String navigateTo) {      
        UpdatePersonForm form = new UpdatePersonForm(this, navigateTo);
        this.updatePersonForms.add(form);
        
        return form;
    }

    public PersonTable generatePersonTable(String navigateToAfterEdit) {      
        PersonTable table = new PersonTable(this);
        this.personTables.add(table);
        
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
    @Override
    public void onEvent(org.vaadin.spring.events.Event<BuergerEvent> e) {
        BuergerEvent event = e.getPayload();
        
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
            this.saveBuerger(event.getBuerger());
            
            // UI Komponenten aktualisieren
            this.personTables.stream().forEach((table) -> {
                table.add(event.getBuerger());
            });
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // delete
        if(event.getType().equals(EventType.DELETE)) {
            LOG.debug("delete event");
            // Service Operationen ausführen
            this.deleteBuerger(event.getPerson());
            
            // UI Komponenten aktualisieren
            this.personTables.stream().forEach((table) -> {
                table.delete(event.getItemId());
            });
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Person copy = this.copyPerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            this.personTables.stream().forEach((table) -> {
                table.add(copy);
            });
        }
        
        // select
        if(event.getType().equals(EventType.SELECT)) {
            LOG.debug("select event");
            
            // UI Komponenten aktualisieren
            updatePersonForms.stream().forEach((form) -> {
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
