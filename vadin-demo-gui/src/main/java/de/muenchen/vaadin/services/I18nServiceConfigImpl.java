/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.services;

import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * Reads the properties based on locale and pathstring.
 * @author maximilian.zollbrecht p.mueller
 */
@Primary
@SpringComponent
public class I18nServiceConfigImpl implements I18nService {

    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";

    /**
     * Holds all properties.
     * Properties may be loaded from the spring cloud config server.
     */
    @Autowired
    private Environment env;

    private static final Logger LOG = LoggerFactory.getLogger(I18nService.class);

    @Override
    public String get(String path, Locale locale) {
        return this.get(getFullPath(path,locale));
    }

    /**
     * Returns the string for the given path.
     * @param fullPath path to load property
     * @return resolved String for given path
     */
    private String get(String fullPath) {
        String message = env.getProperty(fullPath);
        if(message == null) {
            LOG.warn(String.format("found no message to path \"%s\"", fullPath));
            return fullPath;
        }
        return isoToUtf8(message);
    }

    /**
     * Returns the full path used in .properties file.
     * @param path Path
     * @param locale Locale
     * @return full path used in .properties file
     */
    private String getFullPath(String path, Locale locale){
        return "i18n."+locale+"."+path;
    }

    /**
     * Returns the utf8 string representation of an iso8859-1 encoded string.
     * @param isoEncoded iso-8859-1 encoded String
     * @return the utf8 encoded String.
     */
    private String isoToUtf8(String isoEncoded){
        try {
            return new String(isoEncoded.getBytes(ISO_8859_1), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Encoding not known.");
        }
    }
}
