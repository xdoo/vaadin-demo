package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rene.zarwel on 07.10.15.
 */
public class GenericGrid<T> extends CustomComponent{
    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);

    /**Wrapped Grid**/
    private final Grid grid = new Grid();
    private TextField filter = new TextField();
    private Button search;
    private Button reset;
    private ActionButton read;
    private ActionButton edit;
    private ActionButton copy;
    private ActionButton delete;
    private ActionButton create;

    public GenericGrid(BeanItemContainer<T> datastore, Enum fields) {
        grid.setContainerDataSource(datastore);


    }
}
