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
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;



import de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.MainUI;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger.Buerger_ReadWriteForm;
import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Pass_ReadEditForm;
import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Sachbearbeiter_ReadEditGrid;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;

@SpringView(name = ReadWriteBurger_View.NAME)
@UIScope
public class ReadWriteBurger_View extends DefaultView{				
	public static final String NAME = "readWriteBurger";

	@Autowired
	private Buerger_ViewController buergerController;
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.readWriteBurger.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final Buerger_ReadWriteForm component1 = new Buerger_ReadWriteForm();
		
		final Buerger_Pass_ReadEditForm component2 = new Buerger_Pass_ReadEditForm(ReadWritePass_View.NAME, CreatePassForBuerger_View.NAME, AddPassForBuerger_View.NAME);
		
		final Buerger_Sachbearbeiter_ReadEditGrid component3 = new Buerger_Sachbearbeiter_ReadEditGrid(buergerController, NAME, CreateSachbearbeiterForBuerger_View.NAME, AddSachbearbeiterForBuerger_View.NAME);
		ActionButton detailsButton = new ActionButton(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".component3.button.details.label"), SimpleAction.none);
		detailsButton.addActionPerformer(new NavigateActions(ReadWriteSachbearbeiter_View.NAME)::navigate);		
		component3.addButton(detailsButton);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(component1, component2, component3);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
