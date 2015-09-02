package de.muenchen.demo.service.util;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

/**
 * Created by fabian.holtkoetter on 02.09.15.
 */
@Entity
@RevisionEntity(AuditingListener.class)
public class AuditingUserEntity extends DefaultRevisionEntity {
    private String username;
    private String remoteAdress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemoteAdress() {
        return remoteAdress;
    }

    public void setRemoteAdress(String remoteAdress) {
        this.remoteAdress = remoteAdress;
    }
}
