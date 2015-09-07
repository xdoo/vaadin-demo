package de.muenchen.demo.service.util;

import de.muenchen.demo.service.domain.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.util.Date;
import java.util.Objects;

/**
 * Created by fabian.holtkoetter on 02.09.15.
 */
public class AuditingListener {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingListener.class);

    /**
     * Wird bei lesendem zugriff auf ein Objekt ausgeführt.
     *
     * @param entity
     */
    @PostLoad
    public void onPostLoad(BaseEntity entity) {

        LOG.error("PostLoad called");

    }

    /**
     * Wird bei erstellen eines Objekts ausgeführt.
     */
    @PostPersist
    public void onPostPersist(BaseEntity entity) {

        LOG.error("PostPersist called");

    }

    /**
     * Wird beim entfernen eines Objekts ausgeführt.
     */
    @PostRemove
    public void onPostRemove(BaseEntity entity) {

        LOG.error("PostRemove called");

    }

    /**
     * Wird nach aktualisieren eines Objekts ausgeführt.
     */
    @PostUpdate
    public void onPostUpdate(BaseEntity entity) {

        LOG.error("PostUpdate called");

    }

    @Deprecated
    public void newRevision(Object revisionObject){
        LOG.debug("New Revision created.");
        AuditingUserEntity ent = (AuditingUserEntity) revisionObject;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "unauthenticated user";
        String remoteAddress = "unknown";

        if (authentication.isAuthenticated()) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
            username = authentication.getName();
            if (!Objects.isNull(webAuthenticationDetails)) {
                remoteAddress = webAuthenticationDetails.getRemoteAddress();
            }
        }

        ent.setUsername(username);
        ent.setEntity(remoteAddress);
        ent.setDate(new Date(System.currentTimeMillis()));
    }

}
