package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorOrKarmaAccess;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.dao.FlagDao;
import br.com.caelum.brutal.dao.FlaggableDAO;
import br.com.caelum.brutal.dto.CommentAndFlagCount;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class FlagController {
	
	private final Result result;
	private final FlagDao flags;
	private final LoggedUser loggedUser;
	private final FlaggableDAO flaggables;
	private final ModelUrlMapping urlMapping;

	public FlagController(Result result, CommentDAO comments, FlagDao flags,
			LoggedUser loggedUser, FlaggableDAO flaggables, ModelUrlMapping urlMapping) {
		this.result = result;
		this.flags = flags;
		this.loggedUser = loggedUser;
		this.flaggables = flaggables;
		this.urlMapping = urlMapping;
	}

	@MinimumReputation(PermissionRulesConstants.CREATE_FLAG)
	@Post("/{flaggableType}/{flaggableId}/marcar")
	public void addFlag(String flaggableType, Long flaggableId, FlagType flagType, String reason) {
		Class<?> clazz = urlMapping.getClassFor(flaggableType);

		if (flagType == null) {
			result.use(Results.http()).sendError(400);
			return;
		}
		
		if (flags.alreadyFlagged(loggedUser.getCurrent(), flaggableId, clazz)) {
			result.use(Results.http()).sendError(409); //conflict
			return;
		}
		
		Flaggable flaggable = flaggables.getById(flaggableId, clazz);

		Flag flag = new Flag(flagType, loggedUser.getCurrent());
		if (flagType.equals(FlagType.OTHER)) {
			flag.setReason(reason);
		}
		
		flags.save(flag);
		flaggable.add(flag);
		
		result.nothing();
	}
	
	@ModeratorOrKarmaAccess
	@Get("/{flaggableType}s/marcados")
	public void topFlaggedComments(String flaggableType) {
		Class<?> model = urlMapping.getClassFor(flaggableType);
		List<CommentAndFlagCount> flaggedComments = flaggables.flagged(model, 3l);
		result.include("flaggedComments", flaggedComments);
	}
	
}
