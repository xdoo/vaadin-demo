package de.muenchen.vaadin.ui.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
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
import de.muenchen.vaadin.ui.app.views.MainView;
import de.muenchen.vaadin.ui.app.views.DefaultPersonView;
import de.muenchen.vaadin.ui.app.views.SamplePersonView;
import de.muenchen.vaadin.ui.app.views.SamplePersonView1;
import de.muenchen.vaadin.ui.app.views.SecuredView;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import org.vaadin.spring.annotation.VaadinUI;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.navigator.SpringViewProvider;

@VaadinUI
@Title("Vaadin Spring-Security Sample")
@Theme("valo")
//@Widgetset("de.muenchen.vaadin.Widgetset")
public class MainUI extends UI {

    private static final long serialVersionUID = 5310014981075920878L;

    @Autowired
    private SpringViewProvider ViewProvider;

    @Autowired
    I18N i18n;

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
        getPage().setTitle(i18n.get("page.title", (Object) null));
        setContent(root);
        root.setWidth("100%");
        root.addMenu(buildMenu());
        addStyleName(ValoTheme.UI_WITH_MENU);

        // configure navigator
        this.navigator = new Navigator(this, this.viewDisplay);
        this.navigator.addProvider(ViewProvider);
        setNavigator(this.navigator);

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                for (final Iterator<Component> it = menuItemsLayout.iterator(); it
                        .hasNext();) {
                    it.next().removeStyleName("selected");
                }
                for (final Entry<String, String> item : menuItems.entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (final Iterator<Component> it = menuItemsLayout
                                .iterator(); it.hasNext();) {
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
        this.menuItems.put(MainView.NAME, "Haupseite");
        this.menuItems.put(SamplePersonView.NAME, "Personen Pflege 1");
        this.menuItems.put(SamplePersonView1.NAME, "Person Pflege 2");
        this.menuItems.put(SecuredView.NAME, "Sichere Seite");

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
//            b.setIcon(testIcon.get());
            menuItemsLayout.addComponent(b);
        }

        return menuItemsLayout;
    }
}
