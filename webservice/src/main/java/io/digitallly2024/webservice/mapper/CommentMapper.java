package io.digitallly2024.webservice.mapper;

import io.digitallly2024.webservice.dto.CommentDto;
import io.digitallly2024.webservice.entity.Comment;

public class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setResourceId(comment.getResource().getId());

        return commentDto;
    }

}
