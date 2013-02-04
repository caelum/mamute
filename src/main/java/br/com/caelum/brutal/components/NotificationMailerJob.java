package br.com.caelum.brutal.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.caelum.brutal.dao.NotifiableDAO;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Notifiable;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.quartzjob.CronTask;

public class NotificationMailerJob implements CronTask {
    
    final private NotifiableDAO<Notifiable> notifiables;
    
    public NotificationMailerJob(NotifiableDAO<Notifiable> notifiables) {
        this.notifiables = notifiables;
    }

    @Override
    public void execute() {
        List<Notifiable> recentNotifications = notifiables.recent(3, Comment.class);
        recentNotifications.addAll(notifiables.recent(3, Answer.class));
        
        Map<String, List<Notifiable>> notifiablesByEmail = notifiablesByEmail(recentNotifications);
    }

    public Map<String, List<Notifiable>> notifiablesByEmail(List<Notifiable> recentNotifications) {
        Map<String, List<Notifiable>> notifiablesByEmail = new HashMap<>();
        for (Notifiable notifiable : recentNotifications) {
            Set<User> subscribedUsers = notifiable.subscribed();
            for (User user : subscribedUsers) {
                List<Notifiable> userNotifiables = notifiablesByEmail.get(user.getEmail());
                if (userNotifiables == null) {
                    userNotifiables = new ArrayList<>(); 
                }
                userNotifiables.add(notifiable);
                notifiablesByEmail.put(user.getEmail(), userNotifiables);
            }
        }
        return notifiablesByEmail;
    }

    @Override
    public String frequency() {
        return "* */03 * * * ?";
    }

}
