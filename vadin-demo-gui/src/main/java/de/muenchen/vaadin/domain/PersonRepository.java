package de.muenchen.vaadin.domain;

import org.springframework.data.repository.CrudRepository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author claus.straube
 */
public interface PersonRepository extends  CrudRepository<Person, Long>{
    
}
