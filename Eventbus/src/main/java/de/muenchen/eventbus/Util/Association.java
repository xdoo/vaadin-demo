package de.muenchen.eventbus.Util;

import org.springframework.hateoas.Link;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class Association<T>{
    private final T association;

    private final String rel;

    public Association(T association, String rel) {
        this.association = association;
        this.rel = rel;
    }

    public T getAssociation() {
        return association;
    }

    public String getRel() {
        return rel;
    }
}
