package com.app.mifi.persist.entity;

import com.app.mifi.controller.model.CreationResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Entity
@Table(name = "creations")
@Data
public class Creation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creation_id")
    private Long creationId;

    @Column(name = "caption", length = 255)
    private String caption;

    @Lob
    @Column(name = "creation")
    private byte[] creation;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    private User user;

    public CreationResponse toDtos(){
        CreationResponse creationResponse= CreationResponse.builder().build();
        BeanUtils.copyProperties(this,creationResponse);
        return creationResponse;
    }
    public CreationResponse toDto(){
        CreationResponse creationResponse= CreationResponse.builder().build();
        BeanUtils.copyProperties(this,creationResponse);
        if(nonNull(this.getCreation())) {
            byte[] fileBytes = this.getCreation();
            String creationPic = Base64.getEncoder().encodeToString(fileBytes);
            creationResponse.setCreationBase64(creationPic);
        }
        creationResponse.setUser(user.toDto());
        //remove null check
        if (nonNull(createdDate)) {
            creationResponse.setCreatedDate(createdDate.format(DateTimeFormatter.ISO_DATE));
        }
        if(nonNull(updatedDate)) {
            creationResponse.setUpdatedDate(updatedDate.format(DateTimeFormatter.ISO_DATE));
        }
        return creationResponse;
    }
}
