package org.mamute.observer;

import org.mamute.dao.UserDAO;
import org.mamute.dao.WatcherDAO;
import org.mamute.event.QuestionCreated;
import org.mamute.mail.NewQuestionMailer;
import org.mamute.mail.action.EmailAction;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.model.watch.Watcher;
import org.mamute.notification.NotificationMail;
import org.mamute.notification.NotificationMailer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

public class SubscribeUsersToNewQuestion {

	@Inject
	private UserDAO users;
	@Inject
	private WatcherDAO watchers;
	@Inject
	private NewQuestionMailer newQuestionMailer;

	public void subscribeUsers(@Observes QuestionCreated questionCreated) {
		Question question = questionCreated.getQuestion();
		List<User> subscribed = users.findUsersSubscribedToAllQuestions();
		for (User user : subscribed) {
			watchers.add(question, new Watcher(user));
		}
		newQuestionMailer.send(subscribed, question);

	}
}
