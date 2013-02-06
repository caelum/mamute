package br.com.caelum.brutal.model;

public class Updater {

	public UpdateStatus update(Answer answer, AnswerInformation information) {
        UpdateStatus status = information.getAuthor().canUpdate(answer);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        answer.enqueueChange(information, status);
		return status;
	}

	public UpdateStatus update(Question question, QuestionInformation information) {
        UpdateStatus status = information.getAuthor().canUpdate(question);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        question.enqueueChange(information, status);
		return status;
	}

}
