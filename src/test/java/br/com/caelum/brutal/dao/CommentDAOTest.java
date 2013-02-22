package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;

public class CommentDAOTest extends DatabaseTestCase {

    @Test
    public void should_find_recent_comments_and_subscribed_users() {
        CommentDAO comments = new CommentDAO(session);
        
        User artur = new User("question author", "qauthor@gmail", "1234");
        User iFag = new User("question author", "otherqauthor@gmail", "1234");
        User valeriano = new User("answer author", "aauthor@gmail", "1234");
        User alcoolatra = new User("comment author", "cauthor@gmail", "1234");
        QuestionBuilder question = new QuestionBuilder();        

		Question beberFazMal = question
			.withTitle("Por que dizem que beber demais faz mal?")
			.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
			.withAuthor(artur).build();
		
        Answer figadoVaiProSaco = answer("Por que seu figado vai pro saco", beberFazMal, valeriano);
        
        Question androidRuim = question
				.withTitle("Por que a api de android é tão ruim?")
				.withDescription("Alguem poderia me dizer o por que disso? Obrigado galera!")
				.withAuthor(iFag).build();
        
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

}
