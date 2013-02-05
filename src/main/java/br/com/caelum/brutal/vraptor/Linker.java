package br.com.caelum.brutal.vraptor;

public interface Linker {

	<T> T linkTo(T controller);

	<T> T linkTo(Class<T> controllerType);

	String get();

}