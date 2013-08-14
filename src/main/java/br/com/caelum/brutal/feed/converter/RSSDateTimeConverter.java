package br.com.caelum.brutal.feed.converter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RSSDateTimeConverter implements Converter {

	private static final DateTimeFormatter RSS_PATTERN = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss Z");

	@Override
	public boolean canConvert(Class type) {
		return DateTime.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		writer.setValue(RSS_PATTERN.print((DateTime) source));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return RSS_PATTERN.parseDateTime(reader.getValue());
	}

}
