package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung.Wohnung_ReadWriteForm;

@SpringView(name = ReadWriteWohnung_View.NAME)
@UIScope
public class ReadWriteWohnung_View extends DefaultView{				
	public static final String NAME = "readWriteWohnung";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.readWriteWohnung.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final ActionButton zurueck = new ActionButton(MainUI.getCurrentI18nResolver().resolve("view_.readWriteWohnung.button.zurueck.label"), SimpleAction.none);
		zurueck.addActionPerformer(new NavigateActions(Wohnungsverwaltung_View.NAME)::navigate);
		zurueck.setId("zurueck-" + Wohnungsverwaltung_View.NAME);
		
		final Label sach__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".sach.label"));
		sach__label.addStyleName(ValoTheme.LABEL_H2);
		final Wohnung_ReadWriteForm sach = new Wohnung_ReadWriteForm();
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(zurueck, sach__label, sach);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
