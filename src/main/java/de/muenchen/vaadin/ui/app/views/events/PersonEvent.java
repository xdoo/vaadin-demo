/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.domain.Person;

/**
 *
 * @author claus.straube
 */
public class PersonEvent {
    private Person person;
    private BeanItem<Person> item;

    public PersonEvent() {
    }

    public PersonEvent(Person person) {
        this.person = person;
    }

    public PersonEvent(BeanItem<Person> item) {
        this.person = item.getBean();
        this.item = item;
    }
    
    // members
    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
    
    public void setItem(BeanItem<Person> item) {
        this.item = item;
    }

    public BeanItem<Person> getItem() {
        return item;
    }
}
