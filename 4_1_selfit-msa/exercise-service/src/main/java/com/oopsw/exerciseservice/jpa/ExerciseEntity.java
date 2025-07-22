package com.oopsw.exerciseservice.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercise")
public class ExerciseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String memberId;

	@Column(nullable = false, unique = true)
	private String exerciseId;

	@Column(nullable = false)
	private LocalDate exerciseDate;

	@Column(nullable = false)
	private String exerciseName;

	@Column(nullable = false)
	private Integer exerciseMin;

	@Column(nullable = false)
	private Float exerciseKcal;

	@Column(nullable = false)
	private Float met;

}
