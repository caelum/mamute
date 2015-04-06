package org.mamute.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import org.apache.commons.io.IOUtils;
import org.mamute.auth.FacebookAuthService;
import org.mamute.brutauth.auth.rules.EditQuestionRule;
import org.mamute.brutauth.auth.rules.InputRule;
import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.*;
import org.mamute.factory.MessageFactory;
import org.mamute.filesystem.AttachmentsFileStorage;
import org.mamute.infra.ClientIp;
import org.mamute.interceptors.IncludeAllTags;
import org.mamute.managers.TagsManager;
import org.mamute.model.*;
import org.mamute.model.post.PostViewCounter;
import org.mamute.model.watch.Watcher;
import org.mamute.search.QuestionIndex;
import org.mamute.util.TagsSplitter;
import org.mamute.validators.TagsValidator;
import org.mamute.vraptor.Linker;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static java.util.Arrays.asList;

@Routed
@Controller
public class AttachmentController {

	@Inject
	private Result result;
	@Inject
	private AttachmentDao attachments;
	@Inject
	private QuestionDAO questions;
	@Inject
	private AttachmentsFileStorage fileStorage;
	@Inject
	private LoggedUser loggedUser;
	@Inject
	private ClientIp clientIp;
	@Inject
	private HttpServletResponse response;

	@CustomBrutauthRules(LoggedRule.class)
	@Post
	public void uploadAttachment(UploadedFile file) throws IOException {
		Attachment attachment = new Attachment(file, loggedUser.getCurrent(),
				clientIp.get());
		attachments.save(attachment);
		fileStorage.save(attachment);

		result.use(json()).withoutRoot().from(attachment).serialize();
	}

	@Get
	public void getAttachment(Long attachmentId) throws IOException {
		Attachment attachment = attachments.load(attachmentId);
		if (attachment == null) {
			result.notFound();
			return;
		}
		try {
			InputStream is = fileStorage.open(attachment);
			response.setHeader("Content-Type", attachment.getMime());
			send(is);
		} catch (FileNotFoundException e) {
			result.notFound();
		}
	}

	@CustomBrutauthRules(LoggedRule.class)
	@Delete
	public void deleteAttachment(Long attachmentId) throws IOException {
		Attachment attachment = attachments.load(attachmentId);
		if (attachment == null) {
			result.notFound();
			return;
		}
		User current = loggedUser.getCurrent();
		Question question = questions.questionWith(attachment);
		if (question != null){
			question.remove(attachment);
		}
		if (!attachment.canDelete(current) && !current.isModerator()) {
			result.use(http()).sendError(403);
			return;
		}
		attachments.delete(attachment);
		fileStorage.delete(attachment);
		result.nothing();
	}

	private void send(InputStream is) {
		try {
			ServletOutputStream os = response.getOutputStream();
			IOUtils.copy(is, os);
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
	}

	private void close(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
