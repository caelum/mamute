package org.mamute.controllers;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class HibernateStatisticsController {
	@Inject private SessionFactory sf;
	@Inject private Result result;
	
	@Get("/alsjkdalkjsjdhadskj")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void show() {
		Statistics statistics = sf.getStatistics();
		result.include("s", statistics);
	}
	

}
