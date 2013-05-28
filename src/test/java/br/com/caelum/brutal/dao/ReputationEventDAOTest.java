package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.model.EventType.ANSWER_UPVOTE;
import static br.com.caelum.brutal.model.EventType.SOLVED_QUESTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dto.KarmaByQuestionHistory;
import br.com.caelum.brutal.dto.KarmaByQuestionHistory.KarmaAndQuestion;
import br.com.caelum.brutal.dto.UserSummaryForTag;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

public class ReputationEventDAOTest extends DatabaseTestCase {

	private User author;
	private ReputationEventDAO reputationEvents;
	private Tag tag;
	private Question questionInvolved1;
	private Question questionInvolved2;
	private Question questionInvolved3;

	@Before
	public void setup() {
		reputationEvents = new ReputationEventDAO(session);
		author = user("Brutal User", "brutal@brutal.com");
		tag = tag("java");
		questionInvolved1 = question(author, tag);
		questionInvolved2 = question(author, tag);
		questionInvolved3 = question(author, tag);
		session.save(author);
		session.save(tag);
		session.save(questionInvolved1);
		session.save(questionInvolved2);
		session.save(questionInvolved3);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void should_delete_event() {
		ReputationEvent toDelete = new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved1, author);
		ReputationEvent other = new ReputationEvent(EventType.ANSWER_UPVOTE, questionInvolved1, author);
		session.save(toDelete);
		session.save(other);
		
		reputationEvents.delete(toDelete);
		List list = session.createCriteria(ReputationEvent.class).list();
		assertEquals(1, list.size());
		assertTrue(list.contains(other));
	}
	
	@Test
	public void should_group_reputation_karma_reward_by_post() throws Exception {
		ReputationEvent event1Question1 = event30MinAgo(EventType.ANSWER_DOWNVOTE);
		ReputationEvent event2Question1 = event30MinAgo(EventType.QUESTION_DOWNVOTE);
		Integer question1Karma = event1Question1.getKarmaReward() + event2Question1.getKarmaReward();
		
		ReputationEvent event1Question2 = new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved2, author);
		ReputationEvent event2Question2 = new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved2, author);
		Integer question2Karma = event1Question2.getKarmaReward() + event2Question2.getKarmaReward();
		
		ReputationEvent eventOut = TimeMachine.goTo(new DateTime().minusHours(2)).andExecute(new Block<ReputationEvent>() {
			@Override
			public ReputationEvent run() {
				return new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved3, author);
			}
		});
		session.save(eventOut);
		session.save(event1Question1);
		session.save(event1Question2);
		session.save(event2Question1);
		session.save(event2Question2);
		
		KarmaByQuestionHistory karmaByQuestion = reputationEvents.karmaWonByQuestion(author, new DateTime().minusHours(1));
		List<KarmaAndQuestion> history = karmaByQuestion.getHistory();
		assertEquals(2, history.size());
		
		assertEquals(questionInvolved1, history.get(1).getQuestion());
		assertEquals(questionInvolved2, history.get(0).getQuestion());
		assertEquals(question1Karma.longValue(), history.get(1).getKarma().longValue());
		assertEquals(question2Karma.longValue(), history.get(0).getKarma().longValue());
	}

	private ReputationEvent event30MinAgo(final EventType type) {
		return TimeMachine.goTo(new DateTime().minusMinutes(30)).andExecute(new Block<ReputationEvent>() {
			@Override
			public ReputationEvent run() {
				return new ReputationEvent(type, questionInvolved1, author);
			}
		});
	}
	
	@Test
	public void should_group_reputation_karma_reward_by_date() throws Exception {
		ReputationEvent event1 = TimeMachine.goTo(new DateTime().minusDays(1)).andExecute(new Block<ReputationEvent>() {
			@Override
			public ReputationEvent run() {
				return new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved1, author);
			}
		});
		ReputationEvent event2 = new ReputationEvent(EventType.ANSWER_DOWNVOTE, questionInvolved1, author);
		
		session.save(event2);
		session.save(event1);
		
		KarmaByQuestionHistory karmaByQuestion = reputationEvents.karmaWonByQuestion(author, new DateTime().minusDays(2));
		List<KarmaAndQuestion> history = karmaByQuestion.getHistory();
		
		assertEquals(2, history.size());
		assertEquals(questionInvolved1, history.get(1).getQuestion());
		assertEquals(event1.getKarmaReward().longValue(), history.get(1).getKarma().longValue());
		assertEquals(questionInvolved1, history.get(0).getQuestion());
		assertEquals(event2.getKarmaReward().longValue(), history.get(0).getKarma().longValue());
	}
	
	@Test
	public void should_return_tag_summary_for_user() {
		User solutionAuthor = user("solutionAuthor", "solution@x.com");
		User otherAnswerAuthor = user("answerAuthor", "other@x.com");
		Tag tag = tag("teste");
		Question question1 = question(author, tag);
		Answer answer1 = answer("BLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLA", question1, solutionAuthor);
		Answer answer2 = answer("BLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLA", question1, otherAnswerAuthor);
		
		Question question2 = question(author, tag);
		Answer answer3 = answer("BLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLA", question2, solutionAuthor);
		ReputationEvent answer3Upvote = new ReputationEvent(EventType.ANSWER_UPVOTE, question2, solutionAuthor);
		
		for(int i = 0; i < 30; i++){
			ReputationEvent questionUpvote = new ReputationEvent(EventType.QUESTION_UPVOTE, question1, author);
			session.save(questionUpvote);
		}
		
		ReputationEvent answerIsSolution = new ReputationEvent(EventType.SOLVED_QUESTION, question1, solutionAuthor);
		ReputationEvent solutionUpvote = new ReputationEvent(EventType.ANSWER_UPVOTE, question1, solutionAuthor);
		long karmaRewardForSolution = SOLVED_QUESTION.reward().longValue() + ANSWER_UPVOTE.reward().longValue()*2;
		
		ReputationEvent answer2Upvote = new ReputationEvent(EventType.ANSWER_UPVOTE, question1, otherAnswerAuthor);
		ReputationEvent answer2Upvote2 = new ReputationEvent(EventType.ANSWER_UPVOTE, question1, otherAnswerAuthor);
		long karmaRewardForOtherAnswer = ANSWER_UPVOTE.reward().longValue()*2;
		
		session.save(tag);
		session.save(question1);
		session.save(question2);
		
		session.save(otherAnswerAuthor);
		session.save(solutionAuthor);
		
		session.save(answer1);
		session.save(answer2);
		session.save(answer3);
		
		session.save(answerIsSolution);
		session.save(solutionUpvote);
		session.save(answer2Upvote);
		session.save(answer2Upvote2);
		session.save(answer3Upvote);
		
		
		List<UserSummaryForTag> summaryForTag = reputationEvents.getTopAnswerersSummaryAllTime(tag);
		
		assertEquals(2, summaryForTag.size());
		assertEquals(karmaRewardForSolution, summaryForTag.get(0).getKarmaReward().longValue());
		assertEquals(solutionAuthor, summaryForTag.get(0).getUser());
		assertEquals(2l, summaryForTag.get(0).getCount().longValue());
		assertEquals(karmaRewardForOtherAnswer, summaryForTag.get(1).getKarmaReward().longValue());
		assertEquals(otherAnswerAuthor, summaryForTag.get(1).getUser());
		assertEquals(1l, summaryForTag.get(1).getCount().longValue());
	}

}
