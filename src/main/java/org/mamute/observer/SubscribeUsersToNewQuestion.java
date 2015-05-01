package org.mamute.observer;

import org.mamute.dao.UserDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.event.QuestionCreated;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.watch.Watcher;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

public class SubscribeUsersToNewQuestion {

	@Inject
	private UserDAO users;
	@Inject
	private WatcherDAO watchers;

	public void subscribeUsers(@Observes QuestionCreated questionCreated) {
		Question question = questionCreated.getQuestion();
		List<User> subscribed = users.findUsersSubscribedToAllQuestions();
		for (User user : subscribed) {
			watchers.add(question, new Watcher(user));
		}
	}
}
