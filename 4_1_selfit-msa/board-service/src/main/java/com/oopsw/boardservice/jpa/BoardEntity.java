package com.oopsw.boardservice.jpa;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

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

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="board")
public class BoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String boardTitle;

	@Column(nullable = false)
	private String boardContent;

	@Column(nullable = false)
	private String categoryName;

	@Column()
	private String boardImg;

	@Column(nullable = false, updatable = false, insertable = false)
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	private Date createdDate;

	@Column(nullable = false)
	private int viewCount;

	@Column(nullable = false, unique = true)
	private String boardId;
	@Column(nullable = false)
	private String memberId;

}
