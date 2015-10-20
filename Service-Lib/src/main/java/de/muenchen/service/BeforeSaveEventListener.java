package de.muenchen.service;


import de.muenchen.security.repositories.UserRepository;
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
        entity.setOid(IdService.next());
    }

    public String getCurrentMandant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(authentication.getName()).getMandant();
    }
}
