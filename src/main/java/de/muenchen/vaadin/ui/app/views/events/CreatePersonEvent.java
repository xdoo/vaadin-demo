/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views.events;

import de.muenchen.vaadin.domain.Person;

/**
 *
 * @author claus.straube
 */
public class CreatePersonEvent extends PersonEvent {

    public CreatePersonEvent() {
        super();
    }

    public CreatePersonEvent(Person person) {
        super(person);
    } 
}
