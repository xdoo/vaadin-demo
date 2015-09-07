/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.util.AuditingListener;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "ACCOUNTS")
@EntityListeners(AuditingListener.class)
public class Account extends SecurityEntity{
    
    
    
}
