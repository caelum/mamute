package br.com.caelum.brutal.infra.rss;

import br.com.caelum.brutal.model.Question;

import com.thoughtworks.xstream.XStream;

public class QuestionRssEntryFactory {

	public String entryOf(Question question) {
		RssEntry entry = new RssEntryBuilder()	
			.withAuthor(question.getAuthor().getName())
			.withTitle(question.getTitle())
			.withLink("http://guj.com.br/perguntas/" + question.getId() + "-" + question.getSluggedTitle())
			.withId(question.getId().toString())
			.withDate(question.getCreatedAt())
			.build();
		
		XStream xstream = new XStream();
		xstream.processAnnotations(RssEntry.class);
		return xstream.toXML(entry);
		
	}
	

}
