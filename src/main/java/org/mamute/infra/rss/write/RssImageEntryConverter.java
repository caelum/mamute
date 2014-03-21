package org.mamute.infra.rss.write;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RssImageEntryConverter implements Converter{

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(RssImageEntry.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		final RssImageEntry entry = (RssImageEntry) value;
		writer.addAttribute("url", entry.getUrl());
		writer.addAttribute("type", "image/*");
		writer.addAttribute("length", "0");
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		throw new UnsupportedOperationException();
	}

}
