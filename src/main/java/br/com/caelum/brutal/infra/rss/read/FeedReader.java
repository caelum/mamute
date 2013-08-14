package br.com.caelum.brutal.infra.rss.read;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import br.com.caelum.vraptor.ioc.Component;


@Component
public class FeedReader {

	private final FeedConverter converter;

	public FeedReader(FeedConverter converter) {
		this.converter = converter;
	}
	
	public RSSFeed read(String uri) throws ClientProtocolException, IOException{
		InputStream rssXml = getXmlFrom(uri);
		return converter.convert(rssXml);
	}

	private InputStream getXmlFrom(String uri) throws IOException,
			ClientProtocolException {
		HttpGet httpGet = new HttpGet(uri);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		return content;
	}
	
}
