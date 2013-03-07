package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
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
    
    @Test
	public void should_get_comments_with_three_flags() throws Exception {
    	User author = user("author author", "author@brutal.com");
		Comment comment = comment(author, "comment comment comment comment");
		Comment other = comment(author, "comment comment comment comment");
		Flag flag1 = flag(FlagType.RUDE, author);
		Flag flag2 = flag(FlagType.RUDE, author);
		Flag flag3 = flag(FlagType.RUDE, author);
		Flag flag4 = flag(FlagType.RUDE, author);
		
		comment.add(flag1);
		comment.add(flag2);
		comment.add(flag3);
		other.add(flag4);
		
		session.save(author);
		session.save(flag1);
		session.save(flag2);
		session.save(flag3);
		session.save(flag4);
		session.save(comment);
		session.save(other);
		
		List<Comment> flagged = comments.flagged(3l);
		assertEquals(1, flagged.size());
		
	}

}
