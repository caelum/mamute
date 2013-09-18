package br.com.caelum.brutal.infra;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.interceptor.ControllerLookupInterceptor;
import br.com.caelum.vraptor.interceptor.multipart.CommonsUploadMultipartInterceptor;
import br.com.caelum.vraptor.interceptor.multipart.MultipartConfig;
import br.com.caelum.vraptor.interceptor.multipart.ServletFileUploadCreator;

@Intercepts(before = ControllerLookupInterceptor.class, after = {})
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class BrutalMultipartInterceptor extends CommonsUploadMultipartInterceptor {
    
    private static final Logger LOG = Logger.getLogger(BrutalMultipartInterceptor.class);

    @Deprecated
    public BrutalMultipartInterceptor() {
	}

    @Inject
    public BrutalMultipartInterceptor(HttpServletRequest request, MutableRequest parameters, MultipartConfig cfg,
            Validator validator, ServletFileUploadCreator fileUploadCreator) {
        super(request, parameters, cfg, validator, fileUploadCreator);
    }
    
    @Override
    public void intercept(InterceptorStack stack, ControllerMethod method, Object instance)
            throws InterceptionException {
        LOG.info("using own multipart interceptor");
        super.intercept(stack, method, instance);
    }

}
