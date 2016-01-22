package org.mamute.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.EventType.ANSWER_UPVOTE;
import static org.mamute.model.EventType.SOLVED_QUESTION;

import java.util.List;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dto.KarmaAndContext;
import org.mamute.dto.KarmaByContextHistory;
import org.mamute.dto.UserSummaryForTag;
import org.mamute.model.Answer;
import org.mamute.model.EventType;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.ReputationEvent;
import org.mamute.model.Tag;
import org.mamute.model.User;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ReputationEventDAOTest extends DatabaseTestCase {

	private User author;
	private ReputationEventDAO reputationEvents;
	private Tag tag;
	private Question questionInvolved1;
	private Question questionInvolved2;
	private Question questionInvolved3;

	@Before
	public void setup() {
		reputationEvents = new ReputationEventDAO(session, new InvisibleForUsersRule(new LoggedUser(author, null)));
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
		
		KarmaByContextHistory karmaByQuestion = reputationEvents.karmaWonByQuestion(author, new DateTime().minusHours(1));
		List<KarmaAndContext> history = karmaByQuestion.getHistory();
		assertEquals(2, history.size());

		KarmaAndContext ck1 = Iterables.find(history, contextOf(questionInvolved1), null);
		assertNotNull(ck1);
		KarmaAndContext ck2 = Iterables.find(history, contextOf(questionInvolved2), null);
		assertNotNull(ck2);

		assertEquals(question1Karma.longValue(), ck1.getKarma().longValue());
		assertEquals(question2Karma.longValue(), ck2.getKarma().longValue());
	}

	private Predicate<KarmaAndContext> contextOf(final Question question) {
		return new Predicate<KarmaAndContext>() {
			@Override
			public boolean apply(@Nullable KarmaAndContext input) {
				return input.getContext().equals(question);
			}
		};
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
		
		KarmaByContextHistory karmaByQuestion = reputationEvents.karmaWonByQuestion(author, new DateTime().minusDays(2));
		List<KarmaAndContext> history = karmaByQuestion.getHistory();
		
		assertEquals(2, history.size());
		assertEquals(questionInvolved1, history.get(1).getContext());
		assertEquals(event1.getKarmaReward().longValue(), history.get(1).getKarma().longValue());
		assertEquals(questionInvolved1, history.get(0).getContext());
		assertEquals(event2.getKarmaReward().longValue(), history.get(0).getKarma().longValue());
	}
	
	@Test
	public void should_return_tag_answerer_summary() {
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
	
	@Test
	public void should_return_tag_asker_summary() {
		User question1Author = user("solutionAuthor", "solution@x.com");
		User question3Author = user("answerAuthor", "other@x.com");
		session.save(question3Author);
		session.save(question1Author);
		
		Tag tag = tag("teste");
		session.save(tag);
		
		Question question1 = question(question1Author, tag);
		Question question2 = question(question1Author, tag);
		Question question3 = question(question3Author, tag);
		
		session.save(question1);
		session.save(question2);
		session.save(question3);
		
		for(int i = 0; i < 30; i++){
			session.save(new ReputationEvent(EventType.QUESTION_UPVOTE, question1, question1Author));
		}
		session.save(new ReputationEvent(EventType.QUESTION_UPVOTE, question2, question1Author));
		
		long karmaRewardForQuestion1 = EventType.QUESTION_UPVOTE.reward().longValue() * 31;
		
		for(int i = 0; i < 10; i++){
			session.save(new ReputationEvent(EventType.QUESTION_UPVOTE, question3, question3Author));
		}
		
		long karmaRewardForQuestion2 = EventType.QUESTION_UPVOTE.reward().longValue() * 10;
		
		List<UserSummaryForTag> summaryForTag = reputationEvents.getTopAskersSummaryAllTime(tag);
		
		assertEquals(2, summaryForTag.size());
		assertEquals(karmaRewardForQuestion1, summaryForTag.get(0).getKarmaReward().longValue());
		assertEquals(question1Author, summaryForTag.get(0).getUser());
		assertEquals(2l, summaryForTag.get(0).getCount().longValue());
		assertEquals(karmaRewardForQuestion2, summaryForTag.get(1).getKarmaReward().longValue());
		assertEquals(question3Author, summaryForTag.get(1).getUser());
		assertEquals(1l, summaryForTag.get(1).getCount().longValue());
	}

	@Test
	public void should_ignore_events_of_deleted_questions() {
		session.save(new ReputationEvent(EventType.QUESTION_UPVOTE, questionInvolved1, author));

		session.delete(questionInvolved1);

		KarmaByContextHistory karmaByQuestion = reputationEvents.karmaWonByQuestion(author, new DateTime(0));

		assertNotNull(karmaByQuestion);
		assertNotNull(karmaByQuestion.getHistory());
		assertTrue(karmaByQuestion.getHistory().isEmpty());
	}
}
