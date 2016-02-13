package org.mamute.controllers;

import java.util.Arrays;
import java.util.Map;

import org.mamute.interceptors.PimpMyControllerInterceptor;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.validator.I18nMessage;

public class BaseController {

	protected Result result() {
		return PimpMyControllerInterceptor.getResult();
	}

	protected <T> T forwardTo(Class<T> argumento0) {
		return result().forwardTo(argumento0);
	}

	protected void forwardTo(String argumento0) {
		result().forwardTo(argumento0);
	}

	protected <T> T forwardTo(T argumento0) {
		return result().forwardTo(argumento0);
	}

	protected Result include(Object argumento0) {
		return result().include(argumento0);
	}

	protected Result include(String argumento0, Object arg1) {
		return result().include(argumento0, arg1);
	}

	protected Result includeAsList(String argumento0, Object ... arg1) {
		return result().include(argumento0, Arrays.asList(arg1));
	}

	protected Map<String, Object> included() {
		return result().included();
	}

	protected void notFound() {
		result().notFound();
	}

	protected void nothing() {
		result().nothing();
	}

	protected <T> T of(Class<T> argumento0) {
		return result().of(argumento0);
	}

	protected <T> T of(T argumento0) {
		return result().of(argumento0);
	}

	protected Result on(Class<? extends Exception> argumento0) {
		return result().on(argumento0);
	}

	protected <T> T permanentlyRedirectTo(Class<T> argumento0) {
		return result().permanentlyRedirectTo(argumento0);
	}

	protected void permanentlyRedirectTo(String argumento0) {
		result().permanentlyRedirectTo(argumento0);
	}

	protected <T> T permanentlyRedirectTo(T argumento0) {
		return result().permanentlyRedirectTo(argumento0);
	}

	protected <T> T redirectTo(Class<T> argumento0) {
		return result().redirectTo(argumento0);
	}

	protected void redirectTo(String argumento0) {
		result().redirectTo(argumento0);
	}

	protected <T> T redirectTo(T argumento0) {
		return result().redirectTo(argumento0);
	}

	protected <T extends View> T use(Class<T> argumento0) {
		return result().use(argumento0);
	}

	protected boolean used() {
		return result().used();
	}
	
	protected I18nMessage i18n(String category, String key, String ... params) {
		return PimpMyControllerInterceptor.getFactory().build(category, key, (Object[]) params);
	}

}
