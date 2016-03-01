package de.muenchen.itm.infrastructure.auth.services;

import de.muenchen.itm.infrastructure.auth.mapper.UserMapper;
import de.muenchen.itm.infrastructure.auth.repositories.AuthorityRepository;
import de.muenchen.itm.infrastructure.auth.repositories.UserRepository;
import de.muenchen.itm.infrastructure.auth.entities.User;
import de.muenchen.service.security.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by huningd on 17.12.15.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    public static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %S not found!", username));
        }
        UserInfo userInfo;
        userInfo = mapper.userToUserInfo(user);
        return userInfo;
    }

    public UserRepository getRepo() {
        return userRepository;
    }

    public void setRepo(UserRepository repo) {
        this.userRepository = repo;
    }
}

