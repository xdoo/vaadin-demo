package de.muenchen.itm.infrastructure.auth.mapper;

import de.muenchen.itm.infrastructure.auth.dto.AuthorityDto;
import de.muenchen.itm.infrastructure.auth.dto.UserDto;
import de.muenchen.itm.infrastructure.auth.entities.Authority;
import de.muenchen.itm.infrastructure.auth.entities.Permission;
import de.muenchen.itm.infrastructure.auth.entities.User;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2015-12-22T11:39:56+0100",
    comments = "version: 1.0.0.Final, compiler: javac, environment: Java 1.8.0_20 (Oracle Corporation)"
)
public class UserMapperImpl extends UserMapper {

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setUsername( user.getUsername() );
        userDto.setForname( user.getForname() );
        userDto.setSurname( user.getSurname() );
        userDto.setBirthdate( user.getBirthdate() );
        userDto.setEmail( user.getEmail() );
        userDto.setUserEnabled( user.isUserEnabled() );
        userDto.setAuthorities( authoritiesToAuthorities( user.getAuthorities() ) );

        return userDto;
    }

    @Override
    public Set<String> authoritiesToAuthorities(Set<Authority> authorities) {
        if ( authorities == null ) {
            return null;
        }

        return authorities.stream().flatMap(authority -> authority.getPermissions().stream()).map(permission -> permission.getPermission()).collect(Collectors.toSet());
    }

    @Override
    public AuthorityDto authorityToAuthorityDto(Authority authority) {
        if ( authority == null ) {
            return null;
        }

        AuthorityDto authorityDto_ = new AuthorityDto();

        authorityDto_.setPermissions( permissionSetToStringList( authority.getPermissions() ) );
        authorityDto_.setAuthority( authority.getAuthority() );

        return authorityDto_;
    }

    @Override
    public Set<String> permissionsToPermissions(Set<Permission> permission) {
        if ( permission == null ) {
            return null;
        }

        Set<String> set_ = new HashSet<String>();
        for ( Permission permission_ : permission ) {
            set_.add( permissionToString( permission_ ) );
        }

        return set_;
    }

    protected List<String> permissionSetToStringList(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        for ( Permission permission : set ) {
            list.add( permissionToString(permission) );
        }

        return list;
    }
}
