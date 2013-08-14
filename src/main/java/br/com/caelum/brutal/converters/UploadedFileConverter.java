package br.com.caelum.brutal.converters;

import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor4.Convert;
import br.com.caelum.vraptor4.Converter;
import br.com.caelum.vraptor4.interceptor.multipart.UploadedFile;

@Convert(UploadedFile.class)
public class UploadedFileConverter implements Converter<UploadedFile>{
	
    @Inject private HttpServletRequest request;

	@Override
	public UploadedFile convert(String value, Class<? extends UploadedFile> type,
			ResourceBundle bundle) {
		 Object upload = request.getAttribute(value);
		return type.cast(upload);
	}

}
