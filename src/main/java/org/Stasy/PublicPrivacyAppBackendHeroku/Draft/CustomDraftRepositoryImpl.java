package org.Stasy.PublicPrivacyAppBackendHeroku.Draft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CustomDraftRepositoryImpl implements CustomDraftRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final DraftRepository draftRepository;

    public CustomDraftRepositoryImpl(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }


    @Override
    public <S extends Draft> S saveDraftWithCollaboratorName(S entity, String collaboratorName) {
        entity.setCollaboratorName(collaboratorName);
        S savedEntity = draftRepository.save(entity); // Use the save method from JpaRepository
        entityManager.flush();
        return savedEntity;
    }

}