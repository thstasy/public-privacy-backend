package org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository;

import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomOpinionRepository {
    @Transactional
    <S extends Opinion> S saveOpinionWithCollaboratorName(S entity, String collaboratorName);
}
