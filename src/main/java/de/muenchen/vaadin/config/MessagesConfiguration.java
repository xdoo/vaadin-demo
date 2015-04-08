/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

/**
 *
 * @author claus
 */
@Configuration
public class MessagesConfiguration {

    @Bean
    MessageProvider communicationMessages() {
        return new ResourceBundleMessageProvider("messages"); // Will use UTF-8 by default
    }
}
