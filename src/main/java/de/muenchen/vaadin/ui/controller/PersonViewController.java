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
    
    /**
     * {@link UI} {@link Navigator}
     */
    Navigator navigator;
    
    String i18nBasePath;

    public PersonViewController(PersonService service, VaadinUtil util, EventBus eventbus, String i18nBasePath, MainUI ui) {
        this.service = service;
        this.util = util;
        this.eventbus = eventbus;
        this.i18nBasePath = i18nBasePath;
        this.navigator = ui.getNavigator();
        
        // an Event Bus registrieren
        this.eventbus.subscribe(this, true);
    }
    
    List<CreatePersonForm> createPersonForms;
    List<UpdatePersonForm> updatePersonForms;
    List<PersonTable> personTables;
    
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

    public String getI18nBasePath() {
        return i18nBasePath;
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
        if(this.createPersonForms == null) {
            this.createPersonForms = new ArrayList<>();
        }
        
        CreatePersonForm form = new CreatePersonForm(this, navigateTo);
        this.createPersonForms.add(form);
        
        return form;
    }

    public UpdatePersonForm generateUpdatePersonForm(String navigateTo) {
        if(this.updatePersonForms == null) {
            this.updatePersonForms = new ArrayList<>();
        }
        
        UpdatePersonForm form = new UpdatePersonForm(this, navigateTo);
        this.updatePersonForms.add(form);
        
        return form;
    }

    public PersonTable generatePersonTable(String navigateToAfterEdit) {
        if(this.personTables == null) {
            this.personTables = new ArrayList<>();
        }
        
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
            this.personTables.stream().forEach((table) -> {
                table.add(event.getPerson());
            });
            
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
            this.personTables.stream().forEach((table) -> {
                table.add(copy);
            });
        }
        
        // select
        if(event.getType().equals(EventType.SELECT)) {
            LOG.debug("select event");
            
            // UI Komponenten aktualisieren
            updatePersonForms.stream().forEach((_item) -> {
                this.updatePersonForm.select(event.getItem());
            });
        }
    }
    
}
