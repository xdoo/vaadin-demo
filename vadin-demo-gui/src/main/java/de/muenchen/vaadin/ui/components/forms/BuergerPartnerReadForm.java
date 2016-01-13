package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm;

/**
 * Provides a simple ReadForm for the Partner of a Buerger.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerPartnerReadForm extends BaseComponent {
    /**
     * Indicates the mode of the form.
     */
    public static final boolean READ_ONLY = true;
    /**
     * The layout for all Buttons.
     */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    /**
     * The underlying form.
     */
    private final SelectedBuergerPartnerForm partnerForm = new SelectedBuergerPartnerForm() {
        @Override
        public void setBuerger(Buerger buerger) {
            getButtonLayout().setVisible(buerger != null);
            super.setBuerger(buerger);
        }
    };
    /**
     * The button for the update action.
     */
    private final ActionButton detailButton = new ActionButton(Buerger.class, SimpleAction.read);
    /**
     * The navigation for the detail aciton.
     */
    private final NavigateActions detailNavigation;

    /**
     * Create a new ReadForm for the Partner of a Buerger. It will be empty when no partner is present.
     *
     * @param navigateToDetail The View to navigate to on detail button click.
     */
    public BuergerPartnerReadForm(String navigateToDetail) {
        partnerForm.reLoadBuerger();
        detailNavigation = new NavigateActions(navigateToDetail);

        init();
        setIds();
    }

    private void setIds() {
        setId(getClass().getSimpleName());
        getPartnerForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
        getPartnerForm().setId(getId() + "#form");
        getDetailButton().setId(getId() + "#detail-button-" + getDetailNavigation().getNavigateTo());
    }

    /**
     * Get the underlying Partner Form.
     *
     * @return The Form used by this component.
     */
    public SelectedBuergerPartnerForm getPartnerForm() {
        return partnerForm;
    }

    /**
     * Initialize the ReadForm.
     */
    private void init() {
        getPartnerForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getDetailButton());

        configureDetailButton();

        getPartnerForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getPartnerForm());
    }

    /**
     * Configure the Detail Button.
     */
    private void configureDetailButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getPartnerForm()::getBuerger);
        getDetailButton().addActionPerformer(singleActions::read);
        getDetailButton().addActionPerformer(getDetailNavigation()::navigate);
    }

    /**
     * Get the layout for the Buttons.
     *
     * @return The horizontal Layout.
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the Detail Button.
     *
     * @return The button for the detail action.
     */
    public ActionButton getDetailButton() {
        return detailButton;
    }

    /**
     * Get the Navigation for the detail action.
     *
     * @return The NavigateActions for detail.
     */
    public NavigateActions getDetailNavigation() {
        return detailNavigation;
    }
}
