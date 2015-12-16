package de.muenchen.auth.mapper;

import de.muenchen.auth.dto.AuthorityDto;
import de.muenchen.auth.dto.UserDto;
import de.muenchen.auth.entities.Authority;
import de.muenchen.auth.entities.Permission;
import de.muenchen.auth.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * Created by dennis_huning on 09.12.15.
 */
@Mapper
public abstract class UserMapper {
    public static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserDto userToUserDto(User user);

    public abstract Set<AuthorityDto> authoritiesToAuthorities(Set<Authority> authorities);

    public abstract AuthorityDto authorityToAuthorityDto(Authority authority);

    public abstract Set<String> permissionsToPermissions(Set<Permission> permission );

    public String permissionToString(Permission permission){
        return permission.getPermission();
    }
}
