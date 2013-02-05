package br.com.caelum.brutal.model;

public class AnswerAndSubscribedUser {

    private final User user;
    private final Answer answer;

    public AnswerAndSubscribedUser(Answer anwser, User user) {
        this.user = user;
        this.answer = anwser;
    }

    @Override
    public String toString() {
        return "AnswerAndSubscribedUser [user=" + user + ", answer=" + answer + "]\n";
    }
    
    public User getUser() {
        return user;
    }
    
    public Answer getAnswer() {
        return answer;
    }
}
