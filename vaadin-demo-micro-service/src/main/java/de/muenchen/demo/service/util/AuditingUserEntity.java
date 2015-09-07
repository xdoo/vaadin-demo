package de.muenchen.demo.service.util;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.search.annotations.Field;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by fabian.holtkoetter on 02.09.15.
 */
@Entity
public class AuditingUserEntity extends DefaultRevisionEntity {

    @Field
    String entity;
    @Field
    private String username;
    @Field
    private Date date;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
