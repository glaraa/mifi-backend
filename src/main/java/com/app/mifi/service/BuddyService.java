package com.app.mifi.service;

import com.app.mifi.controller.model.BuddyRequestResponse;
import com.app.mifi.controller.model.MutualBuddyResponse;
import com.app.mifi.controller.model.UserBuddyResponse;
import com.app.mifi.controller.model.UserRelationship;
import java.util.List;

public interface BuddyService {
    BuddyRequestResponse createBuddyRequest(Long userId,Long reqUserId);

    List<BuddyRequestResponse> fetchBuddyRequests(Long userId);

    List<MutualBuddyResponse> suggestBuddies(Long userId);

    UserBuddyResponse createUserBuddy(Long userId, Long requesterUserId);

    UserRelationship checkViewUserRelationship(Long userId, Long viewUserId);

    Boolean removeUserBuddy(Long userId, Long buddyUserId);

    Boolean rejectBuddyRequest(Long requestId);
}
