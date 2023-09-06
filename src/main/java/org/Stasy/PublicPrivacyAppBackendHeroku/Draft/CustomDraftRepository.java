package org.Stasy.PublicPrivacyAppBackendHeroku.Draft;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface CustomDraftRepository {
    @Transactional
    <S extends Draft> S saveDraftWithCollaboratorName(S entity, String collaboratorName);

}
