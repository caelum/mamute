package org.mamute.infra.rss.read;

import static java.lang.Integer.valueOf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.mamute.infra.rss.RSSType;

import br.com.caelum.vraptor.environment.Environment;

@ApplicationScoped
public class FeedsMap {

	private Map<String, RSSFeed> hashMap = new HashMap<String, RSSFeed>();
	@Inject	private Environment env;
	
	public void putOrUpdate(String feedBaseKey, RSSType type){
		hashMap.remove(feedBaseKey);
		String uri = env.get(feedBaseKey+".url");
		Integer numberOfItems = valueOf(env.get(feedBaseKey+".items"));
		FeedReader feedReader = new FeedReader(type);
		RSSFeed feed = feedReader.read(uri, numberOfItems);
		hashMap.put(feedBaseKey, feed);
	}
	
	public RSSFeed get(String feedBaseKey){
		return hashMap.get(feedBaseKey);
	}

	public Set<Entry<String, RSSFeed>> entrySet() {
		return new HashSet<>(hashMap.entrySet());
	}
}