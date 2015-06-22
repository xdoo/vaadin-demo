package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class PersonTable extends CustomComponent {

    private final BeanItemContainer<Person> container;
    private BuergerViewController controller;
    private Table table;
    
    // Navigation
    private String navigateToAfterEdit;
    
    protected static final Logger LOG = LoggerFactory.getLogger(PersonTable.class);
    
    public PersonTable(final BuergerViewController controller) {
        
        this.controller = controller;
        
        // Have some data
        List<Person> all = controller.findPersons();
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<Person>(Person.class, all);
        
        // create table
        this.table = new Table();
        table.setContainerDataSource(this.container);
       
        //set action column
        table.addGeneratedColumn("button", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                
                return addButtons(itemId);
            }
        });
        
        //configure
        table.setWidth("100%");
        table.setPageLength(10);
        table.setColumnCollapsingAllowed(true);
        table.setVisibleColumns("firstname", "lastname", "birthdate", "button");
        
        // set headers
        table.setColumnHeader("firstname", controller.getUtil().readColumnHeader(controller.getI18nBasePath(), "firstname"));
        table.setColumnIcon("firstname", controller.getUtil().readColumnHeaderIcon(controller.getI18nBasePath(), "firstname"));
        table.setColumnHeader("birthdate", controller.getUtil().readColumnHeader(controller.getI18nBasePath(), "birthdate"));
        table.setColumnIcon("birthdate", controller.getUtil().readColumnHeaderIcon(controller.getI18nBasePath(), "birthdate"));
        table.setColumnHeader("lastname", controller.getUtil().readColumnHeader(controller.getI18nBasePath(), "lastname"));
        table.setColumnIcon("lastname", controller.getUtil().readColumnHeaderIcon(controller.getI18nBasePath(), "lastname"));
        table.setColumnHeader("button", "");
        
        setCompositionRoot(table);
    }
    
    public void add(Person person) {
        LOG.debug("added person to table.");
        this.container.addBean(person);
    }
    
    public void update(Person person) {
        LOG.debug("updated person in table.");
    }
    
    public void delete(Object id) {
        LOG.debug("deleted person from table.");
        this.container.removeItem(id);
    }
    
    /**
     * "Action" Buttons für jede Tabellenzeile. In jeder Tabellenzeile
     * gibt "Action" Buttons für folgende Ereignisse:
     * <ul>
     *  <li>bearbeiten</li>
     *  <li>kopieren</li>
     *  <li>löschen</li>
     * <ul>
     * 
     * @param id
     * @return 
     */
    public HorizontalLayout addButtons(final Object id) {
        
        //edit
        Button edit = new Button();
        edit.setIcon(FontAwesome.PENCIL);
        edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        edit.addClickListener(e -> {
            BeanItem<Person> item = container.getItem(id);
            BuergerEvent event = new BuergerEvent(item, id, EventType.SELECT);
            event.setNavigateTo(navigateToAfterEdit);
            controller.getEventbus().publish(this, event);
        });
        
        //copy
        Button copy = new Button();
        copy.setIcon(FontAwesome.COPY);
        copy.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        copy.addClickListener(e -> {
            BeanItem<Person> item = container.getItem(id);
            controller.getEventbus().publish(this, new BuergerEvent(item, id, EventType.COPY));
        });
        
        //delete
        Button delete = new Button();
        delete.setIcon(FontAwesome.TRASH_O);
        delete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);
        delete.addClickListener(e -> {
            BeanItem<Person> item = container.getItem(id);            
            GenericConfirmationWindow win = new GenericConfirmationWindow( new BuergerEvent(item, id, EventType.DELETE), controller.getEventbus());
            getUI().addWindow(win);
            win.center();
            win.focus();
        });

        HorizontalLayout layout = new HorizontalLayout(edit, copy, delete);
        layout.setSpacing(true);
        
        return layout;
    }
    
    // Members

    public void setNavigateToAfterEdit(String navigateToAfterEdit) {
        this.navigateToAfterEdit = navigateToAfterEdit;
    }

    public String getNavigateToAfterEdit() {
        return navigateToAfterEdit;
    }

}
