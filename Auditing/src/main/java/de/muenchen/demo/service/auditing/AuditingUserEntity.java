package de.muenchen.demo.service.auditing;

import org.hibernate.search.annotations.Field;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

/**
 * Created by fabian.holtkoetter on 02.09.15.
 */
@Entity
public class AuditingUserEntity {
    @Id
    @GeneratedValue
    private long id;

    @Lob
    @Field
    String entity;

    @Field
    private String username;

    @Field
    private Date date;

    @Field
    private String changeType;

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

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuditingUserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", date=" + date +
                ", changeType=" + changeType +
                ", entity='" + entity + '\'' +
                '}';
    }
}
