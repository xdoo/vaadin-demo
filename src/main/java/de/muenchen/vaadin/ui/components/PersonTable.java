/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public class PersonTable extends CustomComponent {

    private List<Person> all;
    private PersonService service;
    private BeanItemContainer<Person> container;
    
    public PersonTable(VaadinUtil util, PersonService service) {

        this.service = service;
        
        // Have some data
        this.all = this.service.findAll();
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<Person>(Person.class, all);

        // Create a grid bound to the container
        Grid grid = new Grid(container);
        grid.setColumnOrder("id", "firstname", "lastname", "birthdate");

        setCompositionRoot(grid);
    }
    
    public void add(Person person) {
        System.out.println(person.toString());
        this.container.addBean(person);
    }
    
    public void update(Person person) {

    }

}
