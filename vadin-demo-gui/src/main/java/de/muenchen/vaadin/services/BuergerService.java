package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.domain.Buerger;

import java.util.List;

/**
 * Service zum Verwalten von Bürgern aus der GUI heraus.
 * 
 * @author claus.straube
 */
public interface BuergerService {
    
    /**
     * Erstellt eine neue {@link Buerger} Instanz mit oid.
     * Diese Instanz ist nicht in der DB gespeichert, sondern
     * existiert virtuell. D.h. ein 'abbrechen' beim 
     * Befüllen des Objektes erfordert keine weitere Aktion.
     * 
     * @return 
     */
    public Buerger createBuerger();
    
    /**
     * Holt ein {@link Buerger} Objekt vom Service. Diese
     * Methode kann beispielsweise genutzt werden, um die 
     * Nutzdaten, oder aber die zur Verfügung stehenden 
     * HATEOAS Links zu aktualisieren.
     * 
     * @param entity aktuelle Instanz des Objektes von der Oberfläche
     * @return aktuelle Instanz des Objektes vom Service
     */
    public Buerger readBuerger(Buerger entity);
    
    /**
     * Aktualisiert einen {@link Buerger} im Service.
     * 
     * @param entity zu aktualisierende Instanz des Objektes
     * @return aktualisierte Instanz des Objektes
     */
    public Buerger updateBuerger(Buerger entity);
    
    /**
     * Speichert einen {@link Buerger} initial im Service.
     * 
     * @param entity zu speichernde Instanz des Objektes
     * @return gespeicherte Instanz des Objektes
     */
    public Buerger saveBuerger(Buerger entity);
    
    /**
     * Löscht einen {@link Buerger} dauerhaft.
     * 
     * @param entity zu löschendes Objekt
     */
    public void deleteBuerger(Buerger entity);
    
    /**
     * Gibt alle für den Mandanten vorhandenen {@link Buerger} zurück.
     * 
     * @return alle gefundenen Objekte
     */
    public List<Buerger> queryBuerger();
    
    public List<Buerger> queryBuerger(String query);
    
    /**
     * Kopiert die Instanz eines {@link Buerger}s. Diese
     * Kopie erhält eine neue OID und wird sofort in der 
     * DB gespeichert. D.h. diese Aktion kann nur durch ein
     * Löschen des Objektes rückgängig gemacht werden.
     * 
     * @return Kopie der übergebene Instanz mit neuer OID
     */
    public Buerger copyBuerger(Buerger entity);
    
    public List<Buerger> queryKinder(Buerger entity);
    public List<Buerger> queryPartner(Buerger entity);
    
    public Buerger saveKind(Buerger entity, Buerger kind);
    public Buerger addKind(Buerger entity, Buerger kind);

    public Buerger releaseElternteil(Buerger elternteil, Buerger kind);
    public Buerger addPartner(Buerger entity, Buerger partner);
    public List<Buerger> queryHistory(Buerger entity);
}
