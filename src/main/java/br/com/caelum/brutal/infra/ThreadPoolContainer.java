package br.com.caelum.brutal.infra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor4.ioc.ApplicationScoped;

@ApplicationScoped
public class ThreadPoolContainer {

	private static final Logger LOG = Logger.getLogger(ThreadPoolContainer.class);   
	private final ExecutorService executor;

	public ThreadPoolContainer() {
        executor = Executors.newFixedThreadPool(10);
	}
	
	public void execute(Runnable r) {
		try {
			executor.execute(r);
		} catch (Exception e) {
			LOG.error("could not run thread", e);
		}
	}
}
