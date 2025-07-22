package com.oopsw.commentservie.jpa;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "comment_id", unique = true, nullable = false)
	private String commentId;

	@Column(name = "comment_content")
	private String commentContent;

	@Column(name = "created_date", nullable = false, updatable = false, insertable = false)
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	private Date createdDate;

	@Column(name = "member_id")
	private String memberId;

	@Column(name = "board_id")
	private String boardId;

}
