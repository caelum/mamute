package br.com.caelum.brutal.controllers;

import java.util.List;

import br.com.caelum.brutal.auth.ModeratorAccess;
import br.com.caelum.brutal.auth.rules.MinimumReputation;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.dao.FlagDao;
import br.com.caelum.brutal.dao.FlaggableDAO;
import br.com.caelum.brutal.model.Comment;
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
	private final CommentDAO comments;
	private final FlagDao flags;
	private final LoggedUser loggedUser;
	private FlaggableDAO flaggables;

	public FlagController(Result result, CommentDAO comments, FlagDao flags,
			LoggedUser loggedUser, FlaggableDAO flaggables) {
		this.result = result;
		this.comments = comments;
		this.flags = flags;
		this.loggedUser = loggedUser;
		this.flaggables = flaggables;
	}

	@MinimumReputation(PermissionRulesConstants.CREATE_FLAG)
	@Post("/{flaggableType}/{flaggableId}/flag")
	public void addFlag(String flaggableType, Long flaggableId, FlagType flagType, String reason) {
		if (flagType == null) {
			result.use(Results.http()).sendError(400);
			return;
		}
		if (flags.alreadyFlagged(loggedUser.getCurrent(), flaggableId, flaggableType)) {
			result.use(Results.http()).sendError(409); //conflict
			return;
		}
		
		Flaggable flaggable = flaggables.getById(flaggableId, flaggableType);

		Flag flag = new Flag(flagType, loggedUser.getCurrent());
		if (flagType.equals(FlagType.OTHER)) {
			flag.setReason(reason);
		}
		
		flags.save(flag);
		flaggable.add(flag);
		
		result.nothing();
	}
	
	@ModeratorAccess
	@Get("/comments/flagged")
	public void topFlaggedComments() {
		List<Comment> flaggedComments = comments.flagged(3l);
		result.include("flaggedComments", flaggedComments);
	}
}
