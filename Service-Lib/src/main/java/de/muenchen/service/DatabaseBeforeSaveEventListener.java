package de.muenchen.service;

import javax.persistence.PrePersist;

/**
 * Created by claus.straube on 28.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class DatabaseBeforeSaveEventListener {

    @PrePersist
    public void addOid(Object object) {
        BaseEntity entity = (BaseEntity) object;
        //TODO Hackfür die Tests. Soll das wirklich möglich sein?
        if (entity.getOid() == null)
            entity.setOid(IdService.next());
    }
}
