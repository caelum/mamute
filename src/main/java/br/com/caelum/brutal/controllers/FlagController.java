package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOrKarmaRule;
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
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class FlagController {
	
	@Inject private Result result;
	@Inject private FlagDao flags;
	@Inject private LoggedUser loggedUser;
	@Inject private FlaggableDAO flaggables;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private FlagTrigger flagTrigger;

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
	@Get("/marcadas")
	public void topFlagged() {
		List<FlaggableAndFlagCount> flaggedQuestions = flaggables.flaggedButVisible(Question.class);
		List<FlaggableAndFlagCount> flaggedAnswers = flaggables.flaggedButVisible(Answer.class);
		List<FlaggableAndFlagCount> flaggedComments = flaggables.flaggedButVisible(Comment.class);
		
		result.include("questions", flaggedQuestions);
		result.include("answers", flaggedAnswers);
		result.include("comments", flaggedComments);
	}
	
}
