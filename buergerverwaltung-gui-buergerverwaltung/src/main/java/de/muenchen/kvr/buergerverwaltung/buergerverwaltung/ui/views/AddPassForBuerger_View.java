package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Pass_AddGrid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Pass_ViewController;

@SpringView(name = AddPassForBuerger_View.NAME)
@UIScope
public class AddPassForBuerger_View extends DefaultView{				
	public static final String NAME = "addPassForBuerger";

	@Autowired
	private Buerger_ViewController buergerController;

	@Autowired
	private Pass_ViewController passController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.addPassForBuerger.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label pass__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".pass.label"));
		pass__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_Pass_AddGrid pass = new Buerger_Pass_AddGrid(passController, ReadWriteBurger_View.NAME);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(pass__label, pass);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
