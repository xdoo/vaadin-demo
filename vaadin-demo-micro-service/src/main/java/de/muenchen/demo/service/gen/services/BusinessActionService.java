package de.muenchen.demo.service.gen.services;

import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.demo.service.gen.domain.Wohnung;

/**
 * Provides a service to execute business-actions.
 */
public interface BusinessActionService {

    /**
     * This BusinessAction's purpose is to generate test data
     */
    public void testdatenerzeugen();

    /**
     * This BusinessAction's purpose is: sendet buerger als mail an adresse
     */
    void buergeralsmailverschicken(String abc, Buerger bcd);

    /**
     * This BusinessAction's purpose is: addiert 2 Buerger
     * It returns one Buerger.
     */
    Buerger buergerkombinieren(Buerger ersterBuerger, Buerger abc);

    /**
     * This BusinessAction's purpose is: listeWohnungAuswaehlen
     * It returns multiple Wohnung.
     */
    java.util.Collection<Wohnung> listewohnungauswaehlen(java.util.Collection<Long> listeWohnungAuswaehlen);

}
