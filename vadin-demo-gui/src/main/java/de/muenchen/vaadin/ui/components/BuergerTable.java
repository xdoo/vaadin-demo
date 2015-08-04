package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class BuergerTable extends CustomComponent {

    private final BeanItemContainer<Buerger> container;
    private BuergerViewController controller;
    private Table table;
    
    // Navigation
    private String navigateToForEdit;
    private String navigateToForSelect;
    private String from;
    
    // Buttons ein / aus blenden
    private boolean read = Boolean.TRUE;
    private boolean edit = Boolean.TRUE;
    private boolean copy = Boolean.TRUE;
    private boolean delete = Boolean.TRUE;
    
    
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerTable.class);
    
    public BuergerTable(final BuergerViewController controller) {
        
        this.controller = controller;
        
        // Have a container of some type to contain the data
        this.container = new BeanItemContainer<>(Buerger.class);
        
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
        table.setVisibleColumns(Buerger.VORNAME, Buerger.NACHNAME, Buerger.GEBURTSDATUM, "button");
        
        // set headers
        table.setColumnHeader(Buerger.VORNAME, controller.getMsg().readColumnHeader(controller.getI18nBasePath(), Buerger.VORNAME));
        table.setColumnIcon(Buerger.VORNAME, controller.getMsg().readColumnHeaderIcon(controller.getI18nBasePath(), Buerger.VORNAME));
        table.setColumnHeader(Buerger.GEBURTSDATUM, controller.getMsg().readColumnHeader(controller.getI18nBasePath(), Buerger.GEBURTSDATUM));
        table.setColumnIcon(Buerger.GEBURTSDATUM, controller.getMsg().readColumnHeaderIcon(controller.getI18nBasePath(), Buerger.GEBURTSDATUM));
        table.setColumnHeader(Buerger.NACHNAME, controller.getMsg().readColumnHeader(controller.getI18nBasePath(), Buerger.NACHNAME));
        table.setColumnIcon(Buerger.NACHNAME, controller.getMsg().readColumnHeaderIcon(controller.getI18nBasePath(),Buerger.NACHNAME));
        table.setColumnHeader("button", "");
        
        setCompositionRoot(table);
    }
    
    @Subscribe
    public void update(BuergerComponentEvent event) {
        if(event.getEventType().equals(EventType.SAVE)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.COPY)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.DELETE)) {
            this.delete(event.getItemID());
        }
        
        if(event.getEventType().equals(EventType.UPDATE)) {
            this.add(event.getEntity());
        }
        
        if(event.getEventType().equals(EventType.QUERY)) {
            this.addAll(event.getEntities());
        }
    }
    
    public void add(Optional<Buerger> optional) {
        if(optional.isPresent()) {
            LOG.debug("added buerger to table.");
            this.container.addBean(optional.get());
        }
    }
    
    public void addAll(List<Buerger> buerger) {
        LOG.debug("added search result");
        this.container.removeAllItems();
        this.container.addAll(buerger);
    }
    
    public void delete(Object id) {
        LOG.debug("deleted buerger from table.");
        this.container.removeItem(id);
    }
    
    /**
     * "Action" Buttons für jede Tabellenzeile. In jeder Tabellenzeile
     * gibt "Action" Buttons für folgende Ereignisse:
     * <ul>
     *  <li>ansehen</li>
     *  <li>bearbeiten</li>
     *  <li>kopieren</li>
     *  <li>löschen</li>
     * <ul>
     * 
     * @param id
     * @return 
     */
    public HorizontalLayout addButtons(final Object id) {
        HorizontalLayout layout = new HorizontalLayout();
        // select
        if (this.read) {
            Button select = new Button();
            select.setIcon(FontAwesome.FILE_O);
            select.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            select.addClickListener(e -> {
                BeanItem<Buerger> item = container.getItem(id);
                controller.getEventbus().post(new BuergerAppEvent(item, id, EventType.SELECT2READ).navigateTo(this.navigateToForSelect).from(this.from));
            });
            layout.addComponent(select);
        }
        
        
        //edit
        if (this.edit) {
            Button edit = new Button();
            edit.setIcon(FontAwesome.PENCIL);
            edit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            edit.addClickListener(e -> {
                BeanItem<Buerger> item = container.getItem(id);
                controller.getEventbus().post(new BuergerAppEvent(item, id, EventType.SELECT2UPDATE).navigateTo(navigateToForEdit).from(this.from));
            });
            layout.addComponent(edit);
        }
        
        //copy
        if (this.copy) {
            Button copy = new Button();
            copy.setIcon(FontAwesome.COPY);
            copy.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            copy.addClickListener(e -> {
                BeanItem<Buerger> item = container.getItem(id);
                controller.getEventbus().post(new BuergerAppEvent(item, id, EventType.COPY));
            });
            layout.addComponent(copy);
        }
        
        //delete
        if (this.delete) {
            Button delete = new Button();
            delete.setIcon(FontAwesome.TRASH_O);
            delete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            delete.addStyleName(ValoTheme.BUTTON_DANGER);
            delete.addClickListener(e -> {
                BeanItem<Buerger> item = container.getItem(id);
                GenericConfirmationWindow win = new GenericConfirmationWindow(new BuergerAppEvent(item, id, EventType.DELETE), controller.getEventbus());
                getUI().addWindow(win);
                win.center();
                win.focus();
            });
            layout.addComponent(delete);
        }

        layout.setSpacing(true);
        
        return layout;
    }
    
    // Members

    public void setNavigateToForEdit(String navigateToForEdit) {
        this.navigateToForEdit = navigateToForEdit;
    }

    public String getNavigateToForEdit() {
        return navigateToForEdit;
    }

    public void setNavigateToForSelect(String navigateToForSelect) {
        this.navigateToForSelect = navigateToForSelect;
    }

    public String getNavigateToForSelect() {
        return navigateToForSelect;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    
}
