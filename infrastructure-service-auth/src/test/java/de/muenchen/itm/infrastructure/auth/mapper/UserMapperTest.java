package de.muenchen.itm.infrastructure.auth.mapper;

import de.muenchen.itm.infrastructure.auth.dto.UserDto;
import de.muenchen.itm.infrastructure.auth.entities.Authority;
import de.muenchen.itm.infrastructure.auth.entities.Permission;
import de.muenchen.itm.infrastructure.auth.entities.User;
import de.muenchen.service.security.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserMapperTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUserToUserDtoWithOneAuthorityAndPermission() throws Exception {
        final User user = createUser();
        // UserDto only contains Permissions not Roles
        final UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        assertEquals(user.getBirthdate(), userDto.getBirthdate());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getForname(), userDto.getForname());
        assertEquals(user.getSurname(), userDto.getSurname());
        assertEquals(user.getAuthorities().size(), userDto.getAuthorities().size());
        final List<String> permissions = reducePermissions(user);
        assertArrayEquals("Authorities mapped not correct.", permissions.toArray(), userDto.getAuthorities().toArray());
    }

    private List<String> reducePermissions(User user) {
        return user.getAuthorities().stream().flatMap(authority -> authority.getPermissions().stream()).map(a -> a.getPermission()).collect(Collectors.toList());
    }

    @Test
    public void testUserToUserInfo() {
        final User user = createUser();
        // UserInfo only contains Roles not Permissions
        final UserInfo userInfo = UserMapper.INSTANCE.userToUserInfo(user);
        assertEquals(user.getUsername(), userInfo.getUsername());
        assertEquals(user.getPassword(), userInfo.getPassword());

        final List<String> authoritiesFromUser = user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList());
        final List<String> authoritiesFromUserInfo = userInfo.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertArrayEquals("Authorities mapped not correct.", authoritiesFromUser.toArray(), authoritiesFromUserInfo.toArray());
    }

    private User createUser() {
        final User user = new User();
        final HashSet<Authority> authorities = new HashSet<>();
        final Authority authority = new Authority();
        authority.setAuthority("ADMIN");
        Set<Permission> permissions = new HashSet<>();
        Permission permission = new Permission();
        permission.setPermission("READ_ALL");
        permissions.add(permission);
        authority.setPermissions(permissions);
        authorities.add(authority);
        user.setAuthorities(authorities);
        user.setUsername("username");
        user.setBirthdate(Date.valueOf(LocalDate.parse("2010-08-07")));
        user.setEmail("mail@adresse.de");
        user.setForname("Forname");
        user.setPassword("password");
        user.setSurname("Surname");
        user.setUserEnabled(true);
        return user;
    }
}
