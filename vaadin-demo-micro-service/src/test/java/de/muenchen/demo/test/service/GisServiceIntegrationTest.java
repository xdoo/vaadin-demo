package de.muenchen.demo.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import org.junit.Before;
import org.junit.Test;

import de.muenchen.gis_service._1_0.Adressauskunft;
import de.muenchen.gis_service._1_0.Adresse;
import de.muenchen.gis_service._1_0.Adresssuche;
import de.muenchen.gis_service._1_0.Gebietssuche;
import de.muenchen.gis_service._1_0.Gebietstypen;
import de.muenchen.gis_service._1_0.GisFault;
import de.muenchen.gis_service._1_0.Strasse;
import de.muenchen.gis_service._1_0.Strassensuche;
import de.muenchen.gis_service._1_0.SucheAdressenAntwort;
import de.muenchen.gis_service._1_0.SucheAdressenAuskunft;
import de.muenchen.gis_service._1_0_0.GisService;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;


public class GisServiceIntegrationTest {

	private String wsVersion;
	private String wsAddress;
	private GisService service;

	@Before
	public void setUp() throws IOException {
		//Properties props = new Properties();
//		props.load(ClassLoader.getSystemResourceAsStream("config.properties"));
		wsVersion ="1.0.0";
		//wsAddress = props.getProperty("ws.address");
		wsAddress = "http://eaiallc001.srv.ha3.dir.muenchen.de:8185";
		service = createService();

	}

	@Test
	public void testLeseGebietsdatenZurAdresse() {
		Adressauskunft auskunft = new Adressauskunft("Pestalozzistr.", 22, null, null, null, null);
		Adresse adresse = service.leseGebietsdatenZurAdresse(auskunft);
		assertEquals("Ludwigsvorstadt - Isarvorstadt", adresse.getOrtsteil());
	}

	@Test
	public void testLeseGebietsdatenOhneTreffer() {
		GisService service = createService();
		Adressauskunft auskunft = new Adressauskunft("Gibt es niemals Str.", 1, "a", null, null,
				null);
		Adresse adresse = service.leseGebietsdatenZurAdresse(auskunft);
		assertNull(adresse);
	}

	@Test
	public void testSucheUeberAdresssuche() {
		Adresssuche adresssuche = new Adresssuche(null, "Ampfingstr.", null, 15, null, null,
				null, null, null);
		SucheAdressenAuskunft suche = new SucheAdressenAuskunft(adresssuche, null, null);
		SucheAdressenAntwort antwort = service.sucheAdressen(suche);
		List<Adresse> adressen = antwort.getAdressen();
		assertTrue(adressen.size()>0);
		for (Adresse adresse : adressen) {
			assertEquals(new Integer(209), adresse.getStrassenschluessel());
		}
	}
	
	@Test
	public void testSucheUeberAdresssucheMitBuschstaben() {
		Adresssuche adresssuche = new Adresssuche(null, "Pestalozzistr.", null, null, null, "a", "z", null, null);
		SucheAdressenAuskunft suche = new SucheAdressenAuskunft(adresssuche, null, null);
		SucheAdressenAntwort antwort = service.sucheAdressen(suche);
		List<Adresse> adressen = antwort.getAdressen();
		assertTrue(adressen.size()>0);
		for (Adresse adresse : adressen) {
			assertEquals(new Integer(3527), adresse.getStrassenschluessel());
		}
	}

	@Test(expected = SOAPFaultException.class)
	public void testSucheZuVieleTreffer() throws JAXBException {
		Adresssuche adresssuche = new Adresssuche(null, null, null, null, null, null, null, null,
				null);
		SucheAdressenAuskunft suche = new SucheAdressenAuskunft(adresssuche, null, null);
		try {
			service.sucheAdressen(suche);
		}
		catch (SOAPFaultException e) {
			SOAPFault fault = e.getFault();
			Detail detail = fault.getDetail();
			final JAXBContext jaxbContext = JAXBContext.newInstance(GisFault.class.getPackage().getName());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<GisFault> gisFault = unmarshaller.unmarshal(detail.getFirstChild(),GisFault.class);
			assertEquals("GIS01F002", gisFault.getValue().getFaultCode());
			throw e;
		}
	}

