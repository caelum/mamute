package br.com.caelum.brutal.dao;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;

public class AnswerDAOTest extends DatabaseTestCase {

    private User questionAuthor = user("guilherme", "email@email.com");
    private User answerAuthor1 = user("leonardo", "answer2@email.com");
    private User answerAuthor2 = user("francisco", "answer1@email.com");
    private User answerAuthor3 = user("felipe", "answer3@email.com");
    private QuestionBuilder question = new QuestionBuilder();
    private Tag defaultTag = tag("defaultTag");
    private Question creativeQuestion = question.withTag(defaultTag).withTitle("title title title title").withDescription("description descriptions descriptions descriptions descriptions").withAuthor(questionAuthor).build();
    AnswerDAO answers;
    
    @Before
    public void before_test() {
    	answers = new AnswerDAO(session);
        session.save(questionAuthor);
        session.save(answerAuthor2);
        session.save(answerAuthor3);
        session.save(answerAuthor1);
        session.save(defaultTag);
        session.save(creativeQuestion);
    }

    @Test
    public void should_find_recent_answers() {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusHours(4).getMillis());
        Answer oldAnswer1 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor1);
        Answer oldAnswer2 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor1);
        
        DateTimeUtils.setCurrentMillisSystem();
        Answer newAnswer1 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor2);
        Answer newAnswer2 = answer("answer answer answer answer answer answer", creativeQuestion, answerAuthor3);
        
        session.save(oldAnswer1);
        session.save(oldAnswer2);
        session.save(newAnswer1);
        session.save(newAnswer2);
        
        Long milisecAgo = (long) (3 * (60 * 60 * 1000));
        DateTime threeHoursAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        List<SubscribableDTO> recentAnswers = answers.getSubscribablesAfter(threeHoursAgo);
        
        assertEquals(6, recentAnswers.size());
        assertEquals(creativeQuestion.getId(), recentAnswers.get(0).getQuestion().getId());
    }
    
    @Test @Deprecated
    public void should_not_find_unsubscribed_and_find_subscribed_users_for_answers() {
        User artur = user("question author", "artur@gmail.com");
        artur.setSubscribed(false);
        
        User valeriano = user("answer author", "valeriano@gmail.com");
        valeriano.setSubscribed(false);
        
        User iFag = user("question author", "ifag@gmail.com");
        User alcoolatra = user("comment author", "alcoolatra@gmail.com");
        
        User leo = user("Leonardo", "leo@gmail.com");
        User chico = user("Chico", "chico@gmail.com");
        
        session.save(artur);
        session.save(valeriano);
        session.save(alcoolatra);
        session.save(iFag);
        session.save(leo);
        session.save(chico);
        
        Tag tagDefault = tag("java");
        session.save(tagDefault);
        
        //new answer, but question and first answer author should not be notified
		Question beberFazMal = question(artur, tagDefault);
        Answer figadoVaiProSaco = answer("Por que seu figado vai pro saco", beberFazMal, valeriano);
        Answer figadoVaiProCeu = answer("Por que seu figado vai para o ceu", beberFazMal, leo);
        session.save(beberFazMal);
        session.save(figadoVaiProSaco);
        session.save(figadoVaiProCeu);
        
        //new answer, question author must be notified
        Question androidRuim = question(iFag, tagDefault);
        Answer naoEhRuby = answer("Por que não é ruby, ai não é bacana.", androidRuim, chico);
        Answer ehAndroid = answer("Sabe por que? Porque é android manolo", androidRuim, artur);
        session.save(androidRuim);
        session.save(naoEhRuby);
        session.save(ehAndroid);
        
        List<User> users = new ArrayList<>();
        List<SubscribableDTO> recentComments = answers.getSubscribablesAfter(new DateTime().minusHours(3));
        for (SubscribableDTO subscribableDTO : recentComments) {
			users.add(subscribableDTO.getUser());
		}
        assertThat(users, not(hasItem(artur)));
        assertThat(users, not(hasItem(valeriano)));
        assertThat(users,hasItems(iFag, chico));
    }
    
}
