package br.com.caelum.brutal.dao;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;

/**
 * Constructor from this class should not be used anywhere beside tests.
 */
public abstract class TestCase {

	protected Answer answer(String description, Question question, User author) {
		Answer q = new Answer(new AnswerInformation(description, new LoggedUser(author, null), ""), question, author);
		return q;
	}
	
	protected User user(String name, String email) {
	    User user = new User(name, email, "123456");
	    user.confirmEmail();
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
    
    private void setId(Object o, Long id) {
        new Mirror().on(o).set().field("id").withValue(id);
    }


}

