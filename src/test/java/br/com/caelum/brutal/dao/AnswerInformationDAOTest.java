package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.CurrentUser;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.QuestionInformationBuilder;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;

public class AnswerInformationDAOTest extends DatabaseTestCase {

    @Test
    public void should_find_pending_answers_edits() {
        AnswerInformationDAO answerInformations = new AnswerInformationDAO(session);
        
        User author = new User("namename", "email@gmail.com", "123456");
        CurrentUser currentAuthor = new CurrentUser(author, null);
        
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
        
        UpdatablesAndPendingHistory pendingByUpdatables = answerInformations.pendingByUpdatables();
        
        List<Information> pendingInfoForAnswer1 = pendingByUpdatables.pendingInfoFor(answer1);
        assertEquals(2, pendingInfoForAnswer1.size());
    }

}
