package de.muenchen.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by p.mueller on 11.09.15.
 */
@Component("tenantPermissionEvaluator")
public class TenantService {

    public static final String IS_TENANT_FILTER = "@tenantPermissionEvaluator.isTenant(authentication,filterObject)";
    public static final String IS_TENANT_AUTH = "@tenantPermissionEvaluator.isTenant(authentication,returnObject)";

    public boolean isTenant(Authentication authentication, Object o) {

        if (o == null)
            return true;

        final String mandant = ((BaseEntity) o).getMandant();
        final String currentTenantId = getCurrentTenantId(authentication);

        return mandant.equals(currentTenantId);
    }


    public String getCurrentTenantId(Authentication authentication) {
        if (authentication == null)
            throw new IllegalArgumentException("Authentication cannot be null or emtpy!");
        String tenant = TenantUtils.extractTenantFromPrincipal(authentication.getPrincipal());

        if (tenant == null || tenant.isEmpty())
            throw new AssertionError(String.format("User with authentication %s has no tenant!.", authentication));

        return tenant;
    }

}
