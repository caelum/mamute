package br.com.caelum.pagpag;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.pagpag.modelo.dominio.Parcelamento;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ParcelamentosConverter implements Converter{

	@Override
	public boolean canConvert(Class arg0) {
		return true;
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		List<Parcelamento> parcelamentos = (List<Parcelamento>) obj;
		for (Parcelamento parcelamento : parcelamentos) {
			writer.startNode("parcelamento");
			writer.startNode("id");
			writer.setValue(String.valueOf(parcelamento.getId()));
			writer.endNode();
			writer.endNode();
		}
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		List<Parcelamento> parcs = new ArrayList<Parcelamento>();
		while(reader.hasMoreChildren()) {
			reader.moveDown();
		
			Parcelamento parc = new Parcelamento();
			while(reader.hasMoreChildren()) {
				reader.moveDown();
				setAttribute(parc, reader.getNodeName(), reader.getValue());
				reader.moveUp();
			}
			parcs.add(parc);
			reader.moveUp();
		}
		return parcs;
	}

	private void setAttribute(Parcelamento parc, String name, String value) {
		Field field = getField(parc.getClass(), name);
		Method method = findSetterOf(parc.getClass(), name);
		try {
			if(field.getType().equals(int.class)){
				method.invoke(parc, Integer.valueOf(value));
			} else if(field.getType().equals(BigDecimal.class)) {
				method.invoke(parc, new BigDecimal(value));
			} else if(field.getType().equals(Long.class)) {
				method.invoke(parc, Long.valueOf(value));
			} else {
				throw new RuntimeException("the field " + name + "isn't recognize by ParcelamentosConverter");
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private Field getField(Class<? extends Parcelamento> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Method findSetterOf(Class<? extends Parcelamento> clazz,
			String name) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(setterOf(name))) {
				return method;
			}
		}
		return null;
	}

	private String setterOf(String name) {
		return "set" + capitalize(name);
	}

	private String capitalize(String name) {
		String first = String.valueOf(name.charAt(0)).toUpperCase();
		return first + name.substring(1);
	}
}
