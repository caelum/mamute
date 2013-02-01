package br.com.caelum.brutal.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.brutal.integracao.dao.DatabaseTestCase;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.brutal.model.VoteType;
 
public class AnswerDAOTest extends DatabaseTestCase {

    @Test
    public void should_verify_that_a_user_already_voted_a_answer() {
        AnswerDAO answers = new AnswerDAO(session, new QuestionDAO(session), new UserDAO(session));
        
        Question question = new Question("Tiny title Tiny title Tiny title", "Description 1234567890123456789012345678901234567890");
        User author = new User("nome", "email", "123");
        User otherUser = new User("blabla", "blabla@gmail", "123");
        Answer answer = new Answer("hola q tal? hola q tal? hola q tal? hola q tal?", question, author);
        Vote vote = new Vote(author, VoteType.UP);
        answer.addVote(vote);
        
        session.save(author);
        session.save(otherUser);
        session.save(question);
        session.save(answer);
        session.save(vote);
        
        boolean alreadyVoted = answers.alreadyVoted(answer.getId(), author, VoteType.UP);
        boolean haventVotedDown = answers.alreadyVoted(answer.getId(), author, VoteType.DOWN);
        boolean haventVoted = answers.alreadyVoted(answer.getId(), otherUser, VoteType.UP);
        
        assertTrue(alreadyVoted);
        assertFalse(haventVotedDown);
        assertFalse(haventVoted);
    }

}
