package de.muenchen.vaadin.demo.apilib.hateoas;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.hateoas.Link;

/**
 *
 * @author claus.straube
 */
public class HateoasUtil {
    
    public static final String REL_NEW = "new";
    public static final String REL_UPDATE = "update";
    public static final String REL_DELETE = "delete";
    public static final String REL_SAVE = "save";
    public static final String REL_QUERY = "query";
    public static final String REL_COPY = "copy";
    public final static String REL_SELF = "self";
    public final static String REL_COPY_LIST = "copy_list";
    public final static String REL_DELETE_LIST = "delete_list";
    
    /**
     * Gibt den {@link Link} für eine Relation zurück.
     * 
     * @param rel
     * @param links
     * @return 
     */
    public static Optional<Link> findLinkForRel(String rel, List<Link> links) {
        Optional<Link> result = Optional.empty();
        if(links != null) {
            result = links.stream().filter(l -> l.getRel().equals(rel)).findFirst();
        }
        return result;
    }
    
    /**
     * Prüft ob eine Relation in der Linksammlung vorhanden ist.
     * 
     * @param rel
     * @param links
     * @return 
     */
    public static boolean containsRel(String rel, List<Link> links) {
        return findLinkForRel(rel, links).isPresent();
    }
    
    /**
     * Wildcard Suche für links (starts with).
     * 
     * @param rel 
     * @param links
     * @return 
     */
    public List<Link> findLinksForRel(String rel, List<Link> links) {
        return links.stream().filter(l -> l.getRel().startsWith(rel)).collect(Collectors.toList());
    }
    
    /**
     * Erzeugt eine String Repräsentation der Link Liste.
     * 
     * @param links
     * @return 
     */
    public static String links(Map<String, Link> links) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("\nThere're %s links referenced.", links.size()));
        links.keySet().stream().forEach(k -> result.append(String.format("\n%s > %s", k, links.get(k))));
        return result.toString();
    }
    
}
