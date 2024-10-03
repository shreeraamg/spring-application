package io.digitallly2024.webservice.mapper;

import io.digitallly2024.webservice.dto.ResourceDto;
import io.digitallly2024.webservice.dto.UserDto;
import io.digitallly2024.webservice.entity.Resource;

public class ResourceMapper {

    private ResourceMapper() {}

    public static ResourceDto mapToResourceDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setDescription(resource.getDescription());
        dto.setVotes(resource.getVotes());
        if (resource.getCreatedBy() != null) {
            // Only passing the ID in createdBy field.
            UserDto userDto = new UserDto();
            userDto.setId(resource.getCreatedBy().getId());
            userDto.setName(resource.getCreatedBy().getName());
            dto.setCreatedBy(userDto);
        }

        return dto;
    }

    public static Resource mapToResource(ResourceDto dto) {
        // In this Resource object the user will be null.
        // The class using the method should set this explicitly.
        Resource resource = new Resource();
        resource.setId(dto.getId());
        resource.setTitle(dto.getTitle());
        resource.setDescription(dto.getDescription());
        resource.setVotes(dto.getVotes());

        return resource;
    }
}
