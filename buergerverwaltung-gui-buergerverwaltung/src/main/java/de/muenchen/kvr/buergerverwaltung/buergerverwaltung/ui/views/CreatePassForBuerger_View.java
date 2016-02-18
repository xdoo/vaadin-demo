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



import de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.MainUI;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Pass_CreateForm;

@SpringView(name = CreatePassForBuerger_View.NAME)
@UIScope
public class CreatePassForBuerger_View extends DefaultView{				
	public static final String NAME = "createPassForBuerger";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.createPassForBuerger.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Buerger_Pass_CreateForm component1 = new Buerger_Pass_CreateForm(ReadWriteBurger_View.NAME, Buerger_.Rel.pass.name());
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(component1);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
