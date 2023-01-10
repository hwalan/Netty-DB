package com.netty.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LOG_TABLE")
@SequenceGenerator(name = "generator", sequenceName = "LOG_TABLE_SEQ", allocationSize = 1)
public class LogEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@Column(name = "SEQUENCE")
	private Long sequence;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "MESSAGE")
	private String message;
	
	@Column(name = "LOGTIME")
	private LocalDateTime time;

}
