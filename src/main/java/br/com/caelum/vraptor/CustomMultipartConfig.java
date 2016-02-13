package br.com.caelum.vraptor;

import br.com.caelum.vraptor.observer.upload.DefaultMultipartConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

@Specializes
@ApplicationScoped
public class CustomMultipartConfig extends DefaultMultipartConfig {

	public long getSizeLimit() {
		return 50 * 1024 * 1024;
	}

	public long getFileSizeLimit() {
		return 50 * 1024 * 1024;
	}
}
