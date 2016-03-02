package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter.Sachbearbeiter_CreateForm;

@SpringView(name = SachbearbeiterCreateView_View.NAME)
@UIScope
public class SachbearbeiterCreateView_View extends DefaultView{				
	public static final String NAME = "sachbearbeiterCreateView";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.sachbearbeiterCreateView.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label sach__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".sach.label"));
		sach__label.addStyleName(ValoTheme.LABEL_H2);
		final Sachbearbeiter_CreateForm sach = new Sachbearbeiter_CreateForm(Sachbearbeiterverwaltung_View.NAME
		);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(sach__label, sach);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
