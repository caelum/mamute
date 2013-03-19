package br.com.caelum.brutal.model;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.builder.QuestionBuilder;
import br.com.caelum.brutal.dao.TestCase;

public class UserTest extends TestCase {
    
    private User author;
    private User moderator;
    private User otherUser;
    private QuestionBuilder question = new QuestionBuilder();
    private Question myQuestion;
	private Question myOtherQuestion;

    @Before
    public void before_test() {
        author = user("name", "email");
        author.setId(1l);
        otherUser = user("other", "email");
        otherUser.setId(2l);
        
        myQuestion = question.withTitle("question title").withDescription("question description").withAuthor(author).build();
        myOtherQuestion = question.withTitle("question title").withDescription("question description").withAuthor(otherUser).build();
        
        moderator = user("yeah", "email").asModerator();
        moderator.setId(3l);
    }

    @Test
    public void moderator_should_approve_question_information() throws Exception {
        Information approvedInfo = new QuestionInformation("edited title", "edited desc", 
                new LoggedUser(otherUser, null), new ArrayList<Tag>(), "comment");
        
        moderator.approve(myQuestion, approvedInfo);
        
        assertEquals(approvedInfo, myQuestion.getInformation());
        assertTrue(myQuestion.getInformation().isModerated());
    }
    
    @Test
    public void moderator_should_approve_answer_information() throws Exception {
        Answer answer = answer("answer description", myQuestion, author);
        AnswerInformation approvedInfo = answerInformation("new description", otherUser, answer);
        
        moderator.approve(answer, approvedInfo);
        
        assertEquals(approvedInfo, answer.getInformation());
        assertTrue(answer.getInformation().isModerated());
    }
    
    @Test
    public void should_verify_if_its_author_of_the_question(){
    	assertTrue(author.isAuthorOf(myQuestion));
    	assertFalse(author.isAuthorOf(myOtherQuestion));
    }
    
    @Test
	public void should_update_password_if_confirmation_matches() throws Exception {
    	User user = user("name", "name@brutal.com");
    	assertFalse(user.updateForgottenPassword("12345", "123456"));
    	assertTrue(user.updateForgottenPassword("123456", "123456"));
	}
    
    @Test
	public void should_avatar_when_null_photo() throws Exception {
    	User user = user("name", "name@brutal.com");
    	String photo = user.getPhoto(10, 10);
    	photo.startsWith("http://gravatar.com");
	}
    
    @Test
    public void should_not_use_gravatar_when_user_have_a_photo() throws Exception {
    	User user = user("name", "name@brutal.com");
    	String uri = "http://foo.com/bar.png";
		user.setPhotoUri(new URL(uri));
    	String photo =  user.getPhoto(10, 10);
    	
    	photo.equals(uri);
    }
}
