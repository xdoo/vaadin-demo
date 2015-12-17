package de.muenchen.auth.services;

import de.muenchen.auth.entities.Authority;
import de.muenchen.auth.entities.Permission;
import de.muenchen.auth.mapper.UserMapper;
import de.muenchen.auth.repositories.PermissionRepository;
import de.muenchen.auth.repositories.UserRepository;
import de.muenchen.service.security.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by huningd on 17.12.15.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    public static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        de.muenchen.auth.entities.User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            return null;
        }
        UserInfo userInfo;
        if (user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Authority.ADMIN_AUTHORITY))) {
            // The authority ADMIN gets automatically all permissions. Hence no service needs to map the permissions manual.
            log.debug("User {} has authority {}", username, Authority.ADMIN_AUTHORITY);
            final Iterable<Permission> all = permissionRepository.findAll();
            Set<GrantedAuthority> authorities = StreamSupport.stream(all.spliterator(), false).map(p -> new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());
            userInfo = new UserInfo(user.getUsername(), user.getPassword(), user.getMandant(), authorities);
        } else {
            userInfo = mapper.userToUserInfo(user);
        }
        return userInfo;
    }

    public UserRepository getRepo() {
        return userRepository;
    }

    public void setRepo(UserRepository repo) {
        this.userRepository = repo;
    }
}
