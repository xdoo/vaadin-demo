package de.muenchen.demo.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

/**
 * Created by fabian.holtkoetter on 02.09.15.
 */
public class AuditingListener implements org.hibernate.envers.RevisionListener {

    private static final Logger LOG = LoggerFactory.getLogger(AuditingListener.class);

    @Override
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
        ent.setRemoteAdress(remoteAddress);
    }

}
