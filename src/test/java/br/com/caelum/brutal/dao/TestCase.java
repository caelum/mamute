package br.com.caelum.brutal.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.CurrentUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public abstract class TestCase {

	/**
	 * This constructor should not exist. It is only for tests
	 */
	public Question question(String title,String description, User author) {
		Question q = new Question(new QuestionInformation(title, description, new CurrentUser(author, null), new ArrayList<Tag>(), ""), author);
		return q;
	}


	/**
	 * This constructor should not exist. It is only for tests
	 */
	protected Answer answer(String description, Question question, User author) {
		Answer q = new Answer(new AnswerInformation(description, new CurrentUser(author, null), ""), question, author);
		return q;
	}
	
	/**
	 * This constructor should not exist. It is only for tests
	 */
	protected User user(String name, String email, Long id) {
	    User user = new User(name, email, "123456");
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
	    return user;
	}
    
}

