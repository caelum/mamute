package br.com.caelum.brutal.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.joda.time.DateTimeUtils;
import org.junit.Before;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsInformation;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

/**
 * Constructor from this class should not be used anywhere beside tests.
 */
public abstract class TestCase {
	
	@Before
	public void fixTime() {
		DateTimeUtils.setCurrentMillisSystem();
	}

	private QuestionBuilder questionBuilder = new QuestionBuilder();
	
	protected Answer answer(String description, Question question, User author) {
		Answer q = new Answer(new AnswerInformation(description, new LoggedUser(author, null), "default commentdefault commentdefault commentdefault comment")
							, question, author);
		return q;
	}
	
	protected Question question(User author, Tag... tags){
		ArrayList<Tag> tagsList = new ArrayList<>(Arrays.asList(tags));
		if (tagsList.isEmpty())
			tagsList.add(tag("teste"));
		Question question = questionBuilder.withAuthor(author).withTags(tagsList).build();
		return question;
	}
	protected Question question(User author, List<Tag> tags){
		return question(author, tags.toArray(new Tag[tags.size()]));
	}
	
	protected User user(String name, String email) {
	    User user = new User(name, email);
	    user.confirmEmail();
	    return user;
	}
	
	protected LoggedUser loggedUser(String name, String email, Long id) {
		return new LoggedUser(user(name, email, id), null);
	}
	
	protected User userWithPassword(String name, String email) {
		User user = user(name, email);
		LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, "123456");
		user.add(brutalLogin);
		return user;
	}
	
	protected Vote vote(User author, VoteType type, Long id) {
	    Vote v = new Vote(author, type);
	    setId(v, id);
	    return v;
	}
	
	protected User user(String name, String email, Long id) {
	    User user = user(name, email);
	    setId(user, id);
	    return user;
	}
	
	protected Flag flag(FlagType flagType, User author) {
		return new Flag(flagType, author);
	}
	
    protected AnswerInformation answerInformation(String string, User otherUser, Answer answer) {
        return new AnswerInformation(string, new LoggedUser(otherUser, null), answer, "comment");
    }
    
    protected Comment comment(User author, String comment) {
    	return new Comment(author, comment);
    }
    
    protected void setId(Object o, Long id) {
        new Mirror().on(o).set().field("id").withValue(id);
    }

    protected Tag tag(String name){
    	return new Tag(name, "", null);
    }
    
    protected User moderator() {
    	return user("moderator", "moderator@brutal.com").asModerator();
    }
    
    protected News news(String title, String description, User author) {
    	NewsInformation newsInformation = new NewsInformation(title, description, new LoggedUser(author, null), "comment comment comment");
    	return new News(newsInformation, author);
	}
    
}

