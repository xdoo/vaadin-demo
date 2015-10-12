package de.muenchen.vaadin.ui.components.forms.node;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.*;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
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
        binder.setItemDataSource(new Buerger());
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

    private BeanFieldGroup<Buerger> getBinder() {
        return binder;
    }

    public Buerger getBuerger() {
        try {
            getBinder().commit();
        } catch (FieldGroup.CommitException e) {
            throw new AssertionError("ItemDataSource must be set?! See constructor. OR!! now because of validation...");
        }
        return getBinder().getItemDataSource().getBean();
    }

    public void setBuerger(Buerger buerger) {
        if (buerger == null)
            throw new NullPointerException();
        getBinder().setItemDataSource(buerger);
    }

    public FormLayout getFormLayout() {
        return formLayout;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getBinder().setReadOnly(true);
    }

    public List<Component> getFields() {
        return Collections.unmodifiableList(fields);
    }

}
