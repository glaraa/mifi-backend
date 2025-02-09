package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MutualBuddyResponse {
    private UserResponse suggestedUser;
    private List<UserResponse> mutualUsers;
    private Integer mutualCount;
    private SuggestionType suggestionType;

}
