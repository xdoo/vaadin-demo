package de.muenchen.demo.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Created by p.mueller on 11.09.15.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    TenantPermissionEvaluator tenantPermissionEvaluator;
    @Autowired
    ApplicationContext applicationContext;


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        final PermissionEvaluator permissionEvaluator = tenantPermissionEvaluator;

        final DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setApplicationContext(applicationContext);
        handler.setPermissionEvaluator(permissionEvaluator);

        return handler;
    }


}
