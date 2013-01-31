package br.com.caelum.pagpag;

import java.util.List;

import br.com.caelum.pagpag.modelo.dominio.MudancaStatus;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MudancasConverter implements Converter {

	@Override
	public boolean canConvert(Class arg0) {
		return true;
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		List<MudancaStatus> mudancas = (List<MudancaStatus>) obj;
		for (MudancaStatus mudanca : mudancas) {
			writer.startNode("mudanca");
			writer.startNode("id");
			writer.setValue(String.valueOf(mudanca.getId()));
			writer.endNode();
			writer.endNode();
		}
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return context.convertAnother(context.currentObject(), List.class);
	}

}
