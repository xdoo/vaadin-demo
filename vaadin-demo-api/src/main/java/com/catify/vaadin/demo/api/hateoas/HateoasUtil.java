package com.catify.vaadin.demo.api.hateoas;

import java.util.List;
import java.util.Optional;
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
    
    public static Optional<Link> findLinkForRel(String rel, List<Link> links) {
        return links.stream().filter(l -> l.getRel().equals(rel)).findFirst();
    }
    
    public static boolean containsRel(String rel, List<Link> links) {
        return findLinkForRel(rel, links).isPresent();
    }
    
}
