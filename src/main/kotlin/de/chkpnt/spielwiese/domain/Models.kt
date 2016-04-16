package de.chkpnt.spielwiese.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import java.time.LocalDateTime
import java.util.Collections
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.JoinColumn
import javax.persistence.CascadeType
import javax.persistence.Table
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Column
import java.sql.Array

@Entity
class BlogPost : AbstractPersistable<Long>() {
		
		var author: String = ""

		var text: String = ""

		@OneToMany(cascade=arrayOf(CascadeType.ALL), orphanRemoval=true)
		@JoinColumn(name="BLOGPOST_ID", nullable=true)
		var comments: List<BlogPostComment> = Collections.emptyList()
}

@Entity
class BlogPostComment : AbstractPersistable<Long>() {
		var date: LocalDateTime? = null
	
		var commenter: String = ""
	
		var comment: String = ""
	
}

interface BlogPostRepository : JpaRepository<BlogPost, Long>, QueryDslPredicateExecutor<BlogPost>

interface BlogPostCommentRepository : JpaRepository<BlogPostComment, Long>, QueryDslPredicateExecutor<BlogPostComment>
