package br.com.caelum.brutal.infra.rss.read;

import static java.lang.Integer.valueOf;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class FeedsMap {

	private Map<String, RSSFeed> hashMap = new HashMap<String, RSSFeed>();
	private final FeedReader feedReader;
	private final Environment env;
	
	public FeedsMap(Environment env, FeedReader feedReader) {
		this.env = env;
		this.feedReader = feedReader;
	}
	
	public void putOrUpdate(String feedBaseKey){
		hashMap.remove(feedBaseKey);
		String uri = env.get(feedBaseKey+".url");
		Integer numberOfItems = valueOf(env.get(feedBaseKey+".items"));
		RSSFeed feed = feedReader.read(uri, numberOfItems);
		hashMap.put(feedBaseKey, feed);
	}
	
	public RSSFeed get(String feedBaseKey){
		return hashMap.get(feedBaseKey);
	}
	
}