	@Test
	public void testSucheUeberGebietssuche() {
		Gebietssuche gebietssuche = new Gebietssuche();
		gebietssuche.setGebietstyp(Gebietstypen.MITTELSCHULSPRENGEL);
		gebietssuche.setGebietsnummer(2290);
		SucheAdressenAuskunft suche = new SucheAdressenAuskunft(null, gebietssuche, null);
		SucheAdressenAntwort antwort = service.sucheAdressen(suche);
		List<Adresse> adressen = antwort.getAdressen();
		assertTrue(adressen.size() > 0);
	}

	@Test
	public void testSucheUeberStrassensuche() {

		Strassensuche strassensuche = new Strassensuche(null, 3527);
		SucheAdressenAuskunft suche = new SucheAdressenAuskunft(null, null, strassensuche);
		SucheAdressenAntwort antwort = service.sucheAdressen(suche);
		List<Adresse> adressen = antwort.getAdressen();
		assertTrue(adressen.size()>0);
		for (Adresse adresse : adressen) {
			assertEquals("Pestalozzistr.", adresse.getStrasse());
		}

	}

	@Test
	public void testLeseGebietsdatenZurAdresseMitBuchtsabe() {
		GisService service = createService();
		Adressauskunft auskunft = new Adressauskunft("Pestalozzistr.", 40, "b", null, null, null);
		Adresse adresse = service.leseGebietsdatenZurAdresse(auskunft);
		assertEquals("b", adresse.getBuchstabe());
		assertEquals(new Integer(3527), adresse.getStrassenschluessel());

		auskunft = new Adressauskunft("Pestalozzistr.", 40, null, null, null, null);
		adresse = service.leseGebietsdatenZurAdresse(auskunft);
		assertEquals(null, adresse.getBuchstabe());
		assertEquals(new Integer(3527), adresse.getStrassenschluessel());
	}

	@Test
	public void testLeseStrassenname() {
		GisService service = createService();
		String straname = service.leseStrassenname(3527);
		assertEquals("Pestalozzistr.", straname);
	}

	@Test
	public void testLeseStrassennamerOhneTreffer() {
		GisService service = createService();
		String straname = service.leseStrassenname(1111111111);
		assertNull(straname);
	}

	@Test
	public void testSucheStrasse() {
		GisService service = createService();
		List<Strasse> strasse = service.sucheStrasse("Friedrich-Loy*");
		assertEquals(1, strasse.size());
	}

	@Test
	public void testSucheAlleStrasse() {
		GisService service = createService();
		List<Strasse> strassen = service.sucheStrasse("");
		// assertEquals(5757, strassen.size());
		for (Strasse strasse : strassen) {
			assertNotNull(strasse);
			assertNotNull(String.format("Bezeichnung fÃŒr %s ist null", strasse.getStrassenschluessel()),strasse.getStrasse());
		}
	}

	/**
	 * Erstellt den WS-Client
	 */
	private GisService createService() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(GisService.class);
		factory.setAddress(wsAddress + "/cxf/" + wsVersion + "/gis-service");
		factory.setProperties(new HashMap<String, Object>());
		factory.getProperties().put("password", "ivu");
		factory.getOutInterceptors().add(createWSS4JOutInterceptor());
		GisService service = (GisService) factory.create();
		return service;
	}

	/**
	 * Erstellt den PasswordCallback fÃŒr die WSS.
	 */
	private WSS4JOutInterceptor createWSS4JOutInterceptor() {
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, "test");
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CallbackHandler() {

			public void handle(Callback[] callbacks) throws IOException,
					UnsupportedCallbackException {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
				pc.setPassword("test");
			}
		});

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		return wssOut;
	}

}
