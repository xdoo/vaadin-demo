package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * @author claus
 */
public class BuergerChildTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid grid;
    private ActionButton delete;

    public BuergerChildTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateBack) {

        this.controller = controller;

        ActionButton create = new ActionButton(controller, SimpleAction.create, navigateToForCreate);
        create.addClickListener(clickEvent -> {
            //TODO kind erstellen und dann hinzufügen.
            controller.getNavigator().navigateTo(navigateToForCreate);
        });
        ActionButton add = new ActionButton(controller, SimpleAction.add, "");
        add.addClickListener(clickEvent -> {
            getUI().addWindow(new TableSelectWindow(controller, controller.getViewFactory().generateChildSearchTable()));
            //controller.postEvent(controller.buildAppEvent(EventType.ADD_CHILD))
        });

        delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<Buerger> item = (BeanItem<Buerger>) grid.getContainerDataSource().getItem(next);

                    final Association<Buerger> association = new Association<>(item.getBean(), Buerger.Rel.kinder.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
                    grid.deselect(next);
                }
            }
        });
        delete.setVisible(false);

        grid = controller.getViewFactory().generateChildTable(navigateToForDetail);
        grid.setColumns(Buerger.Field.getProperties());
        grid.addSelectionListener(selectionEvent -> setButtonVisability());

        // set headers
        this.grid.getColumn(Buerger.Field.vorname.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.geburtsdatum.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.nachname.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), I18nPaths.Type.column_header)));
        this.grid.getColumn(Buerger.Field.augenfarbe.name()).setHeaderCaption(controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), I18nPaths.Type.column_header)));

        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, add, delete);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, grid);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, BuergerViewController.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }

    private void setButtonVisability() {
        if (grid.getSelectedRows().size() == 0)
            delete.setVisible(Boolean.FALSE);
        else
            delete.setVisible(Boolean.TRUE);
    }

    public GenericGrid getGrid() {
        return grid;
    }

    public void setGrid(GenericGrid grid) {
        this.grid = grid;
    }
}
