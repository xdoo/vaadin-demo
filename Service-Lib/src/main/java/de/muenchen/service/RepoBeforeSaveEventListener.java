package de.muenchen.service;


import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component
public class RepoBeforeSaveEventListener extends AbstractRepositoryEventListener<BaseEntity> {

    @Override
    protected void onBeforeCreate(BaseEntity entity) {
        entity.setMandant(getCurrentMandant());
    }

    public String getCurrentMandant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return TenantUtils.extractTenantFromPrincipal(authentication.getPrincipal());
    }
}
