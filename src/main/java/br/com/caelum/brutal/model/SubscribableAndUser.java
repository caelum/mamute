package br.com.caelum.brutal.model;

public class SubscribableAndUser {

    private final User user;
    private final Subscribable subscribable;

    public SubscribableAndUser(Subscribable subscribable, User user) {
        this.user = user;
        this.subscribable = subscribable;
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

}
