package org.mamute.controllers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.search.IndexSyncJob;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

@Controller
@CustomBrutauthRules(ModeratorOnlyRule.class)
public class IndexSyncController {
	public static final String DEFAULT_SYNC = "0 0 0/1 1/1 * ? *";

	@Inject private IndexSyncJob job;
	@Inject private Result result;

	@Post("/sdfajsdfjaoiji")
	public void execute() {
		job.execute();
		result.nothing();
	}
}
