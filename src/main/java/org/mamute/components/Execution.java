package org.mamute.components;

import br.com.caelum.vraptor.ioc.Container;

public interface Execution<T> {

	T insideRequest(Container container);

}
