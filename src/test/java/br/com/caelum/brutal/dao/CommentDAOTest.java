package br.com.caelum.brutal.dao;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class CommentDAOTest extends DatabaseTestCase {
	
	private CommentDAO comments;

	@Before
	public void setup() {
		comments = new CommentDAO(session);
	}

    @Test
    public void should_find_recent_comments_and_subscribed_users() {
        User artur = user("question author", "qauthor@gmail");
        User iFag = user("question author", "otherqauthor@gmail");
        User valeriano = user("answer author", "aauthor@gmail");
        User alcoolatra = user("comment author", "cauthor@gmail");
        Tag tagDefault = tag("default");
        session.save(tagDefault);
        
        Question beberFazMal = question(artur, Arrays.asList(tagDefault));
		
        Answer figadoVaiProSaco = answer("Por que seu figado vai pro saco", beberFazMal, valeriano);
        
        Question androidRuim = question(iFag, Arrays.asList(tagDefault));
        
        Answer naoEhRuby = answer("Por que não é ruby, ai não é bacana.", androidRuim, valeriano);
        
        Comment naoFazMal = new Comment(alcoolatra, "O que te levou a pensar que faz mal? Faz nada não.");
        beberFazMal.add(naoFazMal);
        
        session.save(artur);
        session.save(valeriano);
        session.save(alcoolatra);
        session.save(iFag);
        session.save(naoFazMal);
        session.save(beberFazMal);
        session.save(figadoVaiProSaco);
        session.save(androidRuim);
        session.save(naoEhRuby);
        
        List<SubscribableDTO> recentComments = comments.getSubscribablesAfter(new DateTime().minusHours(3));
        
        assertEquals(2, recentComments.size());
        assertEquals(valeriano.getId(), recentComments.get(0).getUser().getId());
        assertEquals(naoFazMal.getId(), ((Comment) recentComments.get(0).getSubscribable()).getId());
        
        assertEquals(artur.getId(), recentComments.get(1).getUser().getId());
        assertEquals(naoFazMal.getId(), ((Comment) recentComments.get(1).getSubscribable()).getId());
        
        assertEquals(beberFazMal.getId(), recentComments.get(0).getQuestion().getId());
    }

    @Test
    public void should_not_find_unsubscribed_and_find_subscribed_users_for_comments() {
        User artur = user("question author", "artur@gmail.com");
        artur.setSubscribed(false);
        
        User valeriano = user("answer author", "valeriano@gmail.com");
        valeriano.setSubscribed(false);
        
        User iFag = user("question author", "ifag@gmail.com");
        User alcoolatra = user("comment author", "alcoolatra@gmail.com");
        
        User leo = user("question author", "leo@gmail.com");
        User chico = user("answer author", "chico@gmail.com");
        
        session.save(artur);
        session.save(valeriano);
        session.save(alcoolatra);
        session.save(iFag);
        session.save(leo);
        session.save(chico);
        
        Tag tagDefault = tag("java");
        session.save(tagDefault);
        
        //comment added to question but question and answer author should not be notified
		Question beberFazMal = question(artur, Arrays.asList(tagDefault));
        Answer figadoVaiProSaco = answer("Por que seu figado vai pro saco", beberFazMal, valeriano);
        Comment naoFazMal = new Comment(alcoolatra, "O que te levou a pensar que faz mal? Faz nada não.");
        beberFazMal.add(naoFazMal);
        session.save(naoFazMal);
        session.save(beberFazMal);
        session.save(figadoVaiProSaco);
        
        //comment added to answer (question and answer author must be notified)
        Question androidRuim = question(iFag, Arrays.asList(tagDefault));
        Answer naoEhRuby = answer("Por que não é ruby, ai não é bacana.", androidRuim, alcoolatra);
        Comment naoEhNao = comment(valeriano, "nao eh ruim nao");
        naoEhRuby.add(naoEhNao);
        session.save(naoEhNao);
        session.save(androidRuim);
        session.save(naoEhRuby);
        
        //comment added to question (question and answer authors must be notified)
        Question comoFaz = question(leo, Arrays.asList(tagDefault));
        Answer fazAssim = answer("faz assim faz assim faz assim faz assim faz assim", comoFaz, chico);
        Comment fazerOQue = comment(valeriano, "quer fazer o que?");
        comoFaz.add(fazerOQue);
        session.save(fazerOQue);
        session.save(comoFaz);
        session.save(fazAssim);
        
        List<User> users = new ArrayList<>();
        List<SubscribableDTO> recentComments = comments.getSubscribablesAfter(new DateTime().minusHours(3));
        for (SubscribableDTO subscribableDTO : recentComments) {
			users.add(subscribableDTO.getUser());
		}
        assertThat(users, not(hasItem(artur)));
        assertThat(users, not(hasItem(valeriano)));
        assertThat(users, hasItems(alcoolatra, iFag, leo, chico));
    }
    
    @Test
    public void should_not_return_dto_with_user_equals_comment_author() {
    	User answerAuthor = user("user","user@x.com");	
    	User questionAuthor = user("other", "other@x.com");
    	
    	Tag tag = tag("teste");
    	Question question = question(questionAuthor, Arrays.asList(tag));
    	
    	Answer answer = answer("descriptiondescriptiondescriptiondescriptiondescriptiondescription", question, answerAuthor);
    	Comment answerComment = comment(answerAuthor, "comentariocomentariocomentariocomentariocomentario");
    	
    	question.add(answerComment);
    	
    	session.save(questionAuthor);
    	session.save(answerAuthor);
    	session.save(answerComment);
    	session.save(tag);
    	session.save(question);
    	session.save(answer);
    	
    	List<SubscribableDTO> recentComments = comments.getSubscribablesAfter(new DateTime().minusHours(3));
    	List<User> users = new ArrayList<>();
    	for (SubscribableDTO subscribableDTO : recentComments) {
    		User user = subscribableDTO.getUser();
			users.add(user);
			System.out.println(user);
		}
    	
    	assertThat(users, not(hasItem(answerAuthor)));
    	
	}

}
