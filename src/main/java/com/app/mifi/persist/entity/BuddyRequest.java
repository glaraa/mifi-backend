package com.app.mifi.persist.entity;

import com.app.mifi.controller.model.BuddyRequestResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.util.Date;
import static java.util.Objects.isNull;

@Entity
@Table(name = "buddy_requests")
@Data
public class BuddyRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "recipient_user_id", nullable = false)
    private User recipientUser;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requesterUser;

    @Column(name = "sent_at")
    private Date sentAt;

    @Column(name = "seen")
    private Boolean hasSeen;

    public void setSentAt(Date sentAt) {
        if(isNull(this.sentAt))
            this.sentAt = new Date();
    }
    public BuddyRequestResponse toDto(){
        BuddyRequestResponse requestResponse= BuddyRequestResponse.builder().build();
        BeanUtils.copyProperties(this,requestResponse);
        requestResponse.setRequesterUser(this.requesterUser.toDtos());
        requestResponse.setRecipientUser(this.recipientUser.toDtos());
        return requestResponse;
    }

}
