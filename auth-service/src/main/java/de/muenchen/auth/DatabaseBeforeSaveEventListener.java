package de.muenchen.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;

/**
 * Created by claus.straube on 28.10.15.
 * fabian.holtkoetter ist unschuldig.
 * <p/>
 * Wird von BaseEntity verwendet, um das setzen der OID einer Entität vor dem Speichern in der Datenbank zu ermöglichen.
 */
public class DatabaseBeforeSaveEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseBeforeSaveEventListener.class);

    /**
     * Setzt die OID der zu speichernden Entität vor dem Speichern in der Datenbank
     *
     * @param object
     */
    @PrePersist
    public void addOid(Object object) {
        BaseEntity entity = (BaseEntity) object;
        if (entity.getOid() == null)
            entity.setOid(IdService.next());
    }
}
