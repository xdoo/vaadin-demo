package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.BaseEntity;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component("tenantPermissionEvaluator")
public class TenantService {

    public static final String IS_TENANT_FILTER = "@tenantPermissionEvaluator.isTenant(authentication,filterObject)";
    public static final String IS_TENANT_AUTH = "@tenantPermissionEvaluator.isTenant(authentication,returnObject)";

    @Autowired
    UserRepository userRepository;

    public boolean isTenant(Authentication authentication, Object o) {

        if (o == null)
            return true;

        final String mandant = ((BaseEntity) o).getMandant();
        final String currentTenantId = getCurrentTenantId(authentication.getName());

        return mandant.equals(currentTenantId);
    }


    public String getCurrentTenantId(String username) {
        final User user = userRepository.findFirstByUsername(username);
        return user.getMandant();
    }
}
