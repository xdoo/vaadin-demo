package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;



import de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.MainUI;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter.Sachbearbeiter_ReadWriteForm;

@SpringView(name = ReadWriteSachbearbeiter_View.NAME)
@UIScope
public class ReadWriteSachbearbeiter_View extends DefaultView{				
	public static final String NAME = "readWriteSachbearbeiter";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.readWriteSachbearbeiter.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final ActionButton zurueck = new ActionButton(MainUI.getCurrentI18nResolver().resolve("view_.readWriteSachbearbeiter.button.zurueck.label"), SimpleAction.none);
		zurueck.addActionPerformer(new NavigateActions(Sachbearbeiterverwaltung_View.NAME)::navigate);
		zurueck.setId("zurueck-" + Sachbearbeiterverwaltung_View.NAME);
		
		final Label sach__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".sach.label"));
		sach__label.addStyleName(ValoTheme.LABEL_H2);
		final Sachbearbeiter_ReadWriteForm sach = new Sachbearbeiter_ReadWriteForm();
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(zurueck, sach__label, sach);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
