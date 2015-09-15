package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.data.util.BeanContainer;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import org.springframework.hateoas.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Provides an ActionButton to be inserted into a table.
 * <p>
 * It has no text label. {@see TableAction}
 */
public class TableActionButton<T> extends ActionButton {

    /**
     * The id in the container
     */
    private final Resource<T> id;
    /**
     * This container contains :) all items.
     */
    private final BeanContainer<Resource<T>, T> container;

    /**
     * Create a new TableActionButton with the specified context, action and //TODO navigateTo String.
     * Also the container of the table data with the specific id this button belongs to is set.
     * <p>
     * It won't have any ClickListener.
     *
     * @param context    //TODO used to generate the id
     * @param action     Action the button should represent (is styled for).
     * @param navigateTo //TODO used to generate the id
     */
    private TableActionButton(ControllerContext context, Action action, String navigateTo, Resource<T> id, BeanContainer<Resource<T>, T> container) {
        super("", context, action, navigateTo);
        this.id = id;
        this.container = container;
    }

    /**
     * Add an ClickListener for the specific item this button is bound to.
     *
     * @param itemClickListener The listener handles a click on the button, it can use the container and the id ( == Object) to figure out bound object/row.
     */
    public void addItemClickListener(java.util.function.BiConsumer<BeanContainer<Resource<T>, T>, Resource<T>> itemClickListener) {
        addClickListener(clickEvent -> itemClickListener.accept(container, id));
    }

    /**
     * Builder for a TableActionButton.
     * <p>
     * This can be used to write the functionality of the button before the specific item id and container are assigned.
     * <p>
     * The {@link de.muenchen.vaadin.ui.components.buttons.TableActionButton.Builder#build(BeanContainer, Resource)} method
     * is used to finally set the id and container and get the desired button.
     */
    public static class Builder<T> {
        private final ControllerContext context;
        private final Action action;
        private final String navigateTo;
        private final List<BiConsumer<BeanContainer<Resource<T>, T>, Object>> itemClickListeners = new ArrayList<>();

        private Builder(ControllerContext context, Action action, String navigateTo, BiConsumer<BeanContainer<Resource<T>, T>, Object> itemClickListener) {
            this.context = context;
            this.action = action;
            this.navigateTo = navigateTo;
            itemClickListeners.add(itemClickListener);
        }


        /**
         * Start building a {@link TableActionButton}. At the build state the item id and container are not yet defined.
         * By using the build() method they will be assigned and the Button be created.
         *
         * @param context //TODO used for identifier
         * @param action Action the button should represent (is styled for).
         * @param navigateTo  //TODO used for identifier
         * @param itemClickListener The ClickListener that will perform the onclick, it can access its assigned Object
         *                          with the container and the id.
         * @return An (almost ready) Builder for the Button.
         */
        public static <T> Builder make(ControllerContext context, Action action, String navigateTo, BiConsumer<BeanContainer<Resource<T>, T>, Resource<T>> itemClickListener) {
            Builder builder = new Builder(context, action, navigateTo, itemClickListener);
            return builder;
        }

        public TableActionButton build(BeanContainer<Resource<T>, T> container, Resource<T> id) {
            TableActionButton button = new TableActionButton(context, action, navigateTo, id, container);
            itemClickListeners.forEach(button::addItemClickListener);
            return button;
        }

        public ControllerContext getContext() {
            return context;
        }

        public Action getAction() {
            return action;
        }

        public String getNavigateTo() {
            return navigateTo;
        }


    }
}
