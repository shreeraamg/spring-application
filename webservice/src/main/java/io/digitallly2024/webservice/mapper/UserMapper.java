package io.digitallly2024.webservice.mapper;

import io.digitallly2024.webservice.dto.UserDto;
import io.digitallly2024.webservice.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static User mapToUser(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                null, // Explicitly making the credentials null
                dto.getRole()
        );
    }

}
