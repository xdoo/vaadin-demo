package de.muenchen.service;

import javax.persistence.PrePersist;

/**
 * Created by claus.straube on 28.10.15.
 * fabian.holtkoetter ist unschuldig.
 *
 * Wird von BaseEntity verwendet, um das setzen der OID einer Entität vor dem Speichern in der Datenbank zu ermöglichen.
 */
public class DatabaseBeforeSaveEventListener {

    /**
     * Setzt die OID der zu speichernden Entität vor dem Speichern in der Datenbank
     * @param object
     */
    @PrePersist
    public void addOid(Object object) {
        BaseEntity entity = (BaseEntity) object;
        //TODO Hackfür die Tests. Soll das wirklich möglich sein?
        if (entity.getOid() == null)
            entity.setOid(IdService.next());
    }
}
