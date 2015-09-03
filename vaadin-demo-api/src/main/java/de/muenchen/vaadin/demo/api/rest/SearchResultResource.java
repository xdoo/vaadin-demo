package de.muenchen.vaadin.demo.api.rest;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author claus.straube
 * @param <E>
 */
public class SearchResultResource<E> extends ResourceSupport {
    
    private List<E> result = new ArrayList<>();
    
    public void add(E e) {
        this.result.add(e);
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
    
}
