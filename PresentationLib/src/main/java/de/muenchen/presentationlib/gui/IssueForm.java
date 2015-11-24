package de.muenchen.presentationlib.gui;

import com.vaadin.data.Validator;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import de.muenchen.presentationlib.api.GaiaIssue;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class IssueForm extends BaseComponent {

        /**
         * The FormLayout that contains all the form fields.
         */
        private final FormLayout formLayout;

        private final TextField title;
        private final TextArea content;

        /**
         * Create a new IssueForm;
         * <p/>
         * This Form is only the plain fields for input, and has no additional components or buttons.
         */
        public IssueForm() {
            title = new TextField("Title");
            content = new TextArea("Body");

            content.setWidth(450, Unit.PIXELS);

            formLayout = new FormLayout(title, content);

            setCompositionRoot(formLayout);
        }

        /**
         * Get the Issue object of this form.
         *
         * @return The Issue.
         */
        public GaiaIssue getIssue() throws Validator.InvalidValueException {
            return new GaiaIssue(title.getValue(), content.getValue());
        }

        /**
         * Set the Issue of this Form.
         *
         * @param issue The new Issue.
         */
        public void setIssue(GaiaIssue issue) {
            title.setValue(issue.getTitle());
            content.setValue(issue.getBody());
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
