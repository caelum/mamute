package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class Updater {

	public UpdateStatus update(Question original, QuestionInformation question) {
        UpdateStatus status = question.getAuthor().canUpdate(original);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        original.enqueueChange(question, status);
        return status;
	}

}
