package br.com.caelum.brutal.controllers;

import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.ReputationEventDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class RankingController {
	
	private final Result result;
	private final UserDAO users;
	private final TagDAO tags;
	private final ReputationEventDAO reputationEvents;

	public RankingController(Result result, UserDAO users, TagDAO tags, ReputationEventDAO reputationEvents) {
		this.result = result;
		this.users = users;
		this.tags = tags;
		this.reputationEvents = reputationEvents;
	}

	@Get("/ranking")
	public void rank(Integer p) {
		int page = p == null ? 1 : p;
		result.include("topUsers", users.getRank(page));
		result.include("pages", users.numberOfPages());
		result.include("currentPage", Integer.valueOf(page));
	}
	
	@Get("/ranking/{tagName}")
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
	}
}
