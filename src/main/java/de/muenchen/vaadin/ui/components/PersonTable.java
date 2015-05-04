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
import de.muenchen.vaadin.ui.app.views.events.SelectPersonEvent;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public class PersonTable extends CustomComponent {

    private final BeanItemContainer<Person> container;
    private PersonViewController controller;
    
    public PersonTable(final PersonViewController controller) {
        
        this.controller = controller;
        
        // Have some data
        List<Person> all = controller.findPersons();
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<Person>(Person.class, all);
        
        // create table
        Table table = new Table();
        table.setContainerDataSource(this.container);
        
        //remove id column
        table.removeContainerProperty("id");
        
        //set action column
        table.addGeneratedColumn("button", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                
                return addButtons(itemId);
            }
        });
        
//        // remove id column
//        GeneratedPropertyContainer wrapperContainer = new GeneratedPropertyContainer(this.container);
//        wrapperContainer.removeContainerProperty("id");
//
//        // Create a grid bound to the container
//        Grid grid = new Grid(wrapperContainer);
//        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        grid.setColumnOrder("firstname", "lastname", "birthdate");
//        
//        // set format for date
//        grid.getColumn("birthdate").setRenderer(new DateRenderer(DateFormat.getDateInstance()));
//        
//        // selection listener
//        grid.addSelectionListener(e -> {
//            BeanItem<Person> item = container.getItem(grid.getSelectedRow());
//            controller.getEventbus().publish(this, new SelectPersonEvent(item));
//        });
//
//        setCompositionRoot(grid);
        
        setCompositionRoot(table);
    }
    
    public void add(Person person) {
        System.out.println("add person --> " + person.toString());
        this.container.addBean(person);
    }
    
    public void update(Person person) {
        System.out.println("update person --> " + person.toString());
    }
    
    public HorizontalLayout addButtons(final Object id) {
        
        //edit
        Button edit = new Button();
        edit.setIcon(FontAwesome.EDIT);
        edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        edit.addClickListener(e -> {
            BeanItem<Person> item = container.getItem(id);
            controller.getEventbus().publish(this, new SelectPersonEvent(item));
        });
        
        //copy
        Button copy = new Button();
        copy.setIcon(FontAwesome.COPY);
        copy.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        
        //delete
        Button delete = new Button();
        delete.setIcon(FontAwesome.TRASH_O);
        delete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        delete.addStyleName(ValoTheme.BUTTON_DANGER);

        HorizontalLayout layout = new HorizontalLayout(edit, copy, delete);
        layout.setSpacing(true);
        
        return layout;
    }

}
