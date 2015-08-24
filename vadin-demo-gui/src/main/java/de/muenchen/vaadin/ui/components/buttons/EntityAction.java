package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple enum declaring all actions and providing Values/Settings that depend on a action.
 */
public enum EntityAction implements Action {

    //TODO check for duplicate/unused actions
    create, read, update, back, save, delete, cancel, copy, logout;

    @Override
    public List<String> getStyleNames() {
        List<String> names = new LinkedList<>();
        if (this == create)
            names.add(ValoTheme.BUTTON_FRIENDLY);
        return names;
    }


    @Override
    public Optional<Integer> getClickShortCut() {
        Integer shortcutAction = null;
        switch (this) {
            case back:  shortcutAction = ShortcutAction.KeyCode.ARROW_LEFT; break;
        }
        return Optional.ofNullable(shortcutAction);

    }

    @Override
    public Optional<FontAwesome> getIcon() {
        FontAwesome resource = null;
        switch (this) {
            case back:      resource = FontAwesome.ANGLE_LEFT; break;
            case update:    resource = FontAwesome.PENCIL; break;
            case create:    resource = FontAwesome.MAGIC; break;
            case delete:    resource = FontAwesome.TRASH_O; break;
            case logout:    resource = FontAwesome.SIGN_OUT; break;
        }
        return Optional.ofNullable(resource);
    }

    @Override
    public <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context) {
        return String.format("%s_%s_%s_BUTTON", navigateTo,this.name().toUpperCase(), context.getBasePath());
    }
}
