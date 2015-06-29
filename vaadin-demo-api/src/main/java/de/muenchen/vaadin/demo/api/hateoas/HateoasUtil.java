package de.muenchen.vaadin.demo.api.hateoas;

import java.util.List;
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
    
    /**
     * Gibt den {@link Link} für eine Relation zurück.
     * 
     * @param rel
     * @param links
     * @return 
     */
    public static Optional<Link> findLinkForRel(String rel, List<Link> links) {
        return links.stream().filter(l -> l.getRel().equals(rel)).findFirst();
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
    
}
