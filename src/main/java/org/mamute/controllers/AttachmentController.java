package org.mamute.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
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
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
	private AttachmentsFileStorage fileStorage;
	@Inject
	private LoggedUser loggedUser;
	@Inject
	private ClientIp clientIp;

	@CustomBrutauthRules(LoggedRule.class)
	@Post
	public void uploadAttachment(UploadedFile file) throws IOException {
		Attachment attachment = new Attachment(file, loggedUser.getCurrent(), clientIp.get());
		attachments.save(attachment);
		fileStorage.save(attachment);

		result.use(json()).withoutRoot().from(attachment).serialize();
	}
}
