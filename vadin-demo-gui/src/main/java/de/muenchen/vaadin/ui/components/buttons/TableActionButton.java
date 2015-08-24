package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by p.mueller on 21.08.15.
 */
public class TableActionButton extends ActionButton {

    private final Object id;
    private final BeanItemContainer<Buerger> container;

    /**
     * Create a new EntityButton with the specified context and the Action.
     * <p>
     * It will contain no clicklistener.
     * <p>
     * The navigateTo and from Strings are optional.
     *  @param context
     * @param action
     * @param navigateTo
     * @param container
     */
    private TableActionButton(ControllerContext context, Action action, String navigateTo, Object id, BeanItemContainer<Buerger> container) {
        super("",context, action, navigateTo);
        this.id = id;
        this.container = container;
    }

    public void addItemClickListener(java.util.function.BiConsumer<BeanItemContainer<Buerger>, Object> itemClickListener) {
        addClickListener(clickEvent -> itemClickListener.accept(container,id));
    }

    public static class Builder {
        private final ControllerContext context;
        private final Action action;
        private final String navigateTo;
        private final List<BiConsumer<BeanItemContainer<Buerger>, Object>> itemClickListeners = new ArrayList<>();


        private Builder(ControllerContext context, Action action, String navigateTo, BiConsumer<BeanItemContainer<Buerger>, Object>  itemClickListener) {
            this.context = context;
            this.action = action;
            this.navigateTo = navigateTo;
            itemClickListeners.add(itemClickListener);
        }


        public static Builder make(ControllerContext context, Action action, String navigateTo, BiConsumer<BeanItemContainer<Buerger>, Object>  itemClickListener) {
            Builder builder = new Builder(context,action,navigateTo, itemClickListener);
            return builder;
        }

        public TableActionButton build( BeanItemContainer<Buerger> container, Object id) {
            TableActionButton button =  new TableActionButton(context,action,navigateTo,id,container);
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
