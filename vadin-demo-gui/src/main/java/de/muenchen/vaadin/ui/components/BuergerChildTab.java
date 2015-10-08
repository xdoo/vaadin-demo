package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * @author claus
 */
@SuppressWarnings("unchecked")
public class BuergerChildTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid grid;

    public BuergerChildTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateBack) {

        this.controller = controller;

        grid = controller.getViewFactory().generateChildTable(BuergerDetailView.NAME)
                .activateCreate(navigateToForCreate)
                .activateRead(BuergerDetailView.NAME)
                .addCustomButton("add", () -> {
                    HorizontalLayout layout = new HorizontalLayout(controller.getViewFactory().generateChildSearchTable());
                    layout.setMargin(true);
                    getUI().addWindow(new TableSelectWindow(controller, layout));
                })
                .addCustomMultiSelectButton("delete", buerger -> {
                    final Association<Buerger> association = new Association<>((Buerger) buerger, Buerger.Rel.kinder.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
                })
                ;



        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, BuergerViewController.I18N_BASE_PATH));
    }


}
