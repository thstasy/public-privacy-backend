package org.Stasy.PublicPrivacyAppBackendHeroku.Draft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DraftService {

    private final DraftRepository draftRepository;

    @Autowired
    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }


}
