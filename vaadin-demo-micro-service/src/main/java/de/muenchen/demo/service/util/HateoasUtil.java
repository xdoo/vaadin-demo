/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.util;

/**
 *
 * @author claus.straube
 */
public interface HateoasUtil {

    public final static String SELF = HateoasRelations.SELF.toString().toLowerCase();
    public final static String UPDATE = HateoasRelations.UPDATE.toString().toLowerCase();
    public final static String DELETE = HateoasRelations.DELETE.toString().toLowerCase();
    public final static String QUERY = HateoasRelations.QUERY.toString().toLowerCase();
    public final static String NEW = HateoasRelations.NEW.toString().toLowerCase();
    public final static String SAVE = HateoasRelations.SAVE.toString().toLowerCase();
    public final static String COPY = HateoasRelations.COPY.toString().toLowerCase();
    public final static String WOHNUNGEN = HateoasRelations.WOHNUNGEN.toString().toLowerCase();
    public final static String KINDER = HateoasRelations.KINDER.toString().toLowerCase();
    public final static String PASS = HateoasRelations.PASS.toString().toLowerCase();

}
