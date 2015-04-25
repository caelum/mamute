package org.mamute.filesystem;

import org.mamute.dao.AnswerDAO;
import org.mamute.dao.AttachmentDao;
import org.mamute.dao.QuestionDAO;
import org.mamute.infra.NotFoundException;
import org.mamute.model.Answer;
import org.mamute.model.Attachment;
import org.mamute.model.Question;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AttachmentRepository {

	@Inject
	private AttachmentDao attachments;
	@Inject
	private AttachmentsFileStorage fileStorage;
	@Inject
	private QuestionDAO questions;
	@Inject
	private AnswerDAO answers;

	public void save(Attachment attachment) {
		attachments.save(attachment);
		fileStorage.save(attachment);
	}

	public Attachment load(Long attachmentId) {
		Attachment load = attachments.load(attachmentId);
		if (load == null) {
			throw new NotFoundException();
		}
		return load;
	}

	public void delete(Attachment attachment) {
		attachments.delete(attachment);
		fileStorage.delete(attachment);
		detachFromQuestion(attachment);
		detachFromAnswer(attachment);
	}

	private void detachFromAnswer(Attachment attachment) {
		Answer answer = answers.answerWith(attachment);
		if (answer != null){
			answer.remove(attachment);
		}
	}

	private void detachFromQuestion(Attachment attachment) {
		Question question = questions.questionWith(attachment);
		if (question != null){
			question.remove(attachment);
		}
	}

	public InputStream open(Attachment attachment) {
		try {
			return fileStorage.open(attachment);
		} catch (FileNotFoundException e) {
			throw new NotFoundException(e);
		}
	}
}
