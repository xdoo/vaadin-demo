package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterne;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterne;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
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
public class WohnungServiceImpl implements WohnungService {

    @Autowired
    private BuergerService buergerService;
    private static final Logger LOG = LoggerFactory.getLogger(WohnungService.class);
    WohnungRepository repo;
    AdresseInterneRepository interneRepo;
    AdresseExterneRepository externeRepo;
    AdresseReferenceRepository referenceRepo;
    QueryService<Wohnung> search;
    RestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;
    @Autowired
    private AdresseService adresseService;

    public WohnungServiceImpl() {
    }

    @Autowired
    public WohnungServiceImpl(WohnungRepository repo, AdresseInterneRepository interneRepo, AdresseExterneRepository externeRepo, AdresseReferenceRepository referenceRepo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.externeRepo = externeRepo;
        this.interneRepo = interneRepo;
        this.referenceRepo = referenceRepo;
        this.search = new QueryService<>(userService, em, Wohnung.class, "adresseOid", "ausrichtung", "stock");
    }

    @Override
    public void delete(String wOid) {
        Wohnung item = this.read(wOid);
        this.buergerService.releaseWohnungAllBuerger(wOid);
        this.repo.delete(item);
        String aOid = item.getAdresse().getOid();

        List<AdresseReference> resultReference = this.referenceRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
        if (resultReference.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no adresse with oid '%s'", aOid));

        } else {
            if (resultReference.get(0).getAdresseInterne() == null) {
                List<AdresseExterne> resultExterne = this.externeRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
                this.referenceRepo.delete(resultReference);
                this.externeRepo.delete(resultExterne);
            } else {
                List<AdresseInterne> resultInterne = this.interneRepo.findByOidAndMandantOid(aOid, readUser().getMandant().getOid());
                this.referenceRepo.delete(resultReference);
                this.interneRepo.delete(resultInterne);
            }
        }

    }

    @Override
    public Wohnung create() {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid(IdService.next());
        return wohnung;
    }

    @Override
    public Wohnung save(Wohnung wohnung, Adresse adresse) {
        LOG.info(wohnung.toString());
        Preconditions.checkArgument(wohnung.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        wohnung.setMandant(mandant);
        adresse.setOid(IdService.next());

        if (Objects.isNull(adresse.getStrasseReference())) {

            adresse.setMandant(mandant);
            wohnung.setAdresse(toReferenceExterne(adresse));
        } else {
            adresse.setMandant(mandant);
            wohnung.setAdresse(toReferenceInterne(adresse));
        }
        return this.repo.save(wohnung);

    }

    @Override
    public Wohnung read(String oid) {
        Wohnung result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());

        if (Objects.isNull(result)) {
            LOG.warn(String.format("found no pass with oid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    @Override
    public Wohnung update(Wohnung wohnung, Adresse adresse) {
        this.delete(wohnung.getOid());
        adresse.setMandant(wohnung.getMandant());
        if (Objects.isNull(adresse.getStrasseReference())) {
            wohnung.setAdresse(toReferenceExterne(adresse));
        } else {

            wohnung.setAdresse(toReferenceInterne(adresse));
        }
        return this.repo.save(wohnung);
    }

    @Override
    public List<Wohnung> query() {
        Iterable<Wohnung> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Wohnung> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Wohnung copy(String oid) {
        Wohnung in = this.read(oid);
        // map
        Wohnung out = new Wohnung(in);
        Adresse adresse = this.adresseService.read(in.getAdresse().getOid());
        adresse.setOid(IdService.next());
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out, adresse);
        return out;
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
 @Override
    public void copy(List<String> oids) {
        oids.stream().forEach(this::copy);
    }

    @Override
    public void delete(List<String> oids) {
        oids.stream().forEach(this::delete);
    }
}
