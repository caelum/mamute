package org.mamute.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.auth.rules.PermissionRulesConstants;
import org.mamute.builder.QuestionBuilder;
import org.mamute.dao.TestCase;
import org.mamute.model.Answer;
import org.mamute.model.AnswerInformation;
import org.mamute.model.Information;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.Tag;
import org.mamute.model.User;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

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
		moderatorWannabe.setKarma(PermissionRulesConstants.MODERATE_EDITS);
		
		moderator = user("yeah", "email").asModerator();
		moderator.setId(3l);
	}

	@Test
	public void moderator_should_approve_question_information()
			throws Exception {
		Information approvedInfo = new QuestionInformation("edited title",
				"edited desc", new LoggedUser(otherUser, null),
				new ArrayList<Tag>(), "comment");

		moderator.approve(myQuestion, approvedInfo);

		assertEquals(approvedInfo, myQuestion.getInformation());
		assertTrue(myQuestion.getInformation().isModerated());
	}
	
	@Test
	public void user_with_enough_karma_should_approve_question_information()
			throws Exception {
		Information approvedInfo = new QuestionInformation("edited title",
				"edited desc", new LoggedUser(otherUser, null),
				new ArrayList<Tag>(), "comment");
		
		moderatorWannabe.approve(myQuestion, approvedInfo);
		
		assertEquals(approvedInfo, myQuestion.getInformation());
		assertTrue(myQuestion.getInformation().isModerated());
	}

	@Test
	public void moderator_should_approve_answer_information() throws Exception {
		Answer answer = answer("answer description", myQuestion, author);
		AnswerInformation approvedInfo = answerInformation("new description",
				otherUser, answer);

		moderator.approve(answer, approvedInfo);

		assertEquals(approvedInfo, answer.getInformation());
		assertTrue(answer.getInformation().isModerated());
	}
	
	@Test
	public void user_with_enough_karma_should_approve_answer_information() throws Exception {
		Answer answer = answer("answer description", myQuestion, author);
		AnswerInformation approvedInfo = answerInformation("new description",
				otherUser, answer);
		
		moderatorWannabe.approve(answer, approvedInfo);
		
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
		String photo = user.getPhoto(10, 10);
		assertTrue(photo.startsWith("http://www.gravatar.com"));
	}
	
	@Test
	public void should_have_PG_gravatar_with_robotar_when_null_photo() throws Exception {
		User user = user("name", "paulo@paulo.com.br");
		String photo = user.getPhoto(64, 64);
		assertEquals("http://www.gravatar.com/avatar/620ad6ac2c42fce964bbf2e01e87c04b.png?r=PG&size=64x64&d=http%3A%2F%2Frobohash.org%2Fsize_64x64%2Fset_set1%2Fbgset_any%2F620ad6ac2c42fce964bbf2e01e87c04b.png", photo);
	}
	
	@Test
	public void should_use_width_and_height_parameters_when_photo_uri_is_not_null() throws Exception {
		User user = user("name", "paulo@paulo.com.br");
		user.setPhotoUri(new URL("http://graph.facebook.com/shaverm/picture"));
		String photo = user.getPhoto(200, 200);
		assertEquals("http://graph.facebook.com/shaverm/picture?width=200&height=200", photo);
	}


	@Test
	public void should_not_use_gravatar_when_user_have_a_photo()
			throws Exception {
		User user = user("name", "name@brutal.com");
		String uri = "http://foo.com/bar.png";
		user.setPhotoUri(new URL(uri));
		String photo = user.getPhoto(10, 10);

		assertEquals(uri + "?width=10&height=10", photo);
	}
	
	@Test
	public void should_not_show_upvote_banner() throws Exception {
		User user = TimeMachine.goTo(new DateTime().minusWeeks(1)).andExecute(new Block<User>() {
			@Override
			public User run() {
				return new User("name", "name@brutal.com");
			}
		});
		Thread.sleep(1);
		
		assertFalse(user.isVotingEnough());
		
		user.votedUp();
		
		assertTrue(user.isVotingEnough());
	}
}
