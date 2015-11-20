package de.muenchen.vaadin.guilib.util;

import com.vaadin.data.util.BeanItemContainer;

/**
 * Created by rene.zarwel on 18.11.15.
 */
public interface Datastore<T> {

    BeanItemContainer<T> getEntityContainer();

}
