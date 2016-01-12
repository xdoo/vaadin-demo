package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.TwinColSelect;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority;
import de.muenchen.vaadin.demo.apilib.local.User;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_AssociationListActions;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class User_Authorities_TwinSelect extends BaseComponent {

    private final User_ViewController userViewController;
    private final Authority_ViewController authorityViewController;

    private final TwinColSelect select;

    private final Set<Authority> lastSelected = new HashSet<>();

    private final Property.ValueChangeListener saveListener = (event) -> {
        Collection<Authority> eventData = ((Collection<Authority>)event.getProperty().getValue());

        User_AssociationListActions addedPermissions = new User_AssociationListActions(
                () -> eventData.stream().filter(auth -> !lastSelected.contains(auth)).map(auth -> new Association<>(auth, User.Rel.authorities.name())).collect(Collectors.toList()));
        User_AssociationListActions removedPermissions = new User_AssociationListActions(
                () -> lastSelected.stream().filter(auth -> !eventData.contains(auth)).map(auth -> new Association<>(auth, User.Rel.authorities.name())).collect(Collectors.toList()));

        addedPermissions.addAssociations(null);
        removedPermissions.removeAssociations(null);

        lastSelected.clear();
        lastSelected.addAll(eventData);
    };

    public User_Authorities_TwinSelect(User_ViewController userViewController, Authority_ViewController authorityViewController){
        select = new TwinColSelect();

        this.userViewController = userViewController;
        this.authorityViewController = authorityViewController;

        init();
    }

    private void init(){
        BeanItemContainer<Authority> perms = authorityViewController.getModel().getAuthoritys();
        perms.addAll(authorityViewController.queryAuthority());

        perms.sort(new String[]{"authority"}, new boolean[]{true});
        select.setContainerDataSource(perms);

        select.setWidth("100%");
        select.setHeight("384px");
        select.setImmediate(true);

        select(userViewController.getModel().getSelectedUserAuthorities().getItemIds());

        select.addValueChangeListener(saveListener);
        select.setLeftColumnCaption(BaseUI.getCurrentI18nResolver().resolveRelative(User.class, "authorities.available.label"));
        select.setRightColumnCaption(BaseUI.getCurrentI18nResolver().resolveRelative(User.class, "authorities.granted.label"));

        setCompositionRoot(select);
    }

    public void deselectAll(){
        select.removeValueChangeListener(saveListener);

        lastSelected.clear();
        select.setValue(new HashSet<>());

        select.addValueChangeListener(saveListener);
    }

    public void select(List<Authority> authorities){
        select.removeValueChangeListener(saveListener);

        lastSelected.addAll(authorities);
        select.setValue(authorities);

        select.addValueChangeListener(saveListener);
    }

}

