package de.muenchen.kvr.buergerverwaltung.buergerverwaltung.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * Aus dieser Klasse leiten sich alle anderen Views ab.
 *
 * @author claus.straube
 */
public abstract class DefaultView extends VerticalLayout implements View{

    @Override
    public final void enter(ViewChangeListener.ViewChangeEvent event) {
        this.configureLayout();
        
		MainUI ui = (MainUI) getUI();
		ui.setHelpContent();

        this.removeAllComponents();
        // add some components
        this.init();
    }

    private void configureLayout() {
        setSizeFull();
        this.setHeightUndefined();
        setMargin(new MarginInfo(false, true, false, true));
    }

    protected abstract void init();

}

