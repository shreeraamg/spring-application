package io.digitallly2024.webservice.controller;

import io.digitallly2024.webservice.dto.CommentDto;
import io.digitallly2024.webservice.dto.ResourceDto;
import io.digitallly2024.webservice.request.CreateResourceRequest;
import io.digitallly2024.webservice.response.ResponseMessage;
import io.digitallly2024.webservice.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "Resource API")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Create a new Resource")
    public ResponseEntity<ResourceDto> createResource(@RequestBody CreateResourceRequest request) {
        ResourceDto dto = resourceService.createResource(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @PostMapping(
            path = "/batch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Create Resources in Batch")
    public ResponseEntity<ResponseMessage> createResources(@RequestBody List<CreateResourceRequest> request) {
        ResponseMessage response = resourceService.createResources(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    @Operation(summary = "Get all Resources")
    public ResponseEntity<Page<ResourceDto>> getAllResources(
            @RequestParam(required = false) String category,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        Page<ResourceDto> resourceDtoPage = resourceService.getAllResources(category, query, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(resourceDtoPage);
    }


    @GetMapping(path = "/{resourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    @Operation(summary = "Get Resource by Id")
    public ResponseEntity<ResourceDto> getResourceById(@PathVariable Long resourceId) {
        ResourceDto dto = resourceService.getResourceById(resourceId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @PutMapping(path = "/{resourceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update a Resource")
    public ResponseEntity<ResourceDto> updateResource(
            @PathVariable Long resourceId,
            @RequestBody ResourceDto dto
    ) {
        ResourceDto updatedResource = resourceService.updateResource(resourceId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedResource);
    }


    @DeleteMapping(path = "/{resourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Delete a Resource")
    public ResponseEntity<ResourceDto> deleteResource(@PathVariable Long resourceId) {
        ResourceDto dto = resourceService.deleteResource(resourceId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @GetMapping(path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Get all Resources by User")
    public ResponseEntity<Page<ResourceDto>> getAllResourcesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        Page<ResourceDto> resourceDtoPage = resourceService.getResourcesByUserId(userId, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(resourceDtoPage);
    }


    @PostMapping(path = "/{resourceId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add Comment to a Resource")
    public ResponseEntity<CommentDto> addCommentToResource(
            @PathVariable Long resourceId,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto dto = resourceService.addCommentToResource(resourceId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @GetMapping(path = "/{resourceId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Get Comments by Resource")
    public ResponseEntity<List<CommentDto>> getAllCommentsByResource(@PathVariable Long resourceId) {
        List<CommentDto> dtoList = resourceService.getAllCommentsByResourceId(resourceId);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }


    @PutMapping(path = "/{resourceId}/comments/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update a Comment")
    public ResponseEntity<CommentDto> updateResource(@PathVariable Long resourceId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        CommentDto dto = resourceService.updateComment(resourceId, commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @DeleteMapping(path = "/{resourceId}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Delete a Comment")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long resourceId, @PathVariable Long commentId) {
        CommentDto dto = resourceService.deleteComment(resourceId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PatchMapping(path = "/{resourceId}/votes")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Vote or Remove Vote for a Resource")
    public ResponseEntity<ObjectUtils.Null> modifyVoteForResource(@PathVariable Long resourceId) {
        resourceService.modifyVoteForResource(resourceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
