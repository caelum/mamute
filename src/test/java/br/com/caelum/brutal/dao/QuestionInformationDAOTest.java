package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionInformationBuilder;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;

public class QuestionInformationDAOTest extends DatabaseTestCase {

    @Test
    public void should_get_pending_history() {
        QuestionInformationDAO questionInformations = new QuestionInformationDAO(session);
        User author = new User("chico", "chico@chico", "12345");
        
        QuestionInformation info1 = new QuestionInformationBuilder().withAuthor(author).build();
        Question question1 = new Question(info1, author);
        
        QuestionInformation info2 = new QuestionInformationBuilder().withAuthor(author).build();
        Question question2 = new Question(info2, author);
        QuestionInformation pendingInfo1 = new QuestionInformationBuilder().withAuthor(author).build();
        QuestionInformation pendingInfo2 = new QuestionInformationBuilder().withAuthor(author).build();
        question2.enqueueChange(pendingInfo1, UpdateStatus.PENDING);
        question2.enqueueChange(pendingInfo2, UpdateStatus.PENDING);
        
        QuestionInformation info3 = new QuestionInformationBuilder().withAuthor(author).build();
        Question question3 = new Question(info3, author);
        QuestionInformation pendingInfo3 = new QuestionInformationBuilder().withAuthor(author).build();
        QuestionInformation pendingInfo4 = new QuestionInformationBuilder().withAuthor(author).build();
        QuestionInformation pendingInfo5 = new QuestionInformationBuilder().withAuthor(author).build();
        question3.enqueueChange(pendingInfo3, UpdateStatus.PENDING);
        question3.enqueueChange(pendingInfo4, UpdateStatus.PENDING);
        question3.enqueueChange(pendingInfo5, UpdateStatus.PENDING);
        
        session.save(author);
        session.save(question1);
        session.save(question2);
        session.save(question3);
        
        QuestionAndPendingHistory pending = questionInformations.pending();
        List<Question> questions = pending.questions();
        
        assertEquals(questions.size(), 2);
        assertEquals(question2.getId(), questions.get(0).getId());
        
        List<QuestionInformation> pendingQuestion2 = pending.pendingInfoFor(questions.get(0));
        assertEquals(2, pendingQuestion2.size());
        
        List<QuestionInformation> pendingQuestion3 = pending.pendingInfoFor(questions.get(1));
        assertEquals(3, pendingQuestion3.size());
    }

}
