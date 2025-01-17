package com.app.mifi.controller;

import com.app.mifi.controller.model.BuddyRequestResponse;
import com.app.mifi.controller.model.MutualBuddyResponse;
import com.app.mifi.controller.model.UserBuddyResponse;
import com.app.mifi.controller.model.UserRelationship;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.BuddyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static com.app.mifi.constant.Constant.REQUEST_BY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class BuddyController {
    private final BuddyService buddyService;

    @PostMapping("{userId}/buddy-request/{reqUserId}")
    public ResponseEntity<MiFiResponse<BuddyRequestResponse>> createBuddyRequest(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                 @PathVariable Long userId, @PathVariable Long reqUserId ) {
        log.info("Buddy requested by user [{}], requestedBy [{}]",userId,requestBy);
        BuddyRequestResponse response = buddyService.createBuddyRequest(userId,reqUserId);
        return ResponseEntity.status(201).body(MiFiResponse.<BuddyRequestResponse>builder().response(response).build());
    }

    @DeleteMapping("/requests/{requestId}")
    public ResponseEntity<MiFiResponse<Boolean>> rejectBuddyRequest(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                 @PathVariable Long requestId) {
        log.info("Reject Buddy requested by requestId [{}], requestedBy [{}]",requestId,requestBy);
        Boolean response = buddyService.rejectBuddyRequest(requestId);
        return ResponseEntity.status(201).body(MiFiResponse.<Boolean>builder().response(response).build());
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<MiFiResponse<List<BuddyRequestResponse>>> fetchAllBuddyRequests(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                          @PathVariable Long userId) {
        log.info("Fetching Buddy requests of user [{}], requestedBy [{}]",userId,requestBy);
        List<BuddyRequestResponse> response = buddyService.fetchBuddyRequests(userId);
        return ResponseEntity.status(200).body(MiFiResponse.<List<BuddyRequestResponse>>builder().response(response).build());
    }

    @GetMapping("/suggest-buddies/{userId}")
    public ResponseEntity<MiFiResponse<List<MutualBuddyResponse>>> suggestBuddies(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                  @PathVariable Long userId) {
        log.info("Suggesting Buddies for user [{}], requestedBy [{}]",userId,requestBy);
        List<MutualBuddyResponse> response = buddyService.suggestBuddies(userId);
        return ResponseEntity.status(200).body(MiFiResponse.<List  <MutualBuddyResponse>>builder().response(response).build());
    }

    @PostMapping("{userId}/add-buddy/{requesterUserId}")
    public ResponseEntity<MiFiResponse<UserBuddyResponse>> createUserBuddy(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                           @PathVariable Long userId, @PathVariable Long requesterUserId) {
        log.info("Add Buddy approved by user [{}], requested By User[{}], requestedBy [{}]",userId,requesterUserId,requestBy);
        UserBuddyResponse response = buddyService.createUserBuddy(userId,requesterUserId);
        return ResponseEntity.status(201).body(MiFiResponse.<UserBuddyResponse>builder().response(response).build());
    }

    @GetMapping("{userId}/view-profile/relation/{viewUserId}")
    public ResponseEntity<MiFiResponse<UserRelationship>> relationshipWithViewedUser(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                                    @PathVariable Long userId,
                                                                                    @PathVariable Long viewUserId) {
        log.info("View profile relation for user [{}], requestedBy [{}]",userId,requestBy);
        UserRelationship response = buddyService.checkViewUserRelationship(userId,viewUserId);
        return ResponseEntity.status(200).body(MiFiResponse.<UserRelationship>builder().response(response).build());
    }

    @DeleteMapping("{userId}/remove-buddy/{buddyUserId}")
    public ResponseEntity<MiFiResponse<Boolean>> removeUserBuddy(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                           @PathVariable Long userId, @PathVariable Long buddyUserId) {
        log.info("Remove Buddy requested by user [{}], requested By User[{}], requestedBy [{}]",userId,buddyUserId,requestBy);
        Boolean response = buddyService.removeUserBuddy(userId,buddyUserId);
        return ResponseEntity.status(201).body(MiFiResponse.<Boolean>builder().response(response).build());
    }
}
