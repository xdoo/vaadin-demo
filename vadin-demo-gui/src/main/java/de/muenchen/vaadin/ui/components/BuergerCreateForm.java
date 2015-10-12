package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericWarningNotification;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.Optional;
import java.util.stream.Stream;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.NotificationType;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * Formular zum Erstellen eines {@link Buerger}s.
 * 
 * @author claus.straube
 */
public class BuergerCreateForm extends CustomComponent {

    private final String navigateTo;
    private final String back;
    private final BuergerViewController controller;
    private final BuergerI18nResolver resolver;
    private Optional<String> relation;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen
     * Konstruktor wir die Zielseite für die 'erstellen' Schaltfläche automatisch
     * zur Zielseite für die 'abbrechen' Schaltfläche. Dies ist in den meisten Fällen
     * das gewollte verhalten.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     */
    public BuergerCreateForm(final BuergerViewController controller, BuergerI18nResolver resolver, final String navigateTo, String relation) {
        this(controller, resolver, navigateTo, relation, navigateTo);
        this.createForm();
    }
    
    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden. 
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param back Zielseite nach Druck der 'abbrechen' Schaltfläche
     */
    public BuergerCreateForm(final BuergerViewController controller, BuergerI18nResolver resolver, final String navigateTo, String relation, String back) {
        this.navigateTo = navigateTo;
        this.back = back;
        this.controller = controller;
        this.resolver = resolver;
        this.relation = Optional.ofNullable(relation);
        this.createForm();
    }
    
    /**
     * Erzeugt das eigentliche Formular.
     */
    private void createForm() {
        FormLayout propertyLayout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        propertyLayout.setMargin(true);
        
        // headline
        Label headline = new Label(resolver.resolveRelative(
                getFormPath(SimpleAction.create,
                        I18nPaths.Component.headline,
                        Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        propertyLayout.addComponent(headline);

        // Now use a binder to bind the members
        final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<>(Buerger.class);
        binder.setItemDataSource(new Buerger());


        binder.setFieldFactory(new DefaultFieldGroupFieldFactory(){
            @SuppressWarnings("unchecked")
            @Override
            public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
                if (Enum.class.isAssignableFrom(type)) {
                    return (T) super.createField(type, ComboBox.class);
                }
                return super.createField(type, fieldType);
            }
        });

        // Felder der CreateForm mit Validierung
        Stream.of(Buerger.Field.getProperties()).forEach(entityField -> {
            AbstractField field = (AbstractField)binder.buildAndBind(
                    resolver.resolveRelative(getEntityFieldPath(entityField, Type.label)),
                    entityField
            );
            //HACK
            //Make validation only visible after CHANGE or COMMIT
            field.setValidationVisible(false);
            field.addValueChangeListener(event -> field.setValidationVisible(true));
            binder.addCommitHandler(new FieldGroup.CommitHandler() {
                @Override
                public void preCommit(FieldGroup.CommitEvent commitEvent) throws CommitException {
                    field.setValidationVisible(true);
                }

                @Override
                public void postCommit(FieldGroup.CommitEvent commitEvent) throws CommitException {

                }
            });
            //HACK-END

            propertyLayout.addComponent(field);
        });

        propertyLayout.addComponent(buttonLayout);
        // die 'speichern' Schaltfläche
        String createLabel = resolver.resolveRelative(
                getFormPath(SimpleAction.create,
                        I18nPaths.Component.button,
                        Type.label));
        Button createButton = new Button(createLabel, (ClickEvent click) -> {
            try {
                binder.commit();

                if (getRelation().isPresent()) {
                    final Association<Buerger> buergerAssociation = new Association<>(binder.getItemDataSource().getBean(), getRelation().get());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), buergerAssociation.asEvent());
                } else {
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.CREATE), reactor.bus.Event.wrap(binder.getItemDataSource().getBean()));
                }
                getNavigator().navigateTo(getNavigateTo());

                binder.setItemDataSource(new Buerger());
            } catch (CommitException | Validator.InvalidValueException e) {
                GenericWarningNotification warn = new GenericWarningNotification(
                        resolver.resolveRelative(getNotificationPath(NotificationType.warning, SimpleAction.save, Type.label)),
                        resolver.resolveRelative(getNotificationPath(NotificationType.warning, SimpleAction.save, Type.text)));
                warn.show(Page.getCurrent());
            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        createButton.setIcon(FontAwesome.MAGIC);
        createButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonLayout.addComponent(createButton);
        // die 'abbrechen' Schaltfläche

        ActionButton back = new ActionButton(resolver, SimpleAction.back, "lsadf");
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

        setCompositionRoot(propertyLayout);
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateBack() {
        return back;
    }

    public Optional<String> getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = Optional.ofNullable(relation);
    }

    private Navigator getNavigator() {
        return getController().getNavigator();
    }
}
