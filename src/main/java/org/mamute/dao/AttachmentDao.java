package org.mamute.dao;

import org.hibernate.Session;
import org.mamute.model.Attachment;

import javax.inject.Inject;

public class AttachmentDao {

	@Inject
	private Session session;

	public void save(Attachment attachment) {
		session.save(attachment);
	}
}
