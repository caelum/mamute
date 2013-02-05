package br.com.caelum.brutal.components;

import br.com.caelum.vraptor.quartzjob.CronTask;

public class NotificationMailerJob implements CronTask {
    
    
    public NotificationMailerJob() {
    }

    @Override
    public void execute() {
    }

    @Override
    public String frequency() {
        return "* */03 * * * ?";
    }

}
