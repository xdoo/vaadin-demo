/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.gis_service._1_0_0.GisService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class GisSpringServiceImpl implements GisSpringService {

    private GisService service;
    private final String wsVersion;
    private final String wsAddress;

    public GisSpringServiceImpl() {
        this.wsAddress = "http://eaiallc001.srv.ha3.dir.muenchen.de:8185";
        this.wsVersion = "1.0.0";
    }

    @Override
    public GisService getGisService() {

        return createService();
    }

    private GisService createService() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(GisService.class);
        factory.setAddress(wsAddress + "/cxf/" + wsVersion + "/gis-service");
        factory.setProperties(new HashMap<>());
        factory.getProperties().put("password", "ivu");
        factory.getOutInterceptors().add(createWSS4JOutInterceptor());
        service = (GisService) factory.create();
        return service;
    }
    private WSS4JOutInterceptor createWSS4JOutInterceptor() {
		Map<String, Object> outProps = new HashMap<>();
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
