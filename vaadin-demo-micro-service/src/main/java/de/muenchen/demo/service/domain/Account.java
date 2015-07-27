/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "ACCOUNTS")
public class Account extends SecurityEntity{
    
    
    
}
