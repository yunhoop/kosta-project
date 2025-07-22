package com.oopsw.exerciseservice.jpa;

import java.time.LocalDate;

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
@Table(name = "exerciseTotal")
public class ExerciseTotalEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String memberId;

	@Column(nullable = false)
	private LocalDate exerciseDate;

	@Column(nullable = false)
	private Float exerciseTotalKcal;

	@Column(nullable = false)
	private String exerciseTotalId;



}
