package br.com.caelum.brutal.infra.rss.write;

import static java.util.Arrays.asList;

import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.RssContent;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Component
public class QuestionRssEntryFactory {
	
	private String home;

	public QuestionRssEntryFactory(Environment env) {
		this.home = env.get("host") + "/";
		home = home.endsWith("/") ? home : home + "/";
	}

	public void writeEntry(RssContent rssContent, OutputStream output) {
		
		User author = rssContent.getAuthor();
		RssImageEntry imageEntry = new RssImageEntryBuilder()
			.withUrl(author.getSmallPhoto()).build();
		
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
