package com.oopsw.foodservice.jpa;

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
@Table(name = "foodTotal")
public class FoodTotalEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String memberId;

	@Column(nullable = false)
	private LocalDate intakeDate;

	@Column(nullable = false)
	private Float foodTotalKcal;

	@Column(nullable = false)
	private String foodTotalId;

}
