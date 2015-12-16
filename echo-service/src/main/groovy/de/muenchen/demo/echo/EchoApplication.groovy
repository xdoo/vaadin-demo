package de.muenchen.demo.echo

import org.apache.coyote.Request
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by huningd on 15.12.15.
 */
@SpringBootApplication
@RestController
@EnableResourceServer
class EchoApplication {

    @RequestMapping('/')
    def home(@RequestBody String msg) {
       msg
    }

    static void main(String[] args) {
        SpringApplication.run EchoApplication, args
    }

}