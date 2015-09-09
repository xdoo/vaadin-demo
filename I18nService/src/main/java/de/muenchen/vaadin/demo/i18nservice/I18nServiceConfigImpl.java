/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.demo.i18nservice;

import com.vaadin.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Reads the properties based on locale and pathstring.
 *
 * @author maximilian.zollbrecht p.mueller
 */
@SpringComponent
@Primary
@RefreshScope
public class I18nServiceConfigImpl implements I18nService {

    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(I18nService.class);
    /**
     * Holds all properties.
     * Properties may be loaded from the spring cloud config server.
     */
    @Autowired
    private Environment env;

    @Override
    public String get(String path, Locale locale) {
        return this.get(getFullPath(path, locale));
    }

    /**
     * Returns the string for the given path.
     *
     * @param fullPath path to load property
     * @return resolved String for given path
     */
    private String get(String fullPath) {
        String message = env.getProperty(fullPath);
        if (message == null) {
            LOG.warn(String.format("found no message to path \"%s\"", fullPath));
            return getDebug() ? fullPath : "Could not be resolved in this language.";
        }
        return isoToUtf8(message);
    }

    /**
     * Returns the full path used in .properties file.
     *
     * @param path   Path
     * @param locale Locale
     * @return full path used in .properties file
     */
    private String getFullPath(String path, Locale locale) {
        return "i18n." + locale.toLanguageTag() + "." + path;
    }

    /**
     * Returns the utf8 string representation of an iso8859-1 encoded string.
     *
     * @param isoEncoded iso-8859-1 encoded String
     * @return the utf8 encoded String.
     */
    private String isoToUtf8(String isoEncoded) {
        try {
            return new String(isoEncoded.getBytes(ISO_8859_1), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Encoding not known.");
        }
    }

    @Override
    public Set<Locale> getSupportedLocales() {
        String localeString = env.getProperty("i18n.supported");

        if (localeString == null)
            throw new AssertionError("'i18n.supported' property not set.");

        return Stream.of(localeString.split(","))
                .map(this::forLanguageTag)
                .collect(Collectors.toSet());
    }

    @Override
    public Locale getFallbackLocale() {
        String localeString = env.getProperty("i18n.fallback");
        if (localeString == null)
            throw new AssertionError("'i18n.fallback' property not set.");

        return forLanguageTag(localeString);
    }

    /**
     * Try to convert a well-formed language-tag String into a Locale.
     * <p>
     * If the language tag can't be parsed an IllegalArgumentException will be thrown.
     *
     * @param s , formed as a language tag, e.g. de-DE
     * @return the best fitted Locale to the string.
     */
    private Locale forLanguageTag(String s) {
        Locale locale = Locale.forLanguageTag(s);

        if ("und".equals(locale.toLanguageTag()))
            throw new IllegalArgumentException(s + " could not be converted to a Locale.");

        return locale;
    }

    /**
     * Get if this Service is in debug mode.
     *
     * @return true if in debug.
     */
    private boolean getDebug() {
        return LOG.isDebugEnabled();
    }
}
