package de.muenchen.vaadin.ui.components.buttons;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.sun.istack.internal.Nullable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

import java.util.Optional;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

/**
 * Provides a Generic Button for an complete Entity that represents a specific action.
 *
 * @author p.mueller
 */
public class EntityButton<E extends BaseEntity> extends CustomComponent {

    private E entity;

    /**
     * Create a new EntityButton with the specified context and the Action.
     *
     * The navigateTo and from Strings are optional.
     * @param context
     * @param action
     * @param navigateTo
     * @param from
     */
    private EntityButton(final ControllerContext<E> context, final Action action, @Nullable final String navigateTo, @Nullable final String from) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        final String label = context.resolve(labelPath);

        Button button = new Button(label);

        action.getIcon().ifPresent(button::setIcon);
        action.getClickShortCut().ifPresent(button::setClickShortcut);
        action.getStyleName().ifPresent(button::setStyleName);

        button.addClickListener(
                e -> {
                    action.getAppEvent(context,getEntity(), navigateTo, from).ifPresent(context::postToEventBus);
                }
        );

        //setId(String.format("%s_%s_UPDATE_BUTTON", navigateTo, BuergerViewController.I18N_BASE_PATH));

        setCompositionRoot(button);
    }
/**
    private Optional<String> toStyleName(Action action) {
        String styleName = null;
        if (action == Action.create)
            styleName = ValoTheme.BUTTON_FRIENDLY;

        return Optional.ofNullable(styleName);
    }

    private Optional<AppEvent<E>> toAppEvent(final ControllerContext<E> context, final Action action, final String navigateTo, final String from) {

        AppEvent<E> appEvent = null;
        if (action == Action.back)
            appEvent = context.buildEvent(EventType.CANCEL);
        if (action == Action.update)
            appEvent = context.buildEvent(EventType.SELECT2UPDATE).setEntity(entity);
        if (action == Action.create)
            appEvent = context.buildEvent(EventType.CREATE);

        if (appEvent != null) {
            appEvent.navigateTo(navigateTo);
            appEvent.from(from);
        }
        return Optional.ofNullable(appEvent);
    }


    private Optional<Integer> toClickShortCut(Action action) {
        Integer shortcutAction = null;
        if (action == Action.back)
            shortcutAction = ShortcutAction.KeyCode.ARROW_LEFT;

        return Optional.ofNullable(shortcutAction);

    }

    private Optional<Resource> toIcon(Action action) {
        Resource resource = null;
        if (action == Action.back)
            resource = FontAwesome.ANGLE_LEFT;
        if (action == Action.update)
            resource = FontAwesome.PENCIL;
        if (action == Action.create)
            resource = FontAwesome.MAGIC;

        return Optional.ofNullable(resource);
    }
*/
    public E getEntity() {
        return entity;
    }

    public static class Builder<E extends BaseEntity> {
        private final ControllerContext<E> controllerContext;
        private final Action action;
        private String navigateTo;
        private String from;

        private Builder(ControllerContext<E> controllerContext, Action action) {
            this.controllerContext = controllerContext;
            this.action = action;
            this.navigateTo = null;
            this.from = null;
        }

        public Builder<E> navigateTo(String navigateTo) {
            this.navigateTo = navigateTo;
            return this;
        }

        public Builder<E> from(String from) {
            this.from = from;
            return this;
        }

        public EntityButton<E> build() {
            return new EntityButton<E>(controllerContext, action, navigateTo, from);
        }

    }

    public static <T extends BaseEntity> Builder<T> make(ControllerContext<T> controllerContext, Action action) {
        return new Builder<T>(controllerContext, action);
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }
}
