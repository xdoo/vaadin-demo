package de.muenchen.presentationlib.gui;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;
import de.muenchen.presentationlib.api.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class IssueWindow extends Window {

    private final Logger LOG = LoggerFactory.getLogger(IssueWindow.class);

    private IssueService service = new IssueServiceImpl();

    private final I18nResolver resolver;

    private final IssueForm form = new IssueForm();
    private final Button createButton;

    public IssueWindow(String issueFor){
        this.resolver = BaseUI.getCurrentI18nResolver();

        form.setIssue(new Issue(issueFor, resolver.resolve("issue.content.text")+issueFor+"\n"));
        createButton = new Button(resolver.resolve("issue.button.create"), event -> {
            try {
                Issue issue = form.getIssue();
                service.createIssue(issue);
                LOG.info("created issue:"+issue.getTitle());
                showSuccessNotification();
                this.close();
            } catch (Exception e){
                LOG.error("error while creating issue: "+e.getClass()+" "+e.getMessage());
                showErrorNotification();
            }

        });
        createButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        FormLayout layout = new FormLayout(form, createButton);
        layout.setMargin(true);
        this.setContent(layout);
        this.setCaption(resolver.resolve("issue.window.title"));
        this.setClosable(true);
        this.setResizable(false);
        this.setModal(true);
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
