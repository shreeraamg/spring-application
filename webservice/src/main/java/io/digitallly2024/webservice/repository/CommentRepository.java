package io.digitallly2024.webservice.repository;

import io.digitallly2024.webservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.resource.id = :resourceId")
    List<Comment> findAllByResourceId(@Param("resourceId") Long resourceId);

}
