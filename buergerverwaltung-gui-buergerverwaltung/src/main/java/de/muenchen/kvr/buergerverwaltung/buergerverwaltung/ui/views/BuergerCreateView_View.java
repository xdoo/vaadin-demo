package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger.Buerger_CreateForm;

@SpringView(name = BuergerCreateView_View.NAME)
@UIScope
public class BuergerCreateView_View extends DefaultView{				
	public static final String NAME = "buergerCreateView";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.buergerCreateView.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Label buerger__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".buerger.label"));
		buerger__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_CreateForm buerger = new Buerger_CreateForm(Buergerverwaltung_View.NAME
		);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(buerger__label, buerger);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
