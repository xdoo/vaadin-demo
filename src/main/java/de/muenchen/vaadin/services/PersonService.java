/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.services;

import de.muenchen.vaadin.domain.Person;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface PersonService {
    
    public Person createPerson(Person person);
    
    public Person readPerson(Long id);
    
    public Person updatePerson(Person person);
    
    public void deletePerson(Long id);
    
    public List<Person> findAll();
    
}
