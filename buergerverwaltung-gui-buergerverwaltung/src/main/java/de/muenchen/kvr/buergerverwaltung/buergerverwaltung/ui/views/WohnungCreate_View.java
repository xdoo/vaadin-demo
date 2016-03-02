package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter.Sachbearbeiter_CreateForm;

@SpringView(name = WohnungCreate_View.NAME)
@UIScope
public class WohnungCreate_View extends DefaultView{				
	public static final String NAME = "wohnungCreate";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.wohnungCreate.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label wohnung__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".wohnung.label"));
		wohnung__label.addStyleName(ValoTheme.LABEL_H2);
		final Sachbearbeiter_CreateForm wohnung = new Sachbearbeiter_CreateForm(Wohnungsverwaltung_View.NAME
		);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(wohnung__label, wohnung);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
