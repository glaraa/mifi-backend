package com.app.mifi.service;

import com.app.mifi.controller.model.CreationRequest;
import com.app.mifi.controller.model.CreationResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface CreationService {
    CreationResponse saveCreation(CreationRequest creationRequest, MultipartFile creation) throws IOException;

    List<CreationResponse> fetchAllCreations(Long userId);

    CreationResponse fetchCreationById(Long creationId);

    List<CreationResponse> fetchAllFeedCreations(Long userId);

    Boolean deleteCreationById(Long userId, Long creationId);

    CreationResponse updateCreationById(Long userId, Long creationId, String caption);
}
