package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component
public class TenantPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        return "tenant".equals(o1) && o.equals(getCurrentTenantId(authentication.getName()));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }

    public String getCurrentTenantId(String username) {
        final User user = userRepository.findFirstByUsername(username);
        return user.getMandant();
    }
}
