package br.com.caelum.brutal.model;

public class SubscribableDTO {

    private final User user;
    private final Subscribable subscribable;
    private final Question question;

    public SubscribableDTO(Subscribable subscribable, User user, Question question) {
        this.user = user;
        this.subscribable = subscribable;
        this.question = question;
    }

    @Override
    public String toString() {
        return "AnswerAndSubscribedUser [user=" + user + ", answer=" + subscribable + "]\n";
    }
    
    public User getUser() {
        return user;
    }
    
    public Subscribable getSubscribable() {
        return subscribable;
    }
    
    public Question getQuestion() {
        return question;
    }

}
