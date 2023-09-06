package org.Stasy.PublicPrivacyAppBackendHeroku.Draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface DraftRepository extends JpaRepository<Draft, Long> {
   // Draft saveDraft(Draft draft);

    List<Draft> findAllByOrderByUpdatedAtDesc();

    Draft findDraftById(Long id);

//    @Query(value = "SELECT * FROM dev_draft WHERE collaborator_name = :username ORDER BY updated_at DESC", nativeQuery = true)
    List<Draft> findAllByCollaboratorNameOrderByUpdatedAtDesc(String username);

}
