package com.app.mifi.service.impl;

import com.app.mifi.controller.model.CreationRequest;
import com.app.mifi.controller.model.CreationResponse;
import com.app.mifi.persist.entity.Creation;
import com.app.mifi.persist.entity.User;
import com.app.mifi.repository.CreationRepository;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.CreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreationServiceImpl implements CreationService {

    private final UserRepository userRepository;
    private final CreationRepository creationRepository;

    @Override
    public CreationResponse saveCreation(CreationRequest creationRequest, MultipartFile creationFile) throws IOException {
        Creation creation= new Creation();
        creation.setCreation(creationFile.getBytes());
        creation.setCaption(creationRequest.getCaption());
        creation.setUser(userRepository.findById(creationRequest.getUserId()).orElse(new User()));
        return creationRepository.save(creation).toDto();
    }

    @Override
    public List<CreationResponse> fetchAllCreations(Long userId) {
        List<CreationResponse> creationResponses=new ArrayList<>();
        creationRepository.findAllByUser_UserIdOrderByCreationIdDesc(userId).forEach(creation ->
                creationResponses.add(creation.toDtos()) );
        return creationResponses;
    }

    @Override
    public CreationResponse fetchCreationById(Long creationId) {
        return creationRepository.findById(creationId).orElse(new Creation()).toDto();
    }
}
