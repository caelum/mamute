package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;

public class CommentDAOTest extends DatabaseTestCase {

    @Test
    public void should_find_recent_comments_and_subscribed_users() {
        CommentDAO comments = new CommentDAO(session);
        
        User questionAuthor = new User("question author", "qauthor@gmail", "1234");
        User otherQuestionAuthor = new User("question author", "otherqauthor@gmail", "1234");
        User answerAuthor = new User("answer author", "aauthor@gmail", "1234");
        User commentAuthor = new User("comment author", "cauthor@gmail", "1234");
        
        Question question = question("question question question question", "description description description description description", questionAuthor);
        Answer answer = answer("blablablablablablablablablabla", question, answerAuthor);
        
        Question otherQuestion = question("question question question question", "description description description description description", otherQuestionAuthor);
        Answer otherAnswer = answer("blablablablablablablablablabla", otherQuestion, answerAuthor);
        
        Comment comment = new Comment(commentAuthor, "commentcommentcommentcommentcomment");
        question.add(comment);
        
        session.save(questionAuthor);
        session.save(answerAuthor);
        session.save(commentAuthor);
        session.save(otherQuestionAuthor);
        session.save(comment);
        session.save(question);
        session.save(answer);
        session.save(otherQuestion);
        session.save(otherAnswer);
        
        Long milisecAgo = (long) (3 * (60 * 60 * 1000));
        DateTime threeHoursAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        List<SubscribableDTO> recentComments = comments.getSubscribablesAfter(threeHoursAgo);
        
        assertEquals(2, recentComments.size());
        assertEquals(answerAuthor.getId(), recentComments.get(0).getUser().getId());
        assertEquals(comment.getId(), ((Comment) recentComments.get(0).getSubscribable()).getId());
        
        assertEquals(questionAuthor.getId(), recentComments.get(1).getUser().getId());
        assertEquals(comment.getId(), ((Comment) recentComments.get(1).getSubscribable()).getId());
        
        assertEquals(question.getId(), recentComments.get(0).getQuestion().getId());
        
        
        
        
    }

}
