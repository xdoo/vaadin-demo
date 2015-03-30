/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager am;

    @Override
    public boolean login(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
            Authentication result = am.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
