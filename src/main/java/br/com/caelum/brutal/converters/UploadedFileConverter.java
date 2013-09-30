package br.com.caelum.brutal.converters;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

@Convert(UploadedFile.class)
public class UploadedFileConverter implements Converter<UploadedFile>{
	
    @Inject private HttpServletRequest request;

	@Override
	public UploadedFile convert(String value, Class<? extends UploadedFile> type) {
		 Object upload = request.getAttribute(value);
		return type.cast(upload);
	}

}
