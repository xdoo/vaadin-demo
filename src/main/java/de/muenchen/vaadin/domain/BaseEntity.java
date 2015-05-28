package de.muenchen.vaadin.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author claus.straube
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String oid;
    
}
