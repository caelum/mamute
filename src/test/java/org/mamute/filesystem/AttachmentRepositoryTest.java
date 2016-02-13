package org.mamute.filesystem;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mamute.dao.AnswerDAO;
import org.mamute.dao.AttachmentDao;
import org.mamute.dao.DatabaseTestCase;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.Answer;
import org.mamute.model.Attachment;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mockito.Mockito;

import br.com.caelum.vraptor.observer.upload.DefaultUploadedFile;
import br.com.caelum.vraptor.observer.upload.UploadedFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AttachmentRepositoryTest extends DatabaseTestCase {

	private AttachmentRepository attachmentRepository;
	private AttachmentDao attachments;
	private AttachmentsFileStorage fileStorage;
	private QuestionDAO questions;
	private AnswerDAO answers;

	@Rule
    public TemporaryFolder attachmentRootFolder = new TemporaryFolder();

	@Before
	public void setup() {
		attachments = new AttachmentDao(session);
		fileStorage = new AttachmentsFileStorage(attachmentRootFolder.getRoot().getAbsolutePath());
		questions = new QuestionDAO(session, Mockito.mock(InvisibleForUsersRule.class));
		answers = new AnswerDAO(session, Mockito.mock(InvisibleForUsersRule.class));

		this.attachmentRepository = new AttachmentRepository(attachments, fileStorage, questions, answers);
	}

	@Test
	public void save_should_write_contents_to_disk() throws IOException {
		final String fileContents = "hello";
		final Attachment attachment = createAttachment(fileContents);

		attachmentRepository.save(attachment);

		assertEquals(fileContents, IOUtils.toString(fileStorage.open(attachment)));
	}

	@Test
	public void save_should_create_database_entry() {
		final Attachment attachment = createAttachment("test");

		attachmentRepository.save(attachment);

		assertNotNull(attachment.getId());
		assertEquals(attachment, attachments.load(attachment.getId()));
	}

	@Test
	public void should_delete_database_entry() {
		final Attachment attachment = createAttachment("test");
		attachmentRepository.save(attachment);

		attachmentRepository.delete(attachment);

		assertNull(attachments.load(attachment.getId()));
	}

	@Test
	public void should_detach_from_question() {
		final User author = user("question owner", "question@owner.local");
		session.save(author);

		final Attachment attachment = createAttachment("test");
		attachmentRepository.save(attachment);

		final Tag tag = tag("testtag");
		session.save(tag);

		final Question question = question(author, tag);
		question.getAttachments().add(attachment);
		session.save(question);

		attachmentRepository.delete(question.getAttachments());

		assertEquals(0, question.getAttachments().size());
	}

	@Test
	public void should_detach_from_answer() {
		final User author = user("question owner", "question@owner.local");
		session.save(author);

		final Attachment attachment = createAttachment("test");
		attachmentRepository.save(attachment);

		final Tag tag = tag("testtag");
		session.save(tag);

		final Question question = question(author, tag);
		session.save(question);

		final Answer answer = answer("answer answer answer answer answer", question, author);
		answer.getAttachments().add(attachment);
		session.save(answer);

		attachmentRepository.delete(answer.getAttachments());

		assertEquals(0, answer.getAttachments().size());
	}

	private Attachment createAttachment(final String content) {
		final User owner = user("attachment owner", "attachment@owner.local");
		session.save(owner);

		final UploadedFile file = new DefaultUploadedFile(IOUtils.toInputStream(content), "hello.txt", "text/plain", content.length());
		final Attachment attachment = new Attachment(file, owner, "127.0.0.1");

		return attachment;
	}
}
