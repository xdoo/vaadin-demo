package de.muenchen.auth.mapper;

import de.muenchen.auth.dto.UserDto;
import de.muenchen.auth.entities.Authority;
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

    public abstract Set<String> authoritySetToStringSet(Set<Authority> authorities);

    public String authorityToString(Authority authority){
        return authority.getAuthority();
    }
}
