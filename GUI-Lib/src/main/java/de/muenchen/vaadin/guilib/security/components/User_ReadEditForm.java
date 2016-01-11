package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;

/**
 * Provides a full-featured Form for a User_. It allows to view the current selected user and edit it.
 * It also shows a List of all Authorities and allows to add and remove them for the selected User.
 *
 * @author p.mueller
 * @version 1.0
 */
public class User_ReadEditForm extends BaseComponent {

    /** The Form displaying the current selected User_. */
    private final User_ReadWriteForm userForm;
    /** The TwinColSelect showing Authorities to add */
    private final User_Authorities_TwinSelect authorityTwinSelect;

    /**
     * Create a new User_ReadWriteForm with the internationalization of the Controller.
     * It will navigate to the navigateBack value on the back button click.
     */
    public User_ReadEditForm(User_ViewController userViewController, Authority_ViewController authorityViewController) {
        userForm = new User_ReadWriteForm();
        authorityTwinSelect = new User_Authorities_TwinSelect(userViewController, authorityViewController);

        init();
    }

    private void init(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();

        layout.addComponents(userForm, authorityTwinSelect);
        setCompositionRoot(layout);
    }



}

