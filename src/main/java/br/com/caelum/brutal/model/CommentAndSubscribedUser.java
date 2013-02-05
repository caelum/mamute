package br.com.caelum.brutal.model;

public class CommentAndSubscribedUser {
    
    private final Comment comment;
    private final User user;
    
    public CommentAndSubscribedUser(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
    
    public Comment getComment() {
        return comment;
    }
    
    public User getUser() {
        return user;
    }

}
