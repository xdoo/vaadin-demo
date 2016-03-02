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

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass.Pass_ReadWriteForm;

@SpringView(name = ReadWritePass_View.NAME)
@UIScope
public class ReadWritePass_View extends DefaultView{				
	public static final String NAME = "readWritePass";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.readWritePass.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final ActionButton zurueck = new ActionButton(MainUI.getCurrentI18nResolver().resolve("view_.readWritePass.button.zurueck.label"), SimpleAction.none);
		zurueck.addActionPerformer(new NavigateActions(Passverwaltung_View.NAME)::navigate);
		zurueck.setId("zurueck-" + Passverwaltung_View.NAME);
		
		final Label pass__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".pass.label"));
		pass__label.addStyleName(ValoTheme.LABEL_H2);
		final Pass_ReadWriteForm pass = new Pass_ReadWriteForm();
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(zurueck, pass__label, pass);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
