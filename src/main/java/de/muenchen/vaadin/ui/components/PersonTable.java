/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.ui.app.views.events.SelectPersonEvent;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import java.text.DateFormat;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public class PersonTable extends CustomComponent {

    private final BeanItemContainer<Person> container;
    
    public PersonTable(final PersonViewController controller) {
        
        // Have some data
        List<Person> all = controller.findPersons();
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<Person>(Person.class, all);
        
        // remove id column
        GeneratedPropertyContainer wrapperContainer = new GeneratedPropertyContainer(this.container);
        wrapperContainer.removeContainerProperty("id");

        // Create a grid bound to the container
        Grid grid = new Grid(wrapperContainer);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder("firstname", "lastname", "birthdate");
        
        // set format for date
        grid.getColumn("birthdate").setRenderer(new DateRenderer(DateFormat.getDateInstance()));
        
        // selection listener
        grid.addSelectionListener(e -> {
            BeanItem<Person> item = container.getItem(grid.getSelectedRow());
            controller.getEventbus().publish(this, new SelectPersonEvent(item));
        });

        setCompositionRoot(grid);
    }
    
    public void add(Person person) {
        System.out.println("add person --> " + person.toString());
        this.container.addBean(person);
    }
    
    public void update(Person person) {
        System.out.println("update person --> " + person.toString());
    }

}
