package io.digitallly2024.webservice.mapper;

import io.digitallly2024.webservice.dto.ResourceDto;
import io.digitallly2024.webservice.dto.UserDto;
import io.digitallly2024.webservice.entity.Resource;
import io.digitallly2024.webservice.enums.ResourceEnums;
import io.digitallly2024.webservice.request.CreateResourceRequest;

public class ResourceMapper {

    private ResourceMapper() {}

    public static ResourceDto mapToResourceDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setDescription(resource.getDescription());
        dto.setCategory(resource.getResourceCategory().name());
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
        resource.setResourceCategory(ResourceEnums.Category.valueOf(dto.getCategory()));
        resource.setVotes(dto.getVotes());

        return resource;
    }

    public static Resource mapToResource(CreateResourceRequest request) {
        Resource resource = new Resource();
        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setResourceCategory(ResourceEnums.Category.valueOf(request.getCategory()));

        return resource;
    }
}
