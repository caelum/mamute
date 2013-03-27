package br.com.caelum.brutal.notification;

import java.util.List;

import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class NotificationMailerJob implements CronTask {
    
    private final AnswerDAO answers;
    private final CommentDAO comments;
    private final NotificationMailer notificationMailer;
	private final Environment env;

    public NotificationMailerJob(AnswerDAO answers, CommentDAO comments, NotificationMailer notificationMailer, Environment env) {
        this.answers = answers;
        this.comments = comments;
        this.notificationMailer = notificationMailer;
		this.env = env;
    }

    @Override
    public void execute() {
        DateTime threeHoursAgo = new DateTime().minusHours(3);
        
        List<SubscribableDTO> recentSubscribables = answers.getSubscribablesAfter(threeHoursAgo);
        recentSubscribables.addAll(comments.getSubscribablesAfter(threeHoursAgo));
        
        notificationMailer.sendMails(recentSubscribables);
    }

    @Override
    public String frequency() {
        return env.get("mailer.frequency");
    }
    
}
