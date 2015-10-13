/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class KindGrid extends GenericGrid<Buerger> {

    public KindGrid(BuergerViewController controller) {
        super(controller,
                controller.getModel().getSelectedBuergerKinder(),
                Buerger.Field.getProperties());

    }


}
