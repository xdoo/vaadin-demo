/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import de.muenchen.vaadin.domain.Person;

/**
 *
 * @author claus.straube
 */
public class PersonForm extends CustomComponent {

    public PersonForm(Person item) {
        FormLayout layout = new FormLayout();

        // Now use a binder to bind the members
        final BeanFieldGroup<Person> binder = new BeanFieldGroup<Person>(Person.class);
        binder.setItemDataSource(item);
        layout.addComponent(binder.buildAndBind("Name", "name"));
        layout.addComponent(binder.buildAndBind("Age", "age"));

        setCompositionRoot(layout);
    }

}
