package de.muenchen.vaadin.ui.components.nodes.forms;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.*;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.components.BaseComponent;
import reactor.bus.EventBus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by p.mueller on 07.10.15.
 */
public class BuergerForm extends BaseComponent {

    public static final Class<Buerger> ENTITY_CLASS = Buerger.class;
    private final FormLayout formLayout;
    private final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<>(ENTITY_CLASS);
    private final List<Component> fields;

    public BuergerForm(I18nResolver i18nResolver, EventBus eventBus) {
        super(i18nResolver, eventBus);
        eventBus.on(new ResponseEntityKey(ENTITY_CLASS).toSelector(), this::update);
        fields = buildFields();

        final FormLayout formLayout = new FormLayout();
        fields.stream().forEach(formLayout::addComponent);

        this.formLayout = formLayout;
        setCompositionRoot(formLayout);
    }

    private List<Component> buildFields() {
        final FormUtil formUtil = new FormUtil(getBinder(), getI18nResolver());

        final TextField vorname = formUtil.createTextField(Buerger.Field.vorname.name());
        final TextField nachname = formUtil.createTextField(Buerger.Field.nachname.name());
        final ComboBox augenfarbe = formUtil.createComboBox(Buerger.Field.augenfarbe.name(), Augenfarbe.class);
        final DateField geburtsdatum = formUtil.createDateField(Buerger.Field.geburtsdatum.name());

        return Arrays.asList(vorname, nachname, augenfarbe, geburtsdatum);
    }

    public BeanFieldGroup<Buerger> getBinder() {
        return binder;
    }

    private void update(reactor.bus.Event<BuergerDatastore> event) {
        final BuergerDatastore datastore = event.getData();
        datastore.getSelectedBuerger().ifPresent(binder::setItemDataSource);
    }

    private Buerger getBuerger() {
        return binder.getItemDataSource().getBean();
    }

    public FormLayout getFormLayout() {
        return formLayout;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getFields().forEach(c -> c.setReadOnly(readOnly));
    }

    public List<Component> getFields() {
        return Collections.unmodifiableList(fields);
    }

}
