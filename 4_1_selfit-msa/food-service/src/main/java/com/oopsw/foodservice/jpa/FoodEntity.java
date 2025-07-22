package com.oopsw.foodservice.jpa;

import java.io.Serializable;
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
@Table(name = "food")
public class FoodEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String foodId;
	@Column(nullable = false)
	private Date intakeDate;
	@Column(nullable = false)
	private String foodName;
	@Column(nullable = false)
	private Float intake;
	@Column(nullable = false)
	private Float intakeKcal;
	@Column(nullable = false)
	private Integer unitKcal;

	@Column(nullable = false)
	private String memberId;
}
