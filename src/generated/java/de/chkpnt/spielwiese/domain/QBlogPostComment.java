package de.chkpnt.spielwiese.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlogPostComment is a Querydsl query type for BlogPostComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBlogPostComment extends EntityPathBase<BlogPostComment> {

    private static final long serialVersionUID = -1368573186L;

    public static final QBlogPostComment blogPostComment = new QBlogPostComment("blogPostComment");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final StringPath comment = createString("comment");

    public final StringPath commenter = createString("commenter");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBlogPostComment(String variable) {
        super(BlogPostComment.class, forVariable(variable));
    }

    public QBlogPostComment(Path<BlogPostComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlogPostComment(PathMetadata metadata) {
        super(BlogPostComment.class, metadata);
    }

}

