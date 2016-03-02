package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass.Pass_Grid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Pass_ViewController;

@SpringView(name = Passverwaltung_View.NAME)
@UIScope
public class Passverwaltung_View extends DefaultView{				
	public static final String NAME = "passverwaltung";

	@Autowired
	private Pass_ViewController passController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.passverwaltung.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label pass__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".pass.label"));
		pass__label.addStyleName(ValoTheme.LABEL_H2);
		final Pass_Grid pass = new Pass_Grid(passController
		);
		pass.activateSearch().activateCopy().activateDelete();
		pass.activateCreate(PassCreateView_View.NAME);
		pass.activateRead(ReadWritePass_View.NAME).activateDoubleClickToRead(ReadWritePass_View.NAME);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(pass__label, pass);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
