package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.Util.Association;
import de.muenchen.eventbus.types.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

/**
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid grid;
    private ActionButton delete;

    public BuergerPartnerTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateToForAdd, String from) {

        this.controller = controller;

        ActionButton create = new ActionButton(controller, SimpleAction.create, navigateToForCreate);
        create.addClickListener(clickEvent -> {
            if (grid.getContainerDataSource().size() == 0) {
                controller.getNavigator().navigateTo(navigateToForCreate);
            } else {
                GenericConfirmationWindow window = new GenericConfirmationWindow(controller, SimpleAction.override, e -> {
                    controller.getNavigator().navigateTo(navigateToForCreate);
                });
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        ActionButton add = new ActionButton(controller, SimpleAction.add, navigateToForAdd);
        add.addClickListener(clickEvent -> {
            if (grid.getContainerDataSource().size() == 0) {
                getUI().addWindow(new TableSelectWindow(controller, controller.getViewFactory().generateBuergerPartnerSearchTable()));
                //controller.postEvent(controller.buildAppEvent(EventType.ADD_PARTNER));
            } else {
                GenericConfirmationWindow window =
                        new GenericConfirmationWindow(controller, SimpleAction.override, e -> getUI().addWindow(new TableSelectWindow(controller, controller.getViewFactory().generateBuergerPartnerSearchTable())));
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });


        delete = new ActionButton(controller, SimpleAction.delete, null);
        delete.addClickListener(clickEvent -> {
            if (grid.getSelectedRows() != null) {
                for (Object next : grid.getSelectedRows()) {
                    BeanItem<Buerger> item = (BeanItem<Buerger>) grid.getContainerDataSource().getItem(next);
                    Association<Buerger> association = new Association<Buerger>(item.getBean(), Buerger.Rel.partner.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.DELETE), reactor.bus.Event.wrap(association));
                    grid.deselect(next);
                }
            }
        });
        delete.setVisible(false);

        grid = controller.getViewFactory().generatePartnerTable(navigateToForDetail);
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

        setId(String.format("%s_%s_%s_PARENT_TAB", navigateToForDetail, from, BuergerViewController.I18N_BASE_PATH));
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
