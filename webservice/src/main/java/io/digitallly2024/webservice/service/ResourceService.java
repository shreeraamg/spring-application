package io.digitallly2024.webservice.service;

import io.digitallly2024.webservice.entity.Vote;
import io.digitallly2024.webservice.enums.ResourceEnums;
import io.digitallly2024.webservice.repository.CommentRepository;
import io.digitallly2024.webservice.repository.ResourceRepository;
import io.digitallly2024.webservice.dto.CommentDto;
import io.digitallly2024.webservice.dto.ResourceDto;
import io.digitallly2024.webservice.entity.Comment;
import io.digitallly2024.webservice.entity.Resource;
import io.digitallly2024.webservice.entity.User;
import io.digitallly2024.webservice.exception.ResourceNotFoundException;
import io.digitallly2024.webservice.mapper.CommentMapper;
import io.digitallly2024.webservice.mapper.ResourceMapper;
import io.digitallly2024.webservice.repository.VoteRepository;
import io.digitallly2024.webservice.request.CreateResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final CommentRepository commentRepository;

    private final ResourceRepository resourceRepository;

    private final VoteRepository voteRepository;

    private static final String NO_RESOURCE_FOUND_MESSAGE = "No Resource found with given id: ";

    private static final String NO_COMMENT_FOUND_MESSAGE = "No Comment found with given id: ";

    private static final String ACCESS_DENIED_TO_MODIFY_MESSAGE = "You are not allowed to modify this post.";

    @Autowired
    public ResourceService(
            CommentRepository commentRepository,
            ResourceRepository resourceRepository,
            VoteRepository voteRepository
    ) {
        this.commentRepository = commentRepository;
        this.resourceRepository = resourceRepository;
        this.voteRepository = voteRepository;
    }

    // ====================== RESOURCE ======================
    public ResourceDto createResource(CreateResourceRequest request) {
        User user = getCurrentUser();
        Resource resource = ResourceMapper.mapToResource(request);
        resource.setVotes(0L);
        resource.setCreatedBy(user);

        Resource savedResource = resourceRepository.save(resource);
        return ResourceMapper.mapToResourceDto(savedResource);
    }

    public Page<ResourceDto> getAllResources(String category, String query, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Resource> resources;

        if (category != null) {
            if (query != null) {
                resources = resourceRepository.findAllByResourceCategoryAndTitleContainingIgnoreCase(ResourceEnums.Category.valueOf(category), query, pageable);
            } else {
                resources = resourceRepository.findAllByResourceCategory(ResourceEnums.Category.valueOf(category), pageable);
            }
        } else if (query != null) {
            resources = resourceRepository.findAllByTitleContainingIgnoreCase(query, pageable);
        } else {
            resources = resourceRepository.findAll(pageable);
        }

        return resources.map(ResourceMapper::mapToResourceDto);
    }

    public ResourceDto getResourceById(Long resourceId) {
        Resource resource = fetchResourceById(resourceId);
        ResourceDto dto = ResourceMapper.mapToResourceDto(resource);

        if (getCurrentUser().getId() != null) {
            List<Comment> comments = commentRepository.findAllByResourceId(resourceId);
            List<CommentDto> commentDtoList = comments.stream().map(CommentMapper::mapToCommentDto).toList();
            dto.setComments(commentDtoList);
        }

        return dto;
    }

    public ResourceDto updateResource(Long resourceId, ResourceDto dto) {
        User user = getCurrentUser();
        Resource resource = fetchResourceById(resourceId);

        if (!resource.getCreatedBy().getId().equals(user.getId())) {
            throw new AccessDeniedException(ACCESS_DENIED_TO_MODIFY_MESSAGE);
        }

        resource.setTitle(dto.getTitle());
        resource.setDescription(dto.getDescription());
        Resource updatedResource = resourceRepository.save(resource);

        return ResourceMapper.mapToResourceDto(updatedResource);
    }

    public ResourceDto deleteResource(Long resourceId) {
        User user = getCurrentUser();
        Resource resource = fetchResourceById(resourceId);
        if (user.getId().equals(resource.getCreatedBy().getId()) || user.getRole().equals("ADMIN")) {
            resourceRepository.delete(resource);
            return ResourceMapper.mapToResourceDto(resource);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_TO_MODIFY_MESSAGE);
        }
    }

    // ====================== USER ======================
    public Page<ResourceDto> getResourcesByUserId(Long userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Resource> resources = resourceRepository.findAllByUserId(userId, pageable);
        return resources.map(ResourceMapper::mapToResourceDto);
    }

    // ====================== COMMENTS ======================
    public CommentDto addCommentToResource(Long resourceId, CommentDto commentDto) {
        User user = getCurrentUser();
        Resource resource = fetchResourceById(resourceId);

        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setResource(resource);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(savedComment);
    }

    public CommentDto getCommentById(Long commentId) {
        Comment comment = fetchCommentById(commentId);
        return CommentMapper.mapToCommentDto(comment);
    }

    public List<CommentDto> getAllCommentsByResourceId(Long resourceId) {
        List<Comment> comments = commentRepository.findAllByResourceId(resourceId);
        return comments.stream().map(CommentMapper::mapToCommentDto).toList();
    }

    public CommentDto updateComment(Long resourceId, Long commentId, CommentDto commentDto) {
        User user = getCurrentUser();
        Comment comment = fetchCommentById(commentId);

        // Only Owner of the Comment can update the post
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(ACCESS_DENIED_TO_MODIFY_MESSAGE);
        }

        comment.setName(commentDto.getName());
        Comment updatedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(updatedComment);
    }

    public CommentDto deleteComment(Long resourceId, Long commentId) {
        User user = getCurrentUser();
        Comment comment = fetchCommentById(commentId);

        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isOwnerOfResource = comment.getResource().getCreatedBy().getId().equals(user.getId());
        boolean isOwnerOfComment = comment.getUser().getId().equals(user.getId());

        if (isAdmin || isOwnerOfResource || isOwnerOfComment) {
            commentRepository.delete(comment);
            return CommentMapper.mapToCommentDto(comment);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_TO_MODIFY_MESSAGE);
        }
    }

    // ====================== VOTES ======================
    public void modifyVoteForResource(Long resourceId) {
        User user = getCurrentUser();
        Resource resource = fetchResourceById(resourceId);
        Optional<Vote> optionalVote = voteRepository.findByResourceIdAndUserId(resourceId, user.getId());
        if (optionalVote.isEmpty()) {
            voteRepository.save(new Vote(resourceId, user.getId(), user.getName()));
            resource.setVotes(resource.getVotes() + 1);
        } else {
            voteRepository.delete(optionalVote.get());
            resource.setVotes(resource.getVotes() - 1);
        }
        resourceRepository.save(resource);
    }

    // ====================== INTERNALLY USED METHODS ======================
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal == null || principal.equals("anonymousUser") ? new User() : (User) principal;
    }

    private Resource fetchResourceById(Long resourceId) {
        return resourceRepository
                .findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RESOURCE_FOUND_MESSAGE + resourceId));
    }

    private Comment fetchCommentById(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(NO_COMMENT_FOUND_MESSAGE + commentId));
    }

}
