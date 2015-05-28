/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.services;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.domain.PersonRepository;
import de.muenchen.vaadin.ui.util.IdService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;
    
    /**
     * Factory Methode um eine neue Instanz einer Person zu bekommen. Hier
     * müssen die OID und die DB ID (technische ID) erstellt werden.
     * 
     * @return 
     */
    @Override
    public Person createPerson() {
        Person person = new Person();
        person.setOid(IdService.next());
        return person;
    }

    @Override
    public Person readPerson(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Person updatePerson(Person person) {
        return repository.save(person);
    }

    @Override
    public void deletePerson(Long id) {
        repository.delete(id);
    }

    @Override
    public List<Person> findAll() {
        return Lists.newArrayList(repository.findAll());
    }
    
}
