package com.app.mifi.controller;

import com.app.mifi.controller.model.CreationRequest;
import com.app.mifi.controller.model.CreationResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.CreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class CreationController {
    private final CreationService creationService;
    @PostMapping(value="/creations",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MiFiResponse<CreationResponse>> createCreation(@RequestHeader(name = "RequestBy") String requestBy,
                                                                         @ModelAttribute CreationRequest creationRequest) throws IOException {
        log.info("Uploading Creation for user [{}], requestedBy [{}]",creationRequest.getUserId(),requestBy);
        CreationResponse response = creationService.saveCreation(creationRequest,creationRequest.getCreation());
        return ResponseEntity.status(201).body(MiFiResponse.<CreationResponse>builder().response(response).build());
    }

    @GetMapping(value="/{userId}/creations/all")
    public ResponseEntity<MiFiResponse<List<CreationResponse>>> fetchAllCreations(@RequestHeader(name = "RequestBy") String requestBy,
                                                                                 @PathVariable Long userId){
        log.info("Fetching all Creations for user [{}], requestedBy [{}]",userId,requestBy);
        List<CreationResponse> response = creationService.fetchAllCreations(userId);
        return ResponseEntity.status(200).body(MiFiResponse.<List<CreationResponse>>builder().response(response).build());
    }

    @GetMapping(value="/creations/{creationId}")
    public ResponseEntity<MiFiResponse<CreationResponse>> fetchCreationById(@RequestHeader(name = "RequestBy") String requestBy,
                                                                                  @PathVariable Long userId,
                                                                                  @PathVariable Long creationId
                                                                            ){
        log.info("Fetching all Creations for user [{}] with creationId [{}], requestedBy [{}]",userId,creationId,requestBy);
        CreationResponse response = creationService.fetchCreationById(creationId);
        return ResponseEntity.status(200).body(MiFiResponse.<CreationResponse>builder().response(response).build());
    }
}
