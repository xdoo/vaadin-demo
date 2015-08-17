/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.services;

import java.util.Locale;

import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;


/**
 * Reads the properties based on locale and pathstring.
 * @author maximilian.zollbrecht p.mueller
 */
@Primary
@SpringComponent
public class I18nServiceConfigImpl implements I18nService {

    @Autowired
    private Environment env;

    private static final Logger LOG = LoggerFactory.getLogger(I18nService.class);

    @Override
    public String get(String path, Locale locale) {
        try{
            // return correct language
            return env.getProperty("i18n."+locale+"."+path);
        } catch(Exception ex) {
            LOG.warn(String.format("found no message to path \"i18n.%s.%s\". will use empty string.", Locale.getDefault(), path));
            return "";
        }
    }
}
