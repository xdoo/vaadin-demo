package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component("tenantPermissionEvaluator")
public class TenantPermissionEvaluator {

    @Autowired
    UserRepository userRepository;

    public boolean isTenant(Authentication authentication, String tenant) {
        return tenant.equals(getCurrentTenantId(authentication.getName()));
    }


    public String getCurrentTenantId(String username) {
        final User user = userRepository.findFirstByUsername(username);
        return user.getMandant();
    }
}
