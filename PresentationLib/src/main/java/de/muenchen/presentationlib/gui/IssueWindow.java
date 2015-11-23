package de.muenchen.presentationlib.gui;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.presentationlib.api.GaiaAccess;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.presentationlib.api.GaiaIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
@SpringComponent
@UIScope
public class IssueWindow extends Window {

    private final Logger LOG = LoggerFactory.getLogger(IssueWindow.class);

    @Autowired
    private static IssueService service;

    private static GaiaAccess access;

    private final I18nResolver resolver;

    private final GaiaAccessForm accessForm = new GaiaAccessForm();
    private final Button repoAccSaveButton;
    private final IssueForm form = new IssueForm();
    private final Button createButton;

    public IssueWindow(){
        this.resolver = BaseUI.getCurrentI18nResolver();

        createButton = new Button(resolver.resolve("issue.button.create"), event -> {
            try {
                GaiaIssue issue = form.getIssue();
                service.createIssue(issue);
                LOG.info("created issue: "+issue.getTitle());
                showSuccessNotification();
                this.close();
            } catch (Exception e){
                LOG.error("error while creating issue: " + e.getClass() + " " + e.getMessage());
                //throw new RuntimeException(e);
                showErrorNotification();
            }
        });
        createButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        FormLayout issueLayout = new FormLayout(form, createButton);
        issueLayout.setVisible(false);

        repoAccSaveButton = new Button(resolver.resolve("gaiaaccess.button.create"), event -> {
            if(accessForm.isVisible()){
                service.login(access.getUsername(), access.getPassword());
                access = accessForm.getRepositoryAccess();
                issueLayout.setVisible(true);
                accessForm.setVisible(false);
            } else {
                issueLayout.setVisible(false);
                accessForm.setVisible(true);
            }
        });
        repoAccSaveButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        if(access!=null) {
            accessForm.setRepositoryAccess(access);

            service.login(access.getUsername(), access.getPassword());
            issueLayout.setVisible(true);
            accessForm.setVisible(false);
        }

        FormLayout layout = new FormLayout(accessForm, repoAccSaveButton, issueLayout);
        layout.setMargin(true);
        this.setContent(layout);
        this.setCaption(resolver.resolve("issue.window.title"));
        this.setClosable(true);
        this.setResizable(false);
        this.setModal(true);
    }

    public void setIssue(String forComponent){
        this.form.setIssue(new GaiaIssue(forComponent, resolver.resolve("issue.body.text") + forComponent + "\n"));
    }

    private void showSuccessNotification() {
        GenericSuccessNotification succes = new GenericSuccessNotification(
                resolver.resolve("issue.notification.success.create.label"),
                resolver.resolve("issue.notification.success.create.text"));
        succes.show(Page.getCurrent());
    }

    private void showErrorNotification() {
        GenericErrorNotification error = new GenericErrorNotification(
                resolver.resolve("issue.notification.error.create.label"),
                resolver.resolve("issue.notification.error.create.text"));
        error.show(Page.getCurrent());
    }

}
