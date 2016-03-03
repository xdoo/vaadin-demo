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
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;



import de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.MainUI;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung.Wohnung_Grid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Wohnung_ViewController;

@SpringView(name = Wohnungsverwaltung_View.NAME)
@UIScope
public class Wohnungsverwaltung_View extends DefaultView{				
	public static final String NAME = "wohnungsverwaltung";

	@Autowired
	private Wohnung_ViewController wohnungController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.wohnungsverwaltung.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label wohnungen__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".wohnungen.label"));
		wohnungen__label.addStyleName(ValoTheme.LABEL_H2);
		final Wohnung_Grid wohnungen = new Wohnung_Grid(wohnungController
		);
		wohnungen.activateSearch().activateCopy().activateDelete();
		wohnungen.activateCreate(WohnungCreate_View.NAME);
		wohnungen.activateRead(ReadWriteWohnung_View.NAME).activateDoubleClickToRead(ReadWriteWohnung_View.NAME);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(wohnungen__label, wohnungen);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
