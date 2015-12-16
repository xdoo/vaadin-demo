package de.muenchen.demo.service.services;

import de.muenchen.demo.service.gen.domain.Adresse;
import de.muenchen.demo.service.gen.domain.AdresseExtern;
import de.muenchen.demo.service.gen.domain.AdresseIntern;
import de.muenchen.demo.service.gen.domain.Augenfarben;
import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.demo.service.gen.domain.MoeglicheStaatsangehoerigkeiten;
import de.muenchen.demo.service.gen.domain.Pass;
import de.muenchen.demo.service.gen.domain.PassTyp;
import de.muenchen.demo.service.gen.domain.Sachbearbeiter;
import de.muenchen.demo.service.gen.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.gen.domain.Wohnung;
import de.muenchen.demo.service.gen.rest.AdresseExternRepository;
import de.muenchen.demo.service.gen.rest.AdresseInternRepository;
import de.muenchen.demo.service.gen.rest.AdresseRepository;
import de.muenchen.demo.service.gen.rest.BuergerRepository;
import de.muenchen.demo.service.gen.rest.PassRepository;
import de.muenchen.demo.service.gen.rest.SachbearbeiterRepository;
import de.muenchen.demo.service.gen.rest.StaatsangehoerigkeitRepository;
import de.muenchen.demo.service.gen.rest.WohnungRepository;
import de.muenchen.demo.service.gen.services.BusinessActionService;
import de.muenchen.service.TenantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Provides a service to execute business-actions.
 * If used as generated by GAIA this service will be autowired and called by BusinessActionController.
 */
@Service
public class BusinessActionServiceImpl implements BusinessActionService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;
    @Autowired
    BuergerRepository buergerRepo;
    @Autowired
    WohnungRepository wohnungRepo;
    @Autowired
    AdresseRepository adresseRepo;
    @Autowired
    AdresseExternRepository adresseExternRepo;
    @Autowired
    AdresseInternRepository adresseInternRepo;
    @Autowired
    StaatsangehoerigkeitRepository staatsangehoerigkeitRepo;
    @Autowired
    PassRepository passRepo;
    @Autowired
    SachbearbeiterRepository sachbearbeiterRepo;

    public void testdatenerzeugen() {

        //insert example data for Buerger
        Buerger buerger = new Buerger();
        buerger.setVorname("Max");
        buerger.setNachname("Musterman");
        try {
            buerger.setGeburtsdatum(new java.text.SimpleDateFormat("dd.mm.yyyy").parse("20.10.2000"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        buerger.setAugenfarbe(Augenfarben.blau);
        buerger.setAlive(true);
        HashSet<String> eigenschaften = new HashSet<>();
        eigenschaften.add("Eigenschaft1");
        buerger.setEigenschaften(eigenschaften);
        buerger.setMandant(getCurrentMandant());

        //insert example data for Wohnung
        Wohnung wohnung = new Wohnung();
        wohnung.setStock("erdgeschoss");
        wohnung.setAusrichtung("suedost");
        wohnung.setMandant(getCurrentMandant());

        //insert example data for Adresse
        Adresse adresse = new Adresse();
        adresse.setMandant(getCurrentMandant());

        //insert example data for AdresseExtern
        AdresseExtern adresseExtern = new AdresseExtern();
        adresseExtern.setStrasse("Musterstraße");
        adresseExtern.setHausnummer(3L);
        adresseExtern.setPlz(80331L);
        adresseExtern.setOrt("München");
        adresseExtern.setMandant(getCurrentMandant());

        //insert example data for AdresseIntern
        AdresseIntern adresseIntern = new AdresseIntern();
        adresseIntern.setStrassenSchluessel(1L);
        adresseIntern.setHausnummer(3L);
        adresseIntern.setMandant(getCurrentMandant());

        //insert example data for Staatsangehoerigkeit
        Staatsangehoerigkeit staatsangehoerigkeit = new Staatsangehoerigkeit();
        staatsangehoerigkeit.setName(MoeglicheStaatsangehoerigkeiten.deutsch);
        staatsangehoerigkeit.setMandant(getCurrentMandant());

        //insert example data for Pass
        Pass pass = new Pass();
        pass.setPassNummer(123456L);
        pass.setTyp(PassTyp.personalausweis);
        pass.setKode("123456");
        pass.setGroesse(180L);
        pass.setBehoerde("Einwohnermeldeamt");
        pass.setMandant(getCurrentMandant());

        //insert example data for Sachbearbeiter
        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
        sachbearbeiter.setTelefon(1234567L);
        sachbearbeiter.setFax(1234568L);
        sachbearbeiter.setFunktion("Superbearbeiter");
        sachbearbeiter.setOrganisationseinheit("itm");
        sachbearbeiter.setMandant(getCurrentMandant());

        // Set relations
        buerger.setStaatsangehoerigkeiten(Arrays.asList(staatsangehoerigkeit));
        buerger.setPaesse(Arrays.asList(pass));
        buerger.setWohnungen(wohnung);
        buerger.setSachbearbeiter(Arrays.asList(sachbearbeiter));
        buerger.setKinder(Arrays.asList(buerger));
        buerger.setPartner(buerger);
        wohnung.setAdresse(adresse);
        adresse.setExterneAdresse(adresseExtern);
        adresse.setInterneAdresse(adresseIntern);

        //Save all example Entities in an order that won't cause errors
        adresseExternRepo.save(adresseExtern);
        adresseInternRepo.save(adresseIntern);
        staatsangehoerigkeitRepo.save(staatsangehoerigkeit);
        passRepo.save(pass);
        sachbearbeiterRepo.save(sachbearbeiter);
        adresseRepo.save(adresse);
        wohnungRepo.save(wohnung);
        buergerRepo.save(buerger);
    }

    public String getCurrentMandant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return TenantUtils.extractTenantFromUsername(authentication.getName());
    }

    /**
     * This BusinessAction's purpose is: sendet buerger als mail an adresse
     * TODO: Implement
     */
    @Override
    public void buergeralsmailverschicken(String abc, Buerger bcd) {
        throw new UnsupportedOperationException("The BusinessAction buergeralsmailverschicken is not yet implemented!");
    }

    /**
     * This BusinessAction's purpose is: addiert 2 Buerger
     * It returns one Buerger.
     * TODO: Implement
     */
    @Override
    public Buerger buergerkombinieren(Buerger ersterBuerger, Buerger abc) {
        throw new UnsupportedOperationException("The BusinessAction buergerkombinieren is not yet implemented!");
    }

    /**
     * This BusinessAction's purpose is: listeWohnungAuswaehlen
     * It returns multiple Wohnung.
     * TODO: Implement
     */
    @Override
    public java.util.Collection<Wohnung> listewohnungauswaehlen(java.util.Collection<Long> listeWohnungAuswaehlen) {
        throw new UnsupportedOperationException("The BusinessAction listewohnungauswaehlen is not yet implemented!");
    }

}
