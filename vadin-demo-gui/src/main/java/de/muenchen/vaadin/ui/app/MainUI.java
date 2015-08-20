package de.muenchen.vaadin.ui.app;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.*;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.LogoutEvent;
import de.muenchen.vaadin.ui.components.GenericConfirmationWindow;
import de.muenchen.vaadin.ui.components.buttons.Action;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.demo.api.services.SecurityService;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.MainView;
import de.muenchen.vaadin.ui.app.views.BuergerTableView;
import de.muenchen.vaadin.ui.app.views.BuergerUpdateView;
import de.muenchen.vaadin.ui.app.views.LoginView;
import de.muenchen.vaadin.ui.app.views.events.LoginEvent;
import de.muenchen.vaadin.ui.util.EventBus;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

import javax.servlet.annotation.WebServlet;

@SpringUI
@Title("Vaadin Spring-Security Sample")
@Theme("valo")
@PreserveOnRefresh
//@Widgetset("de.muenchen.vaadin.Widgetset")
public class MainUI extends UI implements ControllerContext {

    private static final Logger LOG = LoggerFactory.getLogger(MainUI.class);

    private static final long serialVersionUID = 5310014981075920878L;

    private final SpringViewProvider viewProvider;
    private final SecurityService security;
    private final MessageService i18n;
    private final EventBus eventBus;

    @Autowired
    public MainUI(SpringViewProvider ViewProvider, SecurityService security, MessageService i18n, EventBus eventBus) {
        LOG.info("starting UI");
        this.viewProvider = ViewProvider;
        this.security = security;
        this.i18n = i18n;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    private final boolean testMode = false;

    protected ValoMenuLayout root = new ValoMenuLayout();
    protected ComponentContainer viewDisplay = root.getContentContainer();
    protected CssLayout menu = new CssLayout();
    protected CssLayout menuItemsLayout = new CssLayout();
    private Navigator navigator;
    private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();

    @Override
    protected void init(VaadinRequest request) {

        // hack
        setLocale(Locale.GERMANY);

        // IE Support
        if (getPage().getWebBrowser().isIE()
                && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }

        // mobile support
        if (!this.testMode) {
            Responsive.makeResponsive(this);
        }

        // build page
        getPage().setTitle(i18n.get("page.title"));
        setContent(root);
        root.setWidth("100%");
        root.addMenu(buildMenu());
        addStyleName(ValoTheme.UI_WITH_MENU);

        // configure navigator
        this.navigator = new Navigator(this, this.viewDisplay);
        this.navigator.addProvider(viewProvider);
        setNavigator(this.navigator);

        // check security
        if (!this.security.isLoggedIn()) {
            this.root.switchOffMenu();
        }

        // add navigator to security Service
//        this.security.setNavigator(this.navigator);


        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {

                LOG.debug("View change to: " + ((event.getViewName().equals(""))?"MainView":event.getViewName()));

                // Check if a user has logged in
                boolean isLoggedIn = security.isLoggedIn();
                boolean isLoginView = event.getNewView() instanceof LoginView;
                boolean fromTable = event.getOldView() instanceof BuergerTableView;
                boolean fromUpdate = event.getOldView() instanceof BuergerUpdateView;
                boolean fromDetail = event.getOldView() instanceof BuergerDetailView;
                if (fromTable) {
                    BuergerTableView old = (BuergerTableView) event.getOldView();
                    old.unRegisterTable();
                }
                if (fromUpdate) {
                    BuergerUpdateView old = (BuergerUpdateView) event.getOldView();
                    old.unRegisterForm();
                }
                if (fromDetail) {
                    BuergerDetailView old = (BuergerDetailView) event.getOldView();
                    old.unRegister();
                }
                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginView.NAME);
                    LOG.info("not logged in");
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    LOG.warn("login view cannot be entered while logged in.");
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                
               /* boolean toTableView = event.getNewView() instanceof BuergerTableView;
                if(toTableView){
                    BuergerTableView tmp = (BuergerTableView) event.getNewView();
                    tmp.setTable(buergerTable);
                }*/
                for (final Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext(); ) {
                    it.next().removeStyleName("selected");
                }
                for (final Entry<String, String> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (final Iterator<Component> it = menuItemsLayout
                                .iterator(); it.hasNext(); ) {
                            final Component c = it.next();
                            if (c.getCaption() != null
                                    && c.getCaption().startsWith(
                                    item.getValue())) {
                                c.addStyleName("selected");
                                break;
                            }
                        }
                        break;
                    }
                }
                menu.removeStyleName("valo-menu-visible");
            }
        });
    }

    @Subscribe
    public void login(LoginEvent event) {
        this.root.switchOnMenu();
        getNavigator().navigateTo(MainView.NAME);

    }

    @Subscribe
    public void logout(LogoutEvent event) {
        this.root.switchOffMenu();
        security.logout();

        // Close the VaadinServiceSession
        getUI().getSession().close();
        // Invalidate underlying session instead if login info is stored there
        VaadinService.getCurrentRequest().getWrappedSession().invalidate();
        LOG.info("logged out");

        // Redirect to avoid keeping the removed UI open in the browser
        getUI().getPage().setLocation("/");
    }

    private CssLayout buildMenu() {
        this.menu.addComponent(this.createMenuTitle());
        this.menu.addComponent(this.createRightCorner());
        this.menu.addComponent(this.createNavigationMenu());
        return menu;
    }

    private Component createMenuTitle() {
        final HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName("valo-menu-title");
        final Label title = new Label(
                "<h3>Vaadin <strong>Valo Theme</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);
        return top;
    }

    private Component createRightCorner() {
        Label label = new Label("foo");
        return label;
    }

    private Component createSettings() {
        return null;
    }

    private Component createNavigationMenu() {

        // Start Menüeinträge
        this.menuItems.put(MainView.NAME, "Haupseite");
        this.menuItems.put(BuergerTableView.NAME, "Bürger Pflege");
        // Ende Menüeinträge

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");

        for (final Entry<String, String> item : menuItems.entrySet()) {
            final Button b = new Button(item.getValue(), new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    navigator.navigateTo(item.getKey());
                }
            });
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            b.setId(String.format("MENU_ITEM_BUTTON_%s", item.getKey()).toUpperCase());
//            b.setIcon(testIcon.get());
            menuItemsLayout.addComponent(b);
        }

        // creates and displays the logout button
        final Button logoutButton = new Button("Logout", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                GenericConfirmationWindow confirmationWindow = new GenericConfirmationWindow(new LogoutEvent(), MainUI.this, Action.logout);
                getUI().addWindow(confirmationWindow);
                confirmationWindow.center();
                confirmationWindow.focus();
            }
        });
        logoutButton.setHtmlContentAllowed(true);
        logoutButton.setPrimaryStyleName("valo-menu-item");
        logoutButton.setId("MENU_ITEM_BUTTON_LOGOUT");
        menuItemsLayout.addComponent(logoutButton);

        return menuItemsLayout;
    }

    @Override
    public String resolveRelative(String relativePath) {
        return i18n.get(relativePath);
    }

    @Override
    public AppEvent<? extends BaseEntity> buildEvent(EventType eventType) {
        return null;
    }

    @Override
    public String getBasePath() {
        return null;
    }

    @Override
    public String resolve(String path) {
        return i18n.get(path);
    }

    @Override
    public void postToEventBus(AppEvent appEvent) {
        eventBus.post(appEvent);
    }
}
