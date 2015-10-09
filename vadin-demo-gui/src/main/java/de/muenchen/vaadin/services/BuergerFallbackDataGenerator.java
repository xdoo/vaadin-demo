package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by claus.straube on 08.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class BuergerFallbackDataGenerator {

    /** Fallback-Daten generierung. Diese Methoden werden durch Barrakuda mit dem gewünschten Verhalten befüllt. */

    /**
     * Generiert Fallback-Daten für Methoden die einen einzelnen Bürger zurückliefern.
     *
     * @return Generierter fallback-Wert.
     */
    public static Buerger createBuergerFallback() {
        return null;
    }

    /**
     * Generiert Fallback-Daten für Methoden die eine List von Bürgern zurückliefern.
     *
     * @return Generierter fallback-Wert.
     */
    public static List<Buerger> createBuergersFallback() {
        return new ArrayList<>();
    }

    /**
     * Generiert Fallback-Daten für Methoden die einen optionalen Bürger zurückliefern.
     *
     * @return Generierter fallback-Wert.
     */
    public static Optional<Buerger> createOptionalBuergerFallback() {
        return Optional.empty();
    }


}
