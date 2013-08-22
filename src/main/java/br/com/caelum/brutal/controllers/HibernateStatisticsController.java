package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Result;

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
