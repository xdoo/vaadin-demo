package de.muenchen.auth.mapper;

import de.muenchen.auth.dto.UserDto;
import de.muenchen.auth.entities.Authority;
import de.muenchen.auth.entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserMapperTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUserToUserDto() throws Exception {
        final User user = createUser();
        final UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        assertEquals(user.getBirthdate(),userDto.getBirthdate());
        assertEquals(user.getEmail(),userDto.getEmail());
        assertEquals(user.getForname(),userDto.getForname());
        assertEquals(user.getSurname(),userDto.getSurname());
        assertEquals(user.getAuthorities().size(), userDto.getAuthorities().size());
        final List<String> authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
        assertArrayEquals("Authorities mapped not correct.", authorities.toArray(),userDto.getAuthorities().toArray());
    }

    private User createUser() {
        final User user = new User();
        final HashSet<Authority> authorities = new HashSet<>();
        final Authority authority = new Authority();
        authority.setAuthority("ADMIN");
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
