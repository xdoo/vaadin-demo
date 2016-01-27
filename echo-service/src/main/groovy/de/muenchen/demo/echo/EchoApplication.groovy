package de.muenchen.demo.echo

import com.netflix.discovery.DiscoveryClient
import de.muenchen.service.security.OAuth2RestTemplateConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by huningd on 15.12.15.
 */
@SpringBootApplication
@EnableResourceServer
/*
When something needs AOP as for instance @PreAuthorize it's important that it isn't use together with
EnableGlobalAuthentication, EnableGlobalMethodSecurity, EnableWebSecurity, or EnableWebMvcSecurity at one class.
Hence you must separate it in different classes. Otherwise it will leeds to a class build cycle.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableEurekaClient
@Import(OAuth2RestTemplateConfig.class)
class EchoApplication {

    @RestController
    static class Controller {
        @RequestMapping('/all')
        def echo(@RequestBody String msg, Authentication principal) {
            ['name': principal.getName(), 'authorities': principal.getAuthorities(), 'request': msg]
        }

        @RequestMapping('/secret')
        @PreAuthorize("hasRole('READ_SEC_User')")
        def echoSecret(@RequestBody String msg, Authentication principal) {
            ['name': principal.getName(), 'authorities': principal.getAuthorities(), 'request': msg]
        }
    }

    static void main(String[] args) {
        SpringApplication.run EchoApplication, args
    }

}
