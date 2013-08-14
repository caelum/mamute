package br.com.caelum.brutal.feed;

import com.thoughtworks.xstream.annotation.XStreamUnmarshalling;

public class RSSFeed {
	
	public final RSSChannel channel;

	public RSSFeed() {
		this(null);
	}
	
	@XStreamUnmarshalling
	public RSSFeed(RSSChannel channel) {
		this.channel = channel;
	}
	
	public RSSChannel getChannel() {
		return channel;
	}

	@Override
	public String toString() {
		return "RSSFeed [channel=" + channel + "]";
	}
	
	
}
