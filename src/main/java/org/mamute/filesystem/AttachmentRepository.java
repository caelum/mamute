package org.mamute.filesystem;

import org.hibernate.Session;
import org.mamute.dao.AnswerDAO;
import org.mamute.dao.AttachmentDao;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.dao.WithUserPaginatedDAO;
import org.mamute.dao.WithUserPaginatedDAO.UserRole;
import org.mamute.infra.NotFoundException;
import org.mamute.model.Answer;
import org.mamute.model.Attachment;
import org.mamute.model.Question;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

public class AttachmentRepository {

	private AttachmentDao attachments;
	private AttachmentsFileStorage fileStorage;
	private QuestionDAO questions;
	private AnswerDAO answers;

	@Deprecated
	public AttachmentRepository() {
	}

	@Inject
    public AttachmentRepository(AttachmentDao attachments, AttachmentsFileStorage fileStorage, QuestionDAO questions, AnswerDAO answers) {
        this.attachments = attachments;
		this.fileStorage = fileStorage;
		this.questions = questions;
		this.answers = answers;
    }

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

	public InputStream open(Attachment attachment) {
		try {
			return fileStorage.open(attachment);
		} catch (FileNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	public void delete(Iterable<Attachment> attachments) {
		for (Attachment attachment : attachments) {
			this.delete(attachment);
		}
	}
}
