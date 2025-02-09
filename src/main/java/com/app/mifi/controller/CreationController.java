package com.app.mifi.controller;

import com.app.mifi.controller.model.CreationRequest;
import com.app.mifi.controller.model.CreationResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.CreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import static com.app.mifi.constant.Constant.REQUEST_BY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class CreationController {
    private final CreationService creationService;

    @PostMapping(value="/creations",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MiFiResponse<CreationResponse>> createCreation(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                         @ModelAttribute CreationRequest creationRequest) throws IOException {
        log.info("Uploading Creation for user [{}], requestedBy [{}]",creationRequest.getUserId(),requestBy);
        CreationResponse response = creationService.saveCreation(creationRequest,creationRequest.getCreation());
        return ResponseEntity.status(201).body(MiFiResponse.<CreationResponse>builder().response(response).build());
    }

    @GetMapping(value="/{userId}/creations/all")
    public ResponseEntity<MiFiResponse<List<CreationResponse>>> fetchAllCreations(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                 @PathVariable Long userId){
        log.info("Fetching all Creations for user [{}], requestedBy [{}]",userId,requestBy);
        List<CreationResponse> response = creationService.fetchAllCreations(userId);
        return ResponseEntity.status(200).body(MiFiResponse.<List<CreationResponse>>builder().response(response).build());
    }

    @GetMapping(value="/{userId}/creations/feed")
    public ResponseEntity<MiFiResponse<List<CreationResponse>>> fetchAllFeedCreations(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                  @PathVariable Long userId){
        log.info("Fetching all Feed Creations for user [{}], requestedBy [{}]",userId,requestBy);
        List<CreationResponse> response = creationService.fetchAllFeedCreations(userId);
        return ResponseEntity.status(200).body(MiFiResponse.<List<CreationResponse>>builder().response(response).build());
    }

    @GetMapping(value="/creations/{creationId}")
    public ResponseEntity<MiFiResponse<CreationResponse>> fetchCreationById(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                  @PathVariable Long creationId){
        log.info("Fetching Creation with creationId [{}], requestedBy [{}]",creationId,requestBy);
        CreationResponse response = creationService.fetchCreationById(creationId);
        return ResponseEntity.status(200).body(MiFiResponse.<CreationResponse>builder().response(response).build());
    }

    @PutMapping(value="/{userId}/creations/{creationId}")
    public ResponseEntity<MiFiResponse<CreationResponse>> updateCreationById(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                             @PathVariable Long userId, @PathVariable Long creationId,
                                                                             @RequestParam String caption){
        log.info("Updating Creation with creationId [{}], requestedBy [{}]",creationId,requestBy);
        CreationResponse response = creationService.updateCreationById(userId,creationId,caption);
        return ResponseEntity.status(200).body(MiFiResponse.<CreationResponse>builder().response(response).build());
    }

    @DeleteMapping(value="/{userId}/creations/{creationId}")
    public ResponseEntity<MiFiResponse<Boolean>> deleteCreationById(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                            @PathVariable Long userId,
                                                                            @PathVariable Long creationId){
        log.info("Deleting creation for with creationId [{}], requestedBy [{}]",creationId,requestBy);
        Boolean response = creationService.deleteCreationById(userId,creationId);
        return ResponseEntity.status(201).body(MiFiResponse.<Boolean>builder().response(response).build());
    }
}
