package de.muenchen.itm.infrastructure.auth;

import de.muenchen.itm.infrastructure.auth.entities.User;
import de.muenchen.itm.infrastructure.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by fabian.holtkoetter on 05.11.15.
 */
@Component("updateOwnUserValidator")
public class UpdateOwnUserValidator {

    public static final String IS_OWN_USER_FILTER = "@updateOwnUserValidator.isOwnUser(authentication,filterObject)";
    public static final String IS_OWN_USER_AUTH = "@updateOwnUserValidator.isOwnUser(authentication,returnObject)";

    @Autowired
    UserRepository userRepository;

    public boolean isOwnUser(Authentication authentication, Object o) {

        if (o == null)
            return true;

        if (o.getClass() != User.class)
            throw new IllegalArgumentException();

        final User changeUser = ((User) o);
        final User currentUser = userRepository.findFirstByUsername(authentication.getName());

        return Objects.equals(currentUser.getOid(), changeUser.getOid());
    }

}

