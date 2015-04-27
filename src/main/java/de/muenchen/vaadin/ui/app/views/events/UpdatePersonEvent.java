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
public class UpdatePersonEvent extends PersonEvent {
    
    public UpdatePersonEvent() {
        super();
    }

    public UpdatePersonEvent(Person person) {
        super(person);
    }

    public UpdatePersonEvent(BeanItem<Person> item) {
        super(item);
    }
    
}
