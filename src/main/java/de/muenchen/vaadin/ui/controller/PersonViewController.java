package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.components.CreatePersonForm;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.components.UpdatePersonForm;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 *
 * @author claus.straube
 */
public class PersonViewController implements EventBusListener<PersonEvent> {
    
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

    public PersonViewController(PersonService service, VaadinUtil util, EventBus eventbus) {
        this.service = service;
        this.util = util;
        this.eventbus = eventbus;
        
        this.eventbus.subscribe(this, true);
    }
     
    // components
    CreatePersonForm createPersonForm;
    UpdatePersonForm updatePersonForm;
    PersonTable personTable;
   

    public EventBus getEventbus() {
        return eventbus;
    }

    public VaadinUtil getUtil() {
        return util;
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

    public CreatePersonForm getCreatePersonForm() {
        if(this.createPersonForm == null) {
            this.createPersonForm = new CreatePersonForm(this);
        }
        return createPersonForm;
    }

    public UpdatePersonForm getUpdatePersonForm() {
        if(this.updatePersonForm == null) {
            this.updatePersonForm = new UpdatePersonForm(this);
        }
        return updatePersonForm;
    }

    public PersonTable getPersonTable() {
        if(this.personTable == null) {
            this.personTable = new PersonTable(this);
        }
        return personTable;
    }
    
    /////////////////////
    // Event Steuerung //
    /////////////////////
    
    /**
     * In dieser Methode wird zentral gesteuert, was bei bestimmten Ereignissen passiert.
     * Ausgehend von den Ereignissen werden innerhalb der UI Komponenten Operationen
     * ausgeführt. So ist es möglich eine Kommunnikation zwischen den Komponenten zu
     * schaffen, ohne dass diese sich untereinander kennen müssen.
     * </br>
     * Bevor auf einer Komponente eine Operation ausgeführt wird, muss gepfüt werden, ob 
     * die Komponente überhaupt aktiv verwendet wird (also nicht null ist).
     * 
     */
    @Override
    public void onEvent(org.vaadin.spring.events.Event<PersonEvent> e) {
        PersonEvent event = e.getPayload();
        
        // create
        if(event.getType().equals(EventType.CREATE)) {
            LOG.debug("create event");
            // Service Operationen ausführen
            this.savePerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            if(this.personTable != null) {
                this.personTable.add(event.getPerson());
            }
        } 
        
        // update
        if(event.getType().equals(EventType.UPDATE)) {
            LOG.debug("update event");
            // Service Operationen ausführen
            this.savePerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            if(this.personTable != null) {
                this.personTable.update(event.getPerson());
            }
        }
        
        // delete
        if(event.getType().equals(EventType.DELETE)) {
            LOG.debug("delete event");
            // Service Operationen ausführen
            this.deletePerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            if(this.personTable != null) {
                this.personTable.delete(event.getItemId());
            }
        }
        
        // copy
        if(event.getType().equals(EventType.COPY)) {
            LOG.debug("copy event");
            // Service Operationen ausführen
            Person copy = this.copyPerson(event.getPerson());
            
            // UI Komponenten aktualisieren
            if(this.personTable != null) {
                this.personTable.add(copy);
            }
        }
        
        // select
        if(event.getType().equals(EventType.SELECT)) {
            LOG.debug("select event");
            
            // UI Komponenten aktualisieren
            if(this.updatePersonForm != null) {
                this.updatePersonForm.select(event.getItem());
            }
        }
    }
    
}
