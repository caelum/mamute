package br.com.caelum.brutal.controllers;

import java.util.Arrays;
import java.util.Map;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.validator.I18nMessage;

public class Controller {

	protected Result result() {
		return PimpMyControllerInterceptor.getResult();
	}

	public <T> T forwardTo(Class<T> arg0) {
		return result().forwardTo(arg0);
	}

	public void forwardTo(String arg0) {
		result().forwardTo(arg0);
	}

	public <T> T forwardTo(T arg0) {
		return result().forwardTo(arg0);
	}

	public Result include(Object arg0) {
		return result().include(arg0);
	}

	public Result include(String arg0, Object arg1) {
		return result().include(arg0, arg1);
	}

	public Result includeAsList(String arg0, Object ... arg1) {
		return result().include(arg0, Arrays.asList(arg1));
	}

	public Map<String, Object> included() {
		return result().included();
	}

	public void notFound() {
		result().notFound();
	}

	public void nothing() {
		result().nothing();
	}

	public <T> T of(Class<T> arg0) {
		return result().of(arg0);
	}

	public <T> T of(T arg0) {
		return result().of(arg0);
	}

	public Result on(Class<? extends Exception> arg0) {
		return result().on(arg0);
	}

	public <T> T permanentlyRedirectTo(Class<T> arg0) {
		return result().permanentlyRedirectTo(arg0);
	}

	public void permanentlyRedirectTo(String arg0) {
		result().permanentlyRedirectTo(arg0);
	}

	public <T> T permanentlyRedirectTo(T arg0) {
		return result().permanentlyRedirectTo(arg0);
	}

	public <T> T redirectTo(Class<T> arg0) {
		return result().redirectTo(arg0);
	}

	public void redirectTo(String arg0) {
		result().redirectTo(arg0);
	}

	public <T> T redirectTo(T arg0) {
		return result().redirectTo(arg0);
	}

	public <T extends View> T use(Class<T> arg0) {
		return result().use(arg0);
	}

	public boolean used() {
		return result().used();
	}
	
	public I18nMessage i18n(String category, String key, String ... params) {
		return PimpMyControllerInterceptor.getFactory().build(category, key, (Object[]) params);
	}

}
