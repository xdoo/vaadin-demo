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
                e -> action.getAppEvent(context, getEntity(), navigateTo, from).ifPresent(context::postToEventBus)
        );

        //TODO add IDs
        //setId(String.format("%s_%s_UPDATE_BUTTON", navigateTo, BuergerViewController.I18N_BASE_PATH));

        setCompositionRoot(button);
    }

    public E getEntity() {
        return entity;
    }

    /**
     * Builder for a BaseEntity use the make() method.
     * @param <E> the entity the button is for.
     */
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

    /**
     * Make a new EntityButton for the specified action.
     *
     * It is possible, but not required to set the navigateTo or from values.
     * The controllerContext will be used to resolve the label and post the on-click Events.
     *
     * @param controllerContext the context providing all the needed information / interfaces
     * @param action the action the button will do. (Also influences Text and Style)
     * @param <T> The type of Entity
     * @return an unfinished builder to build() or set additional properties.
     */
    public static <T extends BaseEntity> Builder<T> make(ControllerContext<T> controllerContext, Action action) {
        return new Builder<T>(controllerContext, action);
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }
}
