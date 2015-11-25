package de.muenchen.presentationlib.gui;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.presentationlib.api.GaiaAccess;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.presentationlib.api.GaiaIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
@SpringComponent
@UIScope
public class IssueWindow extends Window {

    private final Logger LOG = LoggerFactory.getLogger(IssueWindow.class);

    @Autowired
    private IssueService service;

    private static GaiaAccess access;

    private GaiaAccessForm gaiaLoginForm;
    private Button gaiaLoginButton;
    private IssueForm issueForm;
    private Button issueCreateButton;

    public IssueWindow(){
        gaiaLoginForm = new GaiaAccessForm();
        issueForm = new IssueForm();
        issueCreateButton = new Button("Create", event -> {
            try {
                GaiaIssue issue = issueForm.getIssue();
                service.createIssue(issue);
                LOG.info("created issue: "+issue.getTitle());
                showSuccessNotification();
                this.close();
            } catch (Exception e){
                LOG.error("error while creating issue: " + e.getMessage());
                showErrorNotification();
            }
        });
        issueCreateButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        FormLayout issueLayout = new FormLayout(issueForm, issueCreateButton);
        issueLayout.setVisible(false);

        gaiaLoginButton = new Button("GAIA-Login", event -> {
            if(gaiaLoginForm.isVisible()){
                access = gaiaLoginForm.getRepositoryAccess();
                service.login(access.getUsername(), access.getPassword());
                issueLayout.setVisible(true);
                gaiaLoginForm.setVisible(false);
            } else {
                issueLayout.setVisible(false);
                gaiaLoginForm.setVisible(true);
            }
        });
        gaiaLoginButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        if(access!=null) {
            gaiaLoginForm.setRepositoryAccess(access);

            service.login(access.getUsername(), access.getPassword());
            issueLayout.setVisible(true);
            gaiaLoginForm.setVisible(false);
        }

        FormLayout layout = new FormLayout(gaiaLoginForm, gaiaLoginButton, issueLayout);
        layout.setMargin(true);
        this.setContent(layout);
        this.setCaption("Create Issue");
        this.setClosable(true);
        this.setResizable(false);
        this.setModal(true);
    }

    public void setIssue(String forComponent){
        this.issueForm.setIssue(new GaiaIssue(forComponent, "Created in GAIA's Presentation-mode.\nIn View: " + forComponent + "\n"));
    }

    private void showSuccessNotification() {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                "Issue created",
                "Your Issue was created successfully.");
        succes.show(Page.getCurrent());
    }

    private void showErrorNotification() {
        GenericErrorNotification error = new GenericErrorNotification(
                "Error",
                "Issue creation failed.");
        error.show(Page.getCurrent());
    }

}
