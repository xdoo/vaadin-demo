package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class PersonEvent extends Event {
    
    private Person person;
    private BeanItem<Person> item;

    public PersonEvent() {
    }

    public PersonEvent(EventType type) {
        this.type = type;
    }
    
    

    public PersonEvent(Person person, EventType type) {
        this.person = person;
        this.type = type;
    }

    public PersonEvent(BeanItem<Person> item, Object itemId, EventType type) {
        this.person = item.getBean();
        this.item = item;
        this.type = type;
        this.itemId = itemId;
    }
    
    // members
    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
    
    public void setItem(BeanItem<Person> item) {
        this.person = item.getBean();
        this.item = item;
    }

    public BeanItem<Person> getItem() {
        return item;
    }
    
}
