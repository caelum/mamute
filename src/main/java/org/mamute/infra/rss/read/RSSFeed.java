package org.mamute.infra.rss.read;


public class RSSFeed {
	
	public RSSChannel channel;

	public RSSChannel getChannel() {
		return channel;
	}

	@Override
	public String toString() {
		return "RSSFeed [channel=" + channel + "]";
	}

	public void limitItems(Integer numberOfItems) {
		getChannel().limitItems(numberOfItems);		
	}
	
	
}
