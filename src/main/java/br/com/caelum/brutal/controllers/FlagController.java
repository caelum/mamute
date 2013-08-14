package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor4.view.Results.http;
import static br.com.caelum.vraptor4.view.Results.page;

import java.util.List;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.dao.FlagDao;
import br.com.caelum.brutal.dao.FlaggableDAO;
import br.com.caelum.brutal.dto.FlaggableAndFlagCount;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.flag.FlagTrigger;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;

@Controller
public class FlagController {
	
	private final Result result;
	private final FlagDao flags;
	private final LoggedUser loggedUser;
	private final FlaggableDAO flaggables;
	private final ModelUrlMapping urlMapping;
	private final FlagTrigger flagTrigger;

	public FlagController(Result result, CommentDAO comments, FlagDao flags,
			LoggedUser loggedUser, FlaggableDAO flaggables, 
			ModelUrlMapping urlMapping, FlagTrigger flagTrigger) {
		this.result = result;
		this.flags = flags;
		this.loggedUser = loggedUser;
		this.flaggables = flaggables;
		this.urlMapping = urlMapping;
		this.flagTrigger = flagTrigger;
	}

	@SimpleBrutauthRules({ModeratorOrKarmaRule.class})
	@AccessLevel(PermissionRulesConstants.CREATE_FLAG)
	@Post("/{flaggableType}/{flaggableId}/marcar")
	public void addFlag(String flaggableType, Long flaggableId, FlagType flagType, String reason) {
		Class<?> clazz = urlMapping.getClassFor(flaggableType);
		if (flagType == null) {
			result.use(http()).sendError(400);
			return;
		}
		
		if (flags.alreadyFlagged(loggedUser.getCurrent(), flaggableId, clazz)) {
			result.use(http()).sendError(409); //conflict
			return;
		}
		
		Flaggable flaggable = flaggables.getById(flaggableId, clazz);
		flagTrigger.fire(flaggable);

		Flag flag = new Flag(flagType, loggedUser.getCurrent());
		if (flagType.equals(FlagType.OTHER)) {
			flag.setReason(reason);
		}
		
		flags.save(flag);
		flaggable.add(flag);
		
		result.nothing();
	}
	

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get("/perguntas/marcadas")
	public void topFlaggedQuestions() {
		topFlagged(Question.class);
	}
	

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get("/comentarios/marcados")
	public void topFlaggedComments() {
		topFlagged(Comment.class);
	}
	

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get("/respostas/marcados")
	public void topFlaggedAnswers() {
		topFlagged(Answer.class);
	}

	private void topFlagged(Class<?> model) {
		List<FlaggableAndFlagCount> flaggedAndCount = flaggables.flaggedButVisible(model);
		result.include("flagged", flaggedAndCount);
		result.use(page()).forwardTo("/WEB-INF/jsp/flag/topFlagged" + model.getSimpleName() + "s.jsp");
	}
	
}
