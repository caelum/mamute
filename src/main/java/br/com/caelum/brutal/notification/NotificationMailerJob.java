package br.com.caelum.brutal.notification;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.quartzjob.CronTask;

@Resource
public class NotificationMailerJob implements CronTask {
    
    private final AnswerDAO answers;
    private final CommentDAO comments;
    private final NotificationMailer notificationMailer;
	private final Environment env;
	private static final Logger LOG = Logger.getLogger(NotificationMailerJob.class);
	private final Result result;

    public NotificationMailerJob(AnswerDAO answers, CommentDAO comments, NotificationMailer notificationMailer, Environment env, Result result) {
        this.answers = answers;
        this.comments = comments;
        this.notificationMailer = notificationMailer;
		this.env = env;
		this.result = result;
    }

    @Override
    public void execute() {
    	LOG.info("executing NotificationMailerJob...");
        DateTime threeHoursAgo = new DateTime().minusHours(3);
        
        List<SubscribableDTO> recentSubscribables = answers.getSubscribablesAfter(threeHoursAgo);
        recentSubscribables.addAll(comments.getSubscribablesAfter(threeHoursAgo));
        
        notificationMailer.sendMails(recentSubscribables);
        result.nothing();
    }

    @Override
    public String frequency() {
        return env.get("mailer.frequency");
    }
    
}
