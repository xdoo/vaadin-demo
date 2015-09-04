package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface BuergerRestClient {
    
    /**
     * Erstellt eine neue Instanz eines {@link Buerger} Objektes.
     * 
     * @param links
     * @param restTemplate
     * @return 
     */
    public Buerger newBuerger(List<Link> links, RestTemplate restTemplate);
    
    /**
     * Liest eine Instanz eines {@link Buerger} Objektes. Dazu muss ein 'self' Link
     * in den übergebenen Links vorhanden sein.
     * 
     * @param links
     * @param restTemplate
     * @return 
     */
    public Buerger readBuerger(List<Link> links, RestTemplate restTemplate);
    
    /**
     * Kopiert eine Instanz eines {@link Buerger} Objektes. Diese Kopie wird
     * gleich in der DB gespeichert. Beziehungen / Referenzen zu anderen Objekten 
     * werden nicht kopiert. <br><br>
     * Beispiel: Hate eine Person die Attribute Vornamen, Nachnamen und Kinder, wobei
     * die Kinder eine Referenz auf andere (eigenständige) Personen sind, so wird
     * die Kopie nur die Attribute Vorname und Nachname aus der Quelle befüllt enthalten.
     * 
     * @param links
     * @param restTemplate
     * @return 
     */
    public Buerger copyBuerger(List<Link> links, RestTemplate restTemplate);
    
    /**
     * Gibt alle {@link Buerger} Objekte (zu einem Mandanten) zurück.
     * 
     * @param links
     * @param restTemplate
     * @return 
     */
    public List<Buerger> queryBuerger(List<Link> links, RestTemplate restTemplate);
    
    /**
     * Gibt alle {@link Buerger} Objekte (zu einem Mandanten) zurück, die zu einer
     * bestimmten Suchanfrage passen. Hierbei ist kein syntax wie SQL anzuwenden, 
     * sondern es reicht - wie bei einer Suchmasvchine - die Suchwerte als String zu 
     * übergeben. Die entsprechenden Suchfelder müssen allerdings auf Seite des
     * Services markiert sein, damit sie in den Suchindex aufgenommen werden können.
     * 
     * @param query
     * @param links
     * @param restTemplate
     * @return 
     */
    public List<Buerger> queryBuerger(String query, List<Link> links, RestTemplate restTemplate);
    
    /**
     * Aktualisiert eine Instanz eines {@link Buerger} Objektes. Dazu muss ein 'update' Link
     * in den übergebenen Links vorhanden sein.
     * 
     * @param buerger
     * @param restTemplate
     * @return 
     */
    public Buerger updateBuerger(Buerger buerger, RestTemplate restTemplate);
    
    /**
     * Speichert eine noch nicht in der DB vorhandene Instanz eines {@link Buerger} Objektes. 
     * Dazu muss ein 'save' Link in den übergebenen Links vorhanden sein.
     * 
     * @param buerger
     * @param restTemplate
     * @return 
     */
    public Buerger saveBuerger(Buerger buerger, RestTemplate restTemplate);
    
    /**
     * Löscht eine Instanz eines {@link Buerger} Objektes. Dazu muss ein 'delete' Link
     * in den übergebenen Links vorhanden sein.
     * 
     * @param links
     * @param restTemplate 
     */
    public void deleteBuerger(List<Link> links, RestTemplate restTemplate);
    
    /**
     * 
     * @param links
     * @param restTemplate
     * @return 
     */
    public List<Buerger> queryKinder(List<Link> links, RestTemplate restTemplate);
    
    /**
     *
     * @param links
     * @param restTemplate
     * @return
     */
    public List<Buerger> queryPartner(List<Link> links, RestTemplate restTemplate);

    /**
     * 
     * @param buerger
     * @param kind
     * @param restTemplate
     * @return 
     */
    public Buerger saveBuergerKind(Buerger buerger, Buerger kind, RestTemplate restTemplate);
    
    public Buerger addBuergerKind(Buerger buerger, Buerger kind, RestTemplate restTemplate);

    public Buerger releaseBuergerElternteil(Buerger buerger, Buerger kind, RestTemplate restTemplate);
    
    public Buerger addBuergerPartner(Buerger buerger, Buerger kind, RestTemplate restTemplate);

    public List<Buerger> queryHistory(List<Link> links, RestTemplate restTemplate);

    public Buerger saveBuergerPartner(Buerger buerger, Buerger partner, RestTemplate restTemplate);
}
