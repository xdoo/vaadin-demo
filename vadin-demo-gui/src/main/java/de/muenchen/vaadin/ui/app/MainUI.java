package de.muenchen.vaadin.ui.app;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.selector.Keys;
import de.muenchen.vaadin.demo.apilib.services.SecurityService;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.ValoMenuLayout;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.guilib.services.MessageService;
import de.muenchen.vaadin.ui.app.views.BuergerTableView;
import de.muenchen.vaadin.ui.app.views.LoginView;
import de.muenchen.vaadin.ui.app.views.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.bus.EventBus;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import static reactor.bus.Event.wrap;

@SpringUI
@Title("Vaadin Spring-Security Sample")
@Theme("valo")
@PreserveOnRefresh
//@Widgetset("de.muenchen.vaadin.Widgetset")
public class MainUI extends UI implements I18nResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MainUI.class);

    private static final long serialVersionUID = 5310014981075920878L;

    private final SpringViewProvider viewProvider;
    private final SecurityService security;
    private final MessageService i18n;
    private final boolean testMode = false;
    private final LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();
    protected ValoMenuLayout root = new ValoMenuLayout();
    protected ComponentContainer viewDisplay = root.getContentContainer();
    protected CssLayout menu = new CssLayout();
    protected CssLayout menuItemsLayout = new CssLayout();
    @Autowired
    private EventBus eventBus;
    private Navigator navigator;

    @Autowired
    public MainUI(SpringViewProvider ViewProvider, SecurityService security, MessageService i18n) {
        LOG.info("starting UI");
        this.viewProvider = ViewProvider;
        this.security = security;
        this.i18n = i18n;
    }

    @Override
    protected void init(VaadinRequest request) {

        // Set Browser Locale
        setLocale(getPage().getWebBrowser().getLocale());
        i18n.setLocale(getPage().getWebBrowser().getLocale());

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
        VerticalLayout componentContainer = new VerticalLayout();
        viewDisplay.addComponent(buildMenuBar());
        viewDisplay.addComponent(componentContainer);
        this.navigator = new Navigator(this, componentContainer);
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

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    security.logout();
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

        eventBus.on(Keys.LOGIN.toSelector(), this::loginEventHandler);
        eventBus.on(Keys.LOGOUT.toSelector(), this::logoutEventHandler);
    }

    public void loginEventHandler(reactor.bus.Event<?> event) {
        this.root.switchOnMenu();
        getNavigator().navigateTo(MainView.NAME);
    }

    public void logoutEventHandler(reactor.bus.Event<?> event) {
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

    private MenuBar buildMenuBar() {
        MenuBar bar = new MenuBar();
        bar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        addLanguageSelector(bar);
        return bar;
    }

    private MenuBar addLanguageSelector(MenuBar bar) {
        MenuBar.MenuItem language = bar.addItem("Sprache", FontAwesome.LANGUAGE, null);

        MenuBar.Command languageSelection = selectedItem -> i18n.getSupportedLocales().stream().forEach(locale -> {
            if (selectedItem.getText().equals(locale.getDisplayLanguage())) {
                i18n.setLocale(locale);
                postEvent(Keys.REFRESH);
            }
        });

        i18n.getSupportedLocales().stream().forEach(locale -> language.addItem(locale.getDisplayLanguage(), null, languageSelection));
        return bar;
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
            final Button b = new Button(item.getValue(), event -> {
                navigator.navigateTo(item.getKey());
            });
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            b.setId(String.format("MENU_ITEM_BUTTON_%s", item.getKey()).toUpperCase());
//            b.setIcon(testIcon.get());
            menuItemsLayout.addComponent(b);
        }

        // creates and displays the logout button
        final Button logoutButton = new Button("Logout", event -> {
            GenericConfirmationWindow confirmationWindow =
                    new GenericConfirmationWindow(MainUI.this,
                            SimpleAction.logout,
                            e -> this.postEvent(Keys.LOGOUT));
            getUI().addWindow(confirmationWindow);
            confirmationWindow.center();
            confirmationWindow.focus();
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
    public FontAwesome resolveIcon(String relativePath) {
        return null;
    }

    public void postEvent(Object event) {
        eventBus.notify(event, wrap(event));
    }

    @Override
    public String getBasePath() {
        return null;
    }

    @Override
    public String resolve(String path) {
        return i18n.get(path);
    }

}
