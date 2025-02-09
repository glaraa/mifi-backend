package com.app.mifi.persist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="feedback")
@Getter
@Setter
public class FeedBack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackId;

	@Column(name = "username")
	private String username;

	@Column(name = "mail")
	private String mailId;

	@Column(name = "feedback")
	private String feedback;

	@Column(name = "feedback_at")
	private LocalDate feedbackAt;
}