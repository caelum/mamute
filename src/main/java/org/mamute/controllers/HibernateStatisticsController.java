package org.mamute.controllers;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class HibernateStatisticsController {
	@Inject private SessionFactory sf;
	@Inject private Result result;
	
	@Get("/alsjkdalkjsjdhadskj")
	public void show() {
		Statistics statistics = sf.getStatistics();
		result.include("s", statistics);
	}
	

}
