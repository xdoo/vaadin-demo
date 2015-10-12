package de.muenchen.vaadin.services;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by claus.straube on 05.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
@SpringComponent
@UIScope
public class BuergerI18nResolver implements I18nResolver {

    // TODO entweder hier oder im I18nServiceConfigImpl angeben
    public static final String I18N_BASE_PATH = "buerger";
    /** {@link MessageService} zur Auflösung der Platzhalter */
    @Autowired
    private MessageService msg;

    /**
     * Resolve the path (e.g. "asdf.label").
     *
     * @param path the path.
     * @return the resolved String.
     */
    @Override
    public String resolve(String path) {
        return msg.get(path);
    }

    /**
     * Resolve the relative path (e.g. "asdf.label").
     * <p>
     * The base path will be appended at start and then read from the properties.
     *
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public String resolveRelative(String relativePath) {
        return msg.get(I18N_BASE_PATH + "." + relativePath);
    }

    @Override
    public String getBasePath() {
        return I18N_BASE_PATH;
    }

    /**
     * Resolve the relative path (e.g. ".asdf.label") to a icon.
     * <p>
     * The base path will be appended at start and then read from the properties.
     *
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public FontAwesome resolveIcon(String relativePath) {
        return msg.getFontAwesome(I18N_BASE_PATH + "." + relativePath + ".icon");
    }
}
