package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.BaseEntity;
import de.muenchen.demo.service.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component
public class BeforeSaveEventListener extends AbstractRepositoryEventListener<BaseEntity> {
    @Autowired
    UserRepository userRepository;

    @Override
    protected void onBeforeCreate(BaseEntity entity) {
        entity.setMandant(getCurrentMandant());
    }

    @Override
    protected void onBeforeSave(BaseEntity entity) {
        entity.setMandant(getCurrentMandant());
    }

    public String getCurrentMandant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(authentication.getName()).getMandant();
    }
}
