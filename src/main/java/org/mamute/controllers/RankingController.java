package org.mamute.controllers;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.mamute.dao.ReputationEventDAO;
import org.mamute.dao.TagDAO;
import org.mamute.dao.UserDAO;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class RankingController {
	
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private TagDAO tags;
	@Inject private ReputationEventDAO reputationEvents;


	@Get
	public void rank(Integer p) {
		int page = p == null ? 1 : p;
		result.include("topUsers", users.getRank(page));
		result.include("pages", users.numberOfPages());
		result.include("currentPage", Integer.valueOf(page));
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get
	public void tagRank(String tagName){
		Tag tag = tags.findByName(tagName);
		if (tag == null){
			result.notFound();
		}
		result.include("tag", tag);
		DateTime after = new DateTime().minusDays(30);
		result.include("tag", tag);
		result.include("answerersAllTime", reputationEvents.getTopAnswerersSummaryAllTime(tag));
		result.include("answerersLastMonth", reputationEvents.getTopAnswerersSummaryAfter(tag, after));
		result.include("askersAllTime", reputationEvents.getTopAskersSummaryAllTime(tag));
		result.include("askersLastMonth", reputationEvents.getTopAskersSummaryAfter(tag, after));
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}
}
