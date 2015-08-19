/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class SearchBaseTable extends BuergerTable{

    public SearchBaseTable(BuergerViewController controller, BuergerTableButtonFactory... buttonfactory) {
        super(controller, buttonfactory);
    }
    
}
