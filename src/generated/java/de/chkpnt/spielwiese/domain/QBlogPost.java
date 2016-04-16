package de.chkpnt.spielwiese.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlogPost is a Querydsl query type for BlogPost
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBlogPost extends EntityPathBase<BlogPost> {

    private static final long serialVersionUID = -347499487L;

    public static final QBlogPost blogPost = new QBlogPost("blogPost");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final StringPath author = createString("author");

    public final ListPath<BlogPostComment, QBlogPostComment> comments = this.<BlogPostComment, QBlogPostComment>createList("comments", BlogPostComment.class, QBlogPostComment.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath text = createString("text");

    public QBlogPost(String variable) {
        super(BlogPost.class, forVariable(variable));
    }

    public QBlogPost(Path<BlogPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlogPost(PathMetadata metadata) {
        super(BlogPost.class, metadata);
    }

}

