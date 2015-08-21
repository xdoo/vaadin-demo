package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.components.GenericConfirmationWindow;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by p.mueller on 20.08.15.
 */
public enum EntityTableAction implements Action{
    tablecopy,tabledelete,tabledetail,tableedit;

    @Override
    public List<String> getStyleNames() {
        List<String> names = new LinkedList<>();
        names.add(ValoTheme.BUTTON_ICON_ONLY);

        switch (this) {
            case tabledelete:
                names.add(ValoTheme.BUTTON_DANGER);
                break;
        }
        return names;
    }

    @Override
    public Optional<FontAwesome> getIcon() {
        FontAwesome resource = null;

        switch (this) {
            case tablecopy: resource = FontAwesome.COPY; break;
            case tabledelete: resource = FontAwesome.TRASH_O; break;
        }

        return Optional.ofNullable(resource);
    }

    @Override
    public <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context) {
        return String.format("%s_%s_%s_TABLE_BUTTON", navigateTo,this.name().toUpperCase(), context.getBasePath());
    }
}
