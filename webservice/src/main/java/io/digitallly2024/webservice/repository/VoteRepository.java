package io.digitallly2024.webservice.repository;

import io.digitallly2024.webservice.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByResourceId(Long resourceId);

    long countByResourceId(Long resourceId);

    Optional<Vote> findByResourceIdAndUserId(Long resourceId, Long userId);

}
