package org.mamute.filesystem;

import org.mamute.dao.AnswerDAO;
import org.mamute.dao.AttachmentDao;
import org.mamute.dao.QuestionDAO;
import org.mamute.infra.NotFoundException;
import org.mamute.interfaces.IAttachmentStorage;
import org.mamute.model.Answer;
import org.mamute.model.Attachment;
import org.mamute.model.Question;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public class AttachmentRepository {

	private AttachmentDao attachments;
	private IAttachmentStorage fileStorage;
	private QuestionDAO questions;
	private AnswerDAO answers;

	@Deprecated
	public AttachmentRepository() {
	}

	@Inject
    public AttachmentRepository(AttachmentDao attachments, IAttachmentStorage fileStorage, QuestionDAO questions, AnswerDAO answers) {
        this.attachments = attachments;
		this.fileStorage = fileStorage;
		this.questions = questions;
		this.answers = answers;
    }

	public void save(Attachment attachment) {
		// order of statements here is very important
		// first we need to save the attachment to S3
		// then to db since we need the s3key to be stored in db
		fileStorage.save(attachment);
		attachments.save(attachment);
	}

	public Attachment load(Long attachmentId) {
		Attachment load = attachments.load(attachmentId);
		if (load == null) {
			throw new NotFoundException();
		}
		return load;
	}

	public void delete(Attachment attachment) {
		detachFromQuestion(attachment);
		detachFromAnswer(attachment);
		attachments.delete(attachment);
		fileStorage.delete(attachment);
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

	public InputStream open(Attachment attachment) throws IOException
	{
		return fileStorage.open(attachment);
	}

	public void delete(Iterable<Attachment> attachments) {
		for (Attachment attachment : attachments) {
			this.delete(attachment);
		}
	}
}
