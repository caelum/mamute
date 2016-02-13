package org.mamute.infra.rss.write;

import static java.util.Arrays.asList;

import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import javax.inject.Inject;

import org.mamute.model.User;
import org.mamute.model.interfaces.RssContent;

import br.com.caelum.vraptor.environment.Environment;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class QuestionRssEntryFactory {

	private String gravatarUrl;
	private String home;
	
	@Deprecated
	public QuestionRssEntryFactory() {
	}

	@Inject
	public QuestionRssEntryFactory(Environment env) {
		this.home = env.get("host") + "/";
		home = home.endsWith("/") ? home : home + "/";
		this.gravatarUrl = env.get("gravatar.avatar.url");
	}

	public void writeEntry(RssContent rssContent, OutputStream output) {
		
		User author = rssContent.getAuthor();
		RssImageEntry imageEntry = new RssImageEntryBuilder()
			.withUrl(author.getSmallPhoto(gravatarUrl)).build();
		
		RssEntry entry = new RssEntryBuilder()
				.withAuthor(author.getName())
				.withTitle(rssContent.getTitle())
				.withLink(home + rssContent.getLinkPath())
				.withId(rssContent.getId().toString())
				.withDate(rssContent.getCreatedAt())
				.withImage(imageEntry).build();
		
		XStream xstream = buildXstream();

		xstream.processAnnotations(RssEntry.class);
		xstream.toXML(entry, output);
	}

	private XStream buildXstream() {
		return new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					List<String> cdataFields = asList("title", "author");
					boolean cdata = false;
					
					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata = cdataFields.contains(name);
					}
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
	}
}
