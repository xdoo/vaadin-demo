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
import de.muenchen.kvr.buergerverwaltung.buerger	.guilib.gen.ui.components.relation.buerger.Buerger_Pass_ReadEditGrid;
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
		
		final Label buerger__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".buerger.label"));
		buerger__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_ReadWriteForm buerger = new Buerger_ReadWriteForm();
		
		final Label pass__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".pass.label"));
		pass__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_Pass_ReadEditGrid pass = new Buerger_Pass_ReadEditGrid(buergerController, ReadWritePass_View.NAME, 
				CreatePassForBuerger_View.NAME, 
				AddPassForBuerger_View.NAME, 
				null);
		
		final Label sach__label = new Label(BaseUI.getCurrentI18nResolver().resolve("view_." + NAME + ".sach.label"));
		sach__label.addStyleName(ValoTheme.LABEL_H2);
		final Buerger_Sachbearbeiter_ReadEditGrid sach = new Buerger_Sachbearbeiter_ReadEditGrid(buergerController, ReadWriteSachbearbeiter_View.NAME, 
				CreateSachbearbeiterForBuerger_View.NAME, 
				AddSachbearbeiterForBuerger_View.NAME, 
				null);
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(buerger__label, buerger, pass__label, pass, sach__label, sach);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
