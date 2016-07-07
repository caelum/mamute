package org.mamute.model;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.vraptor.environment.MamuteEnvironment;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mamute.model.MarkedText.notMarked;
import static org.mockito.Mockito.mock;

public class UserTest extends TestCase {

	private User author;
	private User moderator;
	private User otherUser;
	private QuestionBuilder question = new QuestionBuilder();
	private Question myQuestion;
	private Question myOtherQuestion;
	private User moderatorWannabe;

	@Before
	public void before_test() {
		author = user("name", "email");
		author.setId(1l);
		otherUser = user("other", "email");
		otherUser.setId(2l);

		myQuestion = question.withTitle("question title")
				.withDescription("question description").withAuthor(author)
				.build();
		myOtherQuestion = question.withTitle("question title")
				.withDescription("question description").withAuthor(otherUser)
				.build();

		moderatorWannabe = user("juaum", "ju@aum.com");
		moderatorWannabe.setKarma(2000);
		
		moderator = user("yeah", "email").asModerator();
		moderator.setId(3l);
	}

	@Test
	public void moderator_should_approve_question_information()
			throws Exception {
		Information approvedInfo = new QuestionInformation("edited title",
				notMarked("edited desc"), new LoggedUser(otherUser, null),
				new ArrayList<Tag>(), "comment");

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		moderator.approve(myQuestion, approvedInfo, env);

		assertEquals(approvedInfo, myQuestion.getInformation());
		assertTrue(myQuestion.getInformation().isModerated());
	}
	
	@Test
	public void user_with_enough_karma_should_approve_question_information()
			throws Exception {
		Information approvedInfo = new QuestionInformation("edited title",
				notMarked("edited desc"), new LoggedUser(otherUser, null),
				new ArrayList<Tag>(), "comment");

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		moderatorWannabe.approve(myQuestion, approvedInfo, env);
		
		assertEquals(approvedInfo, myQuestion.getInformation());
		assertTrue(myQuestion.getInformation().isModerated());
	}

	@Test
	public void moderator_should_approve_answer_information() throws Exception {
		Answer answer = answer("answer description", myQuestion, author);
		AnswerInformation approvedInfo = answerInformation("new description",
				otherUser, answer);

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		moderator.approve(answer, approvedInfo, env);

		assertEquals(approvedInfo, answer.getInformation());
		assertTrue(answer.getInformation().isModerated());
	}
	
	@Test
	public void user_with_enough_karma_should_approve_answer_information() throws Exception {
		Answer answer = answer("answer description", myQuestion, author);
		AnswerInformation approvedInfo = answerInformation("new description",
				otherUser, answer);

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new MamuteEnvironment(ctx));
		moderatorWannabe.approve(answer, approvedInfo, env);
		
		assertEquals(approvedInfo, answer.getInformation());
		assertTrue(answer.getInformation().isModerated());
	}

	@Test
	public void should_verify_if_its_author_of_the_question() {
		assertTrue(author.isAuthorOf(myQuestion));
		assertFalse(author.isAuthorOf(myOtherQuestion));
	}

	@Test
	public void should_update_password_if_confirmation_matches()
			throws Exception {
		User user = user("name", "name@brutal.com");
		assertFalse(user.updateForgottenPassword("12345", "123456"));
		assertTrue(user.updateForgottenPassword("123456", "123456"));
	}

	@Test
	public void should_avatar_when_null_photo() throws Exception {
		User user = user("name", "name@brutal.com");
		String photo = user.getPhoto(10, 10, "http://www.gravatar.com");
		assertTrue(photo.startsWith("http://www.gravatar.com"));
	}
	
	@Test
	public void should_have_PG_gravatar_with_robotar_when_null_photo() throws Exception {
		User user = user("name", "paulo@paulo.com.br");
		String photo = user.getPhoto(64, 64, "http://www.gravatar.com");
		assertEquals("http://www.gravatar.com/avatar/620ad6ac2c42fce964bbf2e01e87c04b.png?r=PG&size=64x64&d=https%3A%2F%2Frobohash.org%2Fsize_64x64%2Fset_set1%2Fbgset_any%2F620ad6ac2c42fce964bbf2e01e87c04b.png", photo);
	}

	@Test
	public void should_use_width_and_height_parameters_when_photo_uri_is_from_facebook() throws Exception {
		User user = user("name", "paulo@paulo.com.br");
		user.setPhotoUri(new URL("http://graph.facebook.com/shaverm/picture"));
		String photo = user.getPhoto(200, 200, "http://www.gravatar.com");
		assertEquals("http://graph.facebook.com/shaverm/picture?width=200&height=200", photo);
	}

	@Test
	public void should_use_width_and_height_parameters_when_photo_uri_is_from_google() throws Exception {
		User user = user("name", "paulo@paulo.com.br");
		user.setPhotoUri(new URL("https://lh3.googleusercontent.com/ksadajsdkasjd/photo.jpg?sz=50"));
		String photo = user.getPhoto(200, 200, "http://www.gravatar.com");
		assertEquals("https://lh3.googleusercontent.com/ksadajsdkasjd/photo.jpg?sz=200", photo);
	}

	@Test
	public void should_not_use_gravatar_when_user_have_a_photo()
			throws Exception {
		User user = user("name", "name@brutal.com");
		String uri = "http://foo.com/bar.png";
		user.setPhotoUri(new URL(uri));
		String photo = user.getPhoto(10, 10, "http://www.gravatar.com");

		assertEquals(uri + "?width=10&height=10", photo);
	}
	
	@Test
	public void should_not_show_upvote_banner() throws Exception {
		User user = TimeMachine.goTo(new DateTime().minusWeeks(1)).andExecute(new Block<User>() {
			@Override
			public User run() {
				return new User(SanitizedText.fromTrustedText("name"), "name@brutal.com");
			}
		});
		Thread.sleep(1);
		
		assertFalse(user.isVotingEnough());
		
		user.votedUp();
		
		assertTrue(user.isVotingEnough());
	}
}
