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

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter.Sachbearbeiter_Grid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Sachbearbeiter_ViewController;

@SpringView(name = Sachbearbeiterverwaltung_View.NAME)
@UIScope
public class Sachbearbeiterverwaltung_View extends DefaultView{				
	public static final String NAME = "sachbearbeiterverwaltung";

	@Autowired
	private Sachbearbeiter_ViewController sachbearbeiterController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.sachbearbeiterverwaltung.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Sachbearbeiter_Grid component1 = new Sachbearbeiter_Grid(sachbearbeiterController
		);
		component1.activateSearch().activateCopy().activateDelete();
		component1.activateCreate(SachbearbeiterCreateView_View.NAME);
		component1.activateRead(ReadWriteSachbearbeiter_View.NAME).activateDoubleClickToRead(ReadWriteSachbearbeiter_View.NAME);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(component1);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
