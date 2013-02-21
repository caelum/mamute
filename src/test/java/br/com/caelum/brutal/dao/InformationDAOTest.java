package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionInformationBuilder;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;

public class InformationDAOTest extends DatabaseTestCase {

    @Test
    public void should_find_pending_answers_edits() {
        InformationDAO informations = new InformationDAO(session);
        
        User author = new User("namename", "email@gmail.com", "123456");
        LoggedUser currentAuthor = new LoggedUser(author, null);
        
        QuestionInformation qinfo1 = new QuestionInformationBuilder().with(author).build();
        Question question = new Question(qinfo1, author);
        AnswerInformation info1 = new AnswerInformation("info1 info1 info1 info1 info1 info1 info1 ", currentAuthor, "");
        Answer answer1 = new Answer(info1, question, author);
        
        AnswerInformation edit1 = new AnswerInformation("info2 info2 info2 info2 info2 info2 info2 ", currentAuthor, answer1, "");
        edit1.setInitStatus(UpdateStatus.PENDING);
        AnswerInformation edit2 = new AnswerInformation("info3 info3 info3 info3 info3 info3 info3 ", currentAuthor, answer1, "");
        edit2.setInitStatus(UpdateStatus.PENDING);
        
        session.save(author);
        session.save(question);
        session.save(qinfo1);
        session.save(info1);
        session.save(answer1);
        session.save(edit1);
        session.save(edit2);
        
        UpdatablesAndPendingHistory pendingByUpdatables = informations.pendingByUpdatables(Answer.class);
        
        List<Information> pendingInfoForAnswer1 = pendingByUpdatables.pendingInfoFor(answer1);
        assertEquals(2, pendingInfoForAnswer1.size());
    }

    @Test
    public void should_get_pending_history() {
        InformationDAO informations = new InformationDAO(session);
        User author = new User("francisco", "chico@chico", "123456");
        
        QuestionInformation info1 = new QuestionInformationBuilder().with(author).build();
        Question question1 = new Question(info1, author);
        
        QuestionInformation info2 = new QuestionInformationBuilder().with(author).build();
        Question question2 = new Question(info2, author);
        QuestionInformation pendingInfo1 = new QuestionInformationBuilder().with(author).build();
        QuestionInformation pendingInfo2 = new QuestionInformationBuilder().with(author).build();
        question2.enqueueChange(pendingInfo1, UpdateStatus.PENDING);
        question2.enqueueChange(pendingInfo2, UpdateStatus.PENDING);
        
        QuestionInformation info3 = new QuestionInformationBuilder().with(author).build();
        Question question3 = new Question(info3, author);
        QuestionInformation pendingInfo3 = new QuestionInformationBuilder().with(author).build();
        QuestionInformation pendingInfo4 = new QuestionInformationBuilder().with(author).build();
        QuestionInformation pendingInfo5 = new QuestionInformationBuilder().with(author).build();
        question3.enqueueChange(pendingInfo3, UpdateStatus.PENDING);
        question3.enqueueChange(pendingInfo4, UpdateStatus.PENDING);
        question3.enqueueChange(pendingInfo5, UpdateStatus.PENDING);
        
        session.save(author);
        session.save(question1);
        session.save(question2);
        session.save(question3);
        
        UpdatablesAndPendingHistory pending = informations.pendingByUpdatables(Question.class);
        List<Moderatable> questions = pending.moderatables();
        
        assertEquals(2, questions.size());
        assertEquals(question2.getId(), questions.get(0).getId());
        
        List<Information> pendingQuestion2 = pending.pendingInfoFor(questions.get(0));
        assertEquals(2, pendingQuestion2.size());
        
        List<Information> pendingQuestion3 = pending.pendingInfoFor(questions.get(1));
        assertEquals(3, pendingQuestion3.size());
    }
}
