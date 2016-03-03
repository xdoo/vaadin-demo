package de.muenchen.itm.infrastructure.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
@EnableHystrixDashboard
public class HystrixApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HystrixApplication.class, args);
    }
    
    @RequestMapping("/")
    public String home() {
        return "forward:/hystrix";
    }

}

