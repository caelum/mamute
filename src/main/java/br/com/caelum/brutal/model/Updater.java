package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public class Updater {

	public UpdateStatus update(Answer answer, AnswerInformation information) {
	    UpdateStatus status = canUpdate(answer, information);
        
        if (status == UpdateStatus.REFUSED)
            return status;
        
        answer.enqueueChange(information, status);
		return status;
	}


	public UpdateStatus update(Question question, QuestionInformation information) {
	    UpdateStatus status = canUpdate(question, information);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        question.enqueueChange(information, status);
		return status;
	}

	private UpdateStatus canUpdate(Moderatable answer, Information newInformation) {
	    User informationAuthor = newInformation.getAuthor();
	    User author = answer.getAuthor();
	    if (author.getId().equals(informationAuthor.getId()) || informationAuthor.canModerate()) {
	        return UpdateStatus.NO_NEED_TO_APPROVE;
	    }
	    return UpdateStatus.PENDING;
	}
}
