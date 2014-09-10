package br.com.caelum.vraptor.serialization;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import org.hibernate.proxy.HibernateProxy;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
public class HibernateProxySerializer implements JsonSerializer<HibernateProxy> {
	@Override
	public JsonElement serialize(HibernateProxy src, Type typeOfSrc, JsonSerializationContext context) {
		Object deProxied = src.getHibernateLazyInitializer().getImplementation();
		return context.serialize(deProxied);
	}
}