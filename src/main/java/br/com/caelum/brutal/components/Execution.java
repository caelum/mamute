package br.com.caelum.brutal.components;

import br.com.caelum.vraptor.ioc.Container;

public interface Execution<T> {

	T insideRequest(Container container);

}
