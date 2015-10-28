package de.muenchen.service;

import javax.persistence.PrePersist;

/**
 * Created by claus.straube on 28.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class DatabaseBeforeSaveEventListener {

    @PrePersist
    public void addOid(Object object){
        BaseEntity entity = (BaseEntity) object;
        entity.setOid(IdService.next());
    }
}
