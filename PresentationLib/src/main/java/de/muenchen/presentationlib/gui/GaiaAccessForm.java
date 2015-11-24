package de.muenchen.presentationlib.gui;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import de.muenchen.presentationlib.api.GaiaAccess;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class GaiaAccessForm extends BaseComponent {

    /**
     * The FormLayout that contains all the form fields.
     */
    private final FormLayout formLayout;

    private final TextField username;
    private final PasswordField password;

    /**
     * Create a new RepositoryAccessForm;
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons.
     */
    public GaiaAccessForm() {
        username = new TextField("GAIA-username");
        password = new PasswordField("GAIA-password");

        username.setWidth(400, Unit.PIXELS);
        password.setWidth(400, Unit.PIXELS);

        formLayout = new FormLayout(username, password);

        setCompositionRoot(formLayout);
    }

    public GaiaAccess getRepositoryAccess() {
        return new GaiaAccess(
            username.getValue(),
            password.getValue()
        );
    }

    public void setRepositoryAccess(GaiaAccess gaiaaccess) {
        username.setValue(gaiaaccess.getUsername());
        password.setValue(gaiaaccess.getPassword());
    }

    /**
     * Get the layout of this form, containing all the Fields.
     *
     * @return The base Layout.
     */
    public FormLayout getFormLayout() {
            return formLayout;
        }
}
