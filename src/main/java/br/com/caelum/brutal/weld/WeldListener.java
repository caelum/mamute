package br.com.caelum.brutal.weld;

import java.util.Arrays;

import org.jboss.weld.environment.Container;
import org.jboss.weld.environment.ContainerContext;
import org.jboss.weld.environment.jetty.JettyContainer;
import org.jboss.weld.environment.servlet.Listener;


public class WeldListener extends Listener {
	
	@Override
	protected Container findContainer(ContainerContext cc, StringBuilder dump) {
		return checkContainers(cc, dump, Arrays.asList(JettyContainer.INSTANCE));
	}

}
