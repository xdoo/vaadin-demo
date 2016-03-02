package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;


import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;


@SpringView(name = Startseite_View.NAME)
@UIScope
public class Startseite_View extends DefaultView{				
	public static final String NAME = "";
	
	@Override
	protected void init(){
		Label pageTitle = new Label(BaseUI.getCurrentI18nResolver().resolve("view_.startseite.title"));
        pageTitle.addStyleName(ValoTheme.LABEL_H1);
        pageTitle.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(pageTitle);
		
		final ActionButton onePercentChanceOfWinningTenMillionEURO = new ActionButton(MainUI.getCurrentI18nResolver().resolve("view_.startseite.button.onePercentChanceOfWinningTenMillionEURO.label"), SimpleAction.none);
		onePercentChanceOfWinningTenMillionEURO.setId("onePercentChanceOfWinningTenMillionEURO-");
		
		// Add components to the default layout
		final VerticalLayout layout = new VerticalLayout(onePercentChanceOfWinningTenMillionEURO);
		
		layout.setSpacing(true);
		addComponent(layout);
	}
}
