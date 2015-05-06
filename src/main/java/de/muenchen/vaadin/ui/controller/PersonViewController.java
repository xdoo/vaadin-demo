package de.muenchen.vaadin.ui.controller;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
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
 *
 * @author claus.straube
 */
@Component
public class PersonViewController implements EventBusListener<PersonEvent> {
    
    public static final String I18N_BASE_PATH = "m1.person";
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(PersonViewController.class);
    
    /**
     * Die Service Klasse
     */
    PersonService service;
    
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
    public PersonViewController(PersonService service, VaadinUtil util) {
        this.service = service;
        this.util = util;
    }
    
    List<CreatePersonForm> createPersonForms = new ArrayList<>();
    List<UpdatePersonForm> updatePersonForms = new ArrayList<>();
    List<PersonTable> personTables = new ArrayList<>();
    
    // components
    CreatePersonForm createPersonForm;
    UpdatePersonForm updatePersonForm;
    PersonTable personTable;
    
    public void registerEventBus(EventBus eventbus) {
        if(this.eventbus == null) {
            this.eventbus = eventbus;
            this.eventbus.subscribe(this, true);
        }
    }
    
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
    
    
    ////////////////////////
    // Service Operations //
    ////////////////////////
    
    /**
     * Erstellt die Instanz eines {@link Person} Objektes. Dieses
     * Objekt wird noch nicht in der DB gespeichert.
     * 
     * @return neue Instanz einer Person
     */
    public Person createPerson() {
        return new Person();
    }
    
    /**
     * Kopiert eine vorhandene Instanz eines {@link Person} Objektes in eine
     * neue Instanz. Die Kopie wird gleich in der DB gespeichert.
     * 
     * @param person
     * @return kopierte Instanz einer Person
     */
    public Person copyPerson(Person person) {
        Person clone = new Person();
        
        // Properties kopieren.
        // TODO --> hier kann eventuell ein Framework eingesetzt werden,
        //          oder man generiert einfach die Kopier-Operationen.
        clone.setFirstname(person.getFirstname());
        clone.setLastname(person.getLastname());
        clone.setBirthdate(person.getBirthdate());
        
        // in der DB speichern
        clone = this.savePerson(clone);
        return clone;
    }
    
    /**
     * Speichert ein {@link Person} Objekt in der Datenbank.
     * 
     * @param person
     * @return 
     */
    public Person savePerson(Person person) {
        return service.updatePerson(person);
    }
    
    public void deletePerson(Person person) {
        service.deletePerson(person.getId());
    }
    
    public List<Person> findPersons() {
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
    public void onEvent(org.vaadin.spring.events.Event<PersonEvent> e) {
        PersonEvent event = e.getPayload();
        
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
            this.savePerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            this.personTables.stream().forEach((table) -> {
                table.add(event.getPerson());
            });
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
        
        // delete
        if(event.getType().equals(EventType.DELETE)) {
            LOG.debug("delete event");
            // Service Operationen ausführen
            this.deletePerson(event.getPerson());
            
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
            
            // Zur Seite wechseln
            this.navigator.navigateTo(event.getNavigateTo());
        }
    }
    
}
