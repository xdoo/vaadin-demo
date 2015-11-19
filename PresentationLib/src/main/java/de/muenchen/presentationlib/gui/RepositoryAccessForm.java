package de.muenchen.presentationlib.gui;

import com.vaadin.data.Validator;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import de.muenchen.presentationlib.api.Issue;
import de.muenchen.presentationlib.api.RepositoryAccess;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class RepositoryAccessForm extends BaseComponent {

        /**
         * The FormLayout that contains all the form fields.
         */
        private final FormLayout formLayout;

        private final TextField repoUrl;
        private final TextField username;
        private final PasswordField password;

        /**
         * Create a new RepositoryAccessForm;
         * <p/>
         * This Form is only the plain fields for input, and has no additional components or buttons.
         */
        public RepositoryAccessForm() {
            I18nResolver resolver = BaseUI.getCurrentI18nResolver();
            repoUrl = new TextField(resolver.resolve("repositoryaccess.repourl.label"));
            repoUrl.setValue("https://api.github.com/repos/<user>/<repo>/issues");
            username = new TextField(resolver.resolve("repositoryaccess.username.label"));
            password = new PasswordField(resolver.resolve("repositoryaccess.password.label"));

            repoUrl.setWidth(400, Unit.PIXELS);
            username.setWidth(400, Unit.PIXELS);
            password.setWidth(400, Unit.PIXELS);

            formLayout = new FormLayout(repoUrl, username, password);

            setCompositionRoot(formLayout);
        }

        public RepositoryAccess getRepositoryAccess() {
            return new RepositoryAccess(
                repoUrl.getValue(),
                username.getValue(),
                password.getValue()
            );
        }

        public void setRepositoryAccess(RepositoryAccess repositoryAccess) {
            repoUrl.setValue(repositoryAccess.getRepoUrl());
            username.setValue(repositoryAccess.getUsername());
            password.setValue(repositoryAccess.getPassword());
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
