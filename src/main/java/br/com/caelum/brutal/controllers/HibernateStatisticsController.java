package br.com.caelum.brutal.controllers;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Result;

@Controller
public class HibernateStatisticsController {
	private final SessionFactory sf;
	private final Result result;

	public HibernateStatisticsController(SessionFactory sf, Result result) {
		this.sf = sf;
		this.result = result;
	}
	
	@Get("/alsjkdalkjsjdhadskj")
	public void show() {
		Statistics statistics = sf.getStatistics();
		result.include("s", statistics);
	}
	

}
