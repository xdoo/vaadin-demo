package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Sachbearbeiter_AddGrid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Sachbearbeiter_ViewController;

@SpringView(name = AddSachbearbeiterForBuerger_View.NAME)
@UIScope
public class AddSachbearbeiterForBuerger_View extends DefaultView{				
	public static final String NAME = "addSachbearbeiterForBuerger";

	@Autowired
	private Buerger_ViewController buergerController;

	@Autowired
	private Sachbearbeiter_ViewController sachbearbeiterController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.addSachbearbeiterForBuerger.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label sach__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".sach.label"));
		sach__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_Sachbearbeiter_AddGrid sach = new Buerger_Sachbearbeiter_AddGrid(sachbearbeiterController, ReadWriteBurger_View.NAME);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(sach__label, sach);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
