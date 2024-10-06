package io.digitallly2024.webservice.repository;

import io.digitallly2024.webservice.entity.Resource;
import io.digitallly2024.webservice.enums.ResourceEnums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r from Resource r WHERE r.createdBy.id = :userId")
    Page<Resource> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    Page<Resource> findAllByResourceCategory(ResourceEnums.Category category, Pageable pageable);

    Page<Resource> findAllByTitleContainingIgnoreCase(String query, Pageable pageable);

    Page<Resource> findAllByResourceCategoryAndTitleContainingIgnoreCase(
            ResourceEnums.Category category, String query, Pageable pageable
    );

}
