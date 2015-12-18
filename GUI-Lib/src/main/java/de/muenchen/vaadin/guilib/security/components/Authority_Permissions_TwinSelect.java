package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.TwinColSelect;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.demo.apilib.local.Permission_;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_AssociationListActions;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Authority_Permissions_TwinSelect extends BaseComponent {
    private final Permission_ViewController controller;

    private final TwinColSelect select;

    private final Set<Permission_> lastSelected = new HashSet<>();

    private final Property.ValueChangeListener saveListener = (event) -> {
        Collection<Permission_> eventData = ((Collection<Permission_>)event.getProperty().getValue());

        Authority_AssociationListActions addedPermissions = new Authority_AssociationListActions(
                () -> eventData.stream().filter(perm -> !lastSelected.contains(perm)).map(perm -> new Association<>(perm, Authority_.Rel.permissions.name())).collect(Collectors.toList()));
        Authority_AssociationListActions removedPermissions = new Authority_AssociationListActions(
                () -> lastSelected.stream().filter(perm -> !eventData.contains(perm)).map(perm -> new Association<>(perm, Authority_.Rel.permissions.name())).collect(Collectors.toList()));

        addedPermissions.addAssociations(null);
        removedPermissions.removeAssociations(null);

        lastSelected.clear();
        lastSelected.addAll(eventData);
    };

    public Authority_Permissions_TwinSelect(Permission_ViewController controller){
        select = new TwinColSelect();

        this.controller = controller;

        init();
    }

    private void init(){
        controller.getModel().getPermissions().addAll(controller.queryPermission());
        BeanItemContainer<Permission_> perms = controller.getModel().getPermissions();
        perms.sort(new String[]{"permission"}, new boolean[]{true});
        select.setContainerDataSource(perms);
        select.setWidth("100%");
        select.setHeight("484px");
        select.setImmediate(true);
        select.addValueChangeListener(saveListener);
        select.setLeftColumnCaption(BaseUI.getCurrentI18nResolver().resolveRelative(Authority_.class, "permissions.available.label"));
        select.setRightColumnCaption(BaseUI.getCurrentI18nResolver().resolveRelative(Authority_.class, "permissions.granted.label"));

        setCompositionRoot(select);
    }

    public void deselectAll(){
        select.removeValueChangeListener(saveListener);

        lastSelected.clear();
        select.setValue(new HashSet<>());

        select.addValueChangeListener(saveListener);
    }

    public void select(List<Permission_> permissions){
        select.removeValueChangeListener(saveListener);

        lastSelected.addAll(permissions);
        select.setValue(permissions);

        select.addValueChangeListener(saveListener);
    }

}

