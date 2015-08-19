package de.muenchen.vaadin.ui.components.buttons;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.sun.istack.internal.Nullable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
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

        toIcon(action).ifPresent(button::setIcon);
        toClickShortCut(action).ifPresent(button::setClickShortcut);

        button.addClickListener(
                e -> {
                    toAppEvent(context, action, navigateTo, from).ifPresent(context::postToEventBus);
                    throw new AssertionError("Clicked EntityButton");
                }
        );


        setId(String.format("%s_%s_UPDATE_BUTTON", navigateTo, BuergerViewController.I18N_BASE_PATH));

        setCompositionRoot(button);
    }

    private Optional<AppEvent<E>> toAppEvent(final ControllerContext<E> context, final Action action, final String navigateTo, final String from) {

        AppEvent<E> appEvent = null;
        if (action == Action.back)
            appEvent = context.buildEvent(EventType.CANCEL);

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

        return Optional.ofNullable(resource);
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
}
