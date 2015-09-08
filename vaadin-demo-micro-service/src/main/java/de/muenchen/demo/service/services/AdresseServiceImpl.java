package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterne;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterne;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import de.muenchen.gis_service._1_0.Adresssuche;
import de.muenchen.gis_service._1_0.SucheAdressenAntwort;
import de.muenchen.gis_service._1_0.SucheAdressenAuskunft;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AdresseServiceImpl implements AdresseService {

    private static final Logger LOG = LoggerFactory.getLogger(AdresseService.class);

    @Autowired
    GisSpringService gisService;
    @Autowired
    MandantService mandantService;
    UserService userService;
    AdresseInterneRepository interneRepo;
    AdresseExterneRepository externeRepo;
    AdresseReferenceRepository referenceRepo;
    QueryService<Adresse> search;
    RestTemplate restTemplate = new TestRestTemplate();

    public AdresseServiceImpl() {
    }

    @Autowired
    public AdresseServiceImpl(AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, UserService userService, EntityManager em) {
        this.externeRepo = externeRepo;
        this.interneRepo = interneRepo;
        this.referenceRepo = referenceRepo;
        this.userService = userService;
        this.search = new QueryService<>(userService, em, Adresse.class, "stadt", "hausnummer", "strasse", "plz");
    }

    @Override
    public List<Adresse> query() {
        Iterable<AdresseReference> allReferences = this.referenceRepo.findByMandantOid(readUser().getMandant().getOid());
        ArrayList<Adresse> list = new ArrayList();
        for (AdresseReference ad : allReferences) {
            list.add(this.read(ad.getOid()));
            
        }
        return list;
    }

    @Override
    public Adresse create() {
        Adresse adresse = new Adresse();
        adresse.setOid(IdService.next());
        return adresse;
    }

    @Override
    public List<Adresse> suche(Adresse adresse) {
        SucheAdressenAuskunft suche = fromAdresse(adresse);
        SucheAdressenAntwort antwort = gisService.getGisService().sucheAdressen(suche);
        return toAdressen(antwort);

    }

    public SucheAdressenAuskunft fromAdresse(Adresse adresse) {
        Adresssuche adresssuche = new Adresssuche(null, adresse.getStrasse(), adresse.getStrasseReference(), adresse.getHausnummer(), null, adresse.getBuchstabe(),
                null, adresse.getPlz(), null);
        return new SucheAdressenAuskunft(adresssuche, null, null);
    }

    public List<Adresse> toAdressen(SucheAdressenAntwort adr) {
        List<Adresse> list = new ArrayList<>();
        adr.getAdressen().stream().forEach(a -> {
            Adresse adresse = new Adresse();
            adresse.setHausnummer(a.getHausnummer());
            adresse.setPlz(a.getPlz());
            adresse.setBuchstabe(a.getBuchstabe());
            adresse.setStrasse(a.getStrasse());
            adresse.setStrasseReference(a.getStrassenschluessel());
            adresse.setStadt("München");
            list.add(adresse);
        });

        return list;
    }

    @Override
    public Adresse read(String oid) {
        AdresseReference resultReference = this.referenceRepo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (Objects.isNull(resultReference)) {
            // TODO
            LOG.warn(String.format("found no Adresse with oid '%s'", oid));
            return null;

        } else {
            if (resultReference.getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(resultReference.getAdresseExterne().getOid(), readUser().getMandant().getOid());
                return fromExterne(resultExterne.get(0));

            } else {
                AdresseInterne resultInterne = this.interneRepo.findByOidAndMandantOid(resultReference.getAdresseInterne().getOid(), readUser().getMandant().getOid()).get(0);
                Adresse adr = new Adresse();
                adr.setHausnummer(resultInterne.getHausnummer());
                adr.setBuchstabe(resultInterne.getBuchstabe());
                adr.setStrasseReference(resultInterne.getStrasseReference());
                SucheAdressenAuskunft suche = fromAdresse(adr);
                SucheAdressenAntwort antwort = gisService.getGisService().sucheAdressen(suche);
                List<Adresse> result2 = toAdressen(antwort);

                result2.get(0).setOid(resultInterne.getOid());
                result2.get(0).setMandant(resultInterne.getMandant());
                return result2.get(0);
            }
        }
    }

    @Override
    public AdresseReference readReference(String oid) {
        List<AdresseReference> result = this.referenceRepo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }

    public AdresseExterne toExterne(Adresse adresse) {
        AdresseExterne adresseExterne = new AdresseExterne();
        adresseExterne.setHausnummer(adresse.getHausnummer());
        adresseExterne.setBuchstabe(adresse.getBuchstabe());
        adresseExterne.setOid(adresse.getOid());
        adresseExterne.setMandant(adresse.getMandant());
        adresseExterne.setPlz(adresse.getPlz());
        adresseExterne.setStadt(adresse.getStadt());
        adresseExterne.setStrasse(adresse.getStrasse());
        return adresseExterne;
    }

    public Adresse fromExterne(AdresseExterne adresseExterne) {
        Adresse adresse = new Adresse();
        adresse.setHausnummer(adresseExterne.getHausnummer());
        adresse.setBuchstabe(adresseExterne.getBuchstabe());

        adresse.setOid(adresseExterne.getOid());
        adresse.setPlz(adresseExterne.getPlz());
        adresse.setStadt(adresseExterne.getStadt());
        adresse.setMandant(adresseExterne.getMandant());
        adresse.setStrasse(adresseExterne.getStrasse());
        return adresse;
    }

    public AdresseInterne toInterne(Adresse adresse) {
        AdresseInterne adresseInterne = new AdresseInterne();

        adresseInterne.setOid(adresse.getOid());
        adresseInterne.setStrasseReference(adresse.getStrasseReference());
        adresseInterne.setHausnummer(adresse.getHausnummer());
        adresseInterne.setBuchstabe(adresse.getBuchstabe());
        adresseInterne.setMandant(adresse.getMandant());
        return adresseInterne;
    }

    public AdresseReference toReferenceInterne(Adresse adresse) {

        AdresseReference adresseRefernece = new AdresseReference();
        adresseRefernece.setAdresseInterne(toInterne(adresse));
        adresseRefernece.setOid(adresse.getOid());
        adresseRefernece.setMandant(adresse.getMandant());

        return adresseRefernece;
    }

    public AdresseReference toReferenceExterne(Adresse adresse) {

        AdresseReference adresseReference = new AdresseReference();
        adresseReference.setOid(adresse.getOid());
        adresseReference.setAdresseExterne(toExterne(adresse));
        adresseReference.setMandant(adresse.getMandant());

        return adresseReference;
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }
}
