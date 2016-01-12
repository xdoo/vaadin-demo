package de.muenchen.vaadin.demo.i18nservice;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arne.schoentag on 26.10.15.
 */
@SpringComponent
@UIScope
public class I18nResolverImpl implements I18nResolver {


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
     * <p/>
     * The base path will be appended at start and then read from the properties.
     *
     * @param clazz the class for the basePath
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public String resolveRelative(Class clazz, String relativePath) {
        return msg.get(((clazz!=null) ? removeUnderscore(clazz.getSimpleName().toLowerCase()) + "." : "") + relativePath);
    }


    /**
     * Resolve the relative path (e.g. "asdf.label").
     * <p/>
     * The base path will be appended at start and then read from the properties.
     *
     * @param label the label for the basePath
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public String resolveRelative(String label, String relativePath) {
        return msg.get(((label!=null) ? removeUnderscore(label.toLowerCase()) + "." : "") + relativePath);
    }

    @Override
    public String getBasePath(Class clazz) {
        return (clazz!=null) ? (removeUnderscore(clazz.getSimpleName().toLowerCase())) : "";
    }

    /**
     * Resolve the relative path (e.g. ".asdf.label") to a icon.
     * <p/>
     * The base path will be appended at start and then read from the properties.
     *
     * @param clazz the class for the BasePath
     * @param relativePath the path to add to the base path.
     * @return the resolved String.
     */
    @Override
    public FontAwesome resolveIcon(Class clazz, String relativePath) {
        return msg.getFontAwesome((clazz != null) ? removeUnderscore(clazz.getSimpleName().toLowerCase()) + "." : "" + "." + relativePath + ".icon");
    }

    /**
     * Help method to remove an Underscore if a string ends with one.
     * @param string
     * @return
     */
    private String removeUnderscore(String string){
        return (string.charAt(string.length()-1)==('_')) ? string.toLowerCase().substring(0,string.length()-1):string;
    }

}
