package de.chkpnt.spielwiese.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import de.chkpnt.spielwiese.SpielwieseConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpielwieseConfig.class)
@DataJpaTest
public class QueryDslExperimente {

	@Autowired
	private BlogPostRepository blogPostRepository;

	@Autowired
	private BlogPostCommentRepository blogPostCommentRepository;

	@Autowired
	private JPAQueryFactory queryFactory;

	@BeforeTransaction
	public void initData() {
		BlogPostComment comment1 = new BlogPostComment();
		comment1.setDate(LocalDate.of(2016, 4, 1)
			.atTime(14, 30));
		comment1.setCommenter("Alfred Alfons");
		comment1.setComment("Sehr gut!");

		BlogPostComment comment2 = new BlogPostComment();
		comment2.setDate(LocalDate.of(2016, 4, 15)
			.atTime(18, 45));
		comment2.setCommenter("Berta Bond");
		comment2.setComment("Naja!");

		BlogPost blogPost1 = new BlogPost();
		blogPost1.setComments(ImmutableList.of(comment1, comment2));
		blogPost1.setText("Blog Post 1");
		blogPostRepository.save(blogPost1);

		BlogPostComment comment3 = new BlogPostComment();
		comment3.setDate(LocalDate.of(2016, 3, 10)
			.atTime(14, 30));
		comment3.setCommenter("Charly Chen");
		comment3.setComment("Weiter so!");

		BlogPostComment comment4 = new BlogPostComment();
		comment4.setDate(LocalDate.of(2016, 4, 20)
			.atTime(18, 45));
		comment4.setCommenter("Danny Do");
		comment4.setComment("Auf auf!");

		BlogPost blogPost2 = new BlogPost();
		blogPost2.setComments(ImmutableList.of(comment3, comment4));
		blogPost2.setText("Blog Post 2");
		blogPostRepository.save(blogPost2);
	}

	@AfterTransaction
	public void teardownData() {
		blogPostRepository.deleteAll();
	}

	@Test
	public void testRepositorySize() {
		assertThat(blogPostRepository.count(), is(2L));
		assertThat(blogPostCommentRepository.count(), is(4L));
	}

	@Test
	public void testQueryUeberJPAQuery() {
		BlogPost blogPost = queryFactory.selectFrom(QBlogPost.blogPost)
			.innerJoin(QBlogPost.blogPost.comments, QBlogPostComment.blogPostComment)
			.where(QBlogPostComment.blogPostComment.commenter.startsWith("Danny"))
			.fetchOne();

		assertThat(blogPost.getText(), is("Blog Post 2"));
	}

	@Test
	public void testDateVergleich() {
		QBlogPostComment q = QBlogPostComment.blogPostComment;
		Iterable<BlogPostComment> comments = blogPostCommentRepository.findAll(q.date.before(LocalDateTime.of(2016, 4, 4, 0, 0)));

		// Wenn LocalDateTimeConverter nicht geladen ist, wird die date-Spalte
		// nicht mit dem SQL-Typ "TIMESTAMP" erzeugt und der Query erzeugt ein
		// leeres Ergebnis.
		assertThat(comments, iterableWithSize(2));
	}

	@Test
	public void alleBlogPostsDerenErsterKommentarNachDatumXErschienenIst() {
		assertBlogPostsDerenErsterKommentarNachDatumXErschienenIst(LocalDate.of(2016, 3, 1), "Blog Post 1", "Blog Post 2");
		assertBlogPostsDerenErsterKommentarNachDatumXErschienenIst(LocalDate.of(2016, 3, 15), "Blog Post 1");
		assertBlogPostsDerenErsterKommentarNachDatumXErschienenIst(LocalDate.of(2016, 4, 15));
	}

	private void assertBlogPostsDerenErsterKommentarNachDatumXErschienenIst(LocalDate x, String... expectedBlogPosts) {
		LocalDateTime thresholdDate = x.atStartOfDay();

		Iterable<BlogPost> queryResult = blogPostRepository.findAll(internalFoobar(QBlogPost.blogPost, thresholdDate));

		List<String> blogPosts = StreamSupport.stream(queryResult.spliterator(), false)
			.map(BlogPost::getText)
			.collect(Collectors.toList());

		if (ObjectUtils.isEmpty(expectedBlogPosts)) {
			assertThat(blogPosts, empty());
		} else {
			assertThat(blogPosts, containsInAnyOrder(expectedBlogPosts));
		}
	}

	private Predicate internalFoobar(QBlogPost q, LocalDateTime threshold) {
		QBlogPost p = new QBlogPost("p");
		QBlogPostComment c = new QBlogPostComment("c");

		JPQLQuery<BlogPost> relevanteBlogPosts = JPAExpressions.select(p)
			.from(p)
			.innerJoin(p.comments, c)
			.groupBy(p)
			.having(c.date.min()
				.after(threshold));

		return q.in(relevanteBlogPosts);
	}
}
