package br.com.caelum.brutal.infra;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Validator;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.http.MutableRequest;
import br.com.caelum.vraptor4.interceptor.ControllerLookupInterceptor;
import br.com.caelum.vraptor4.interceptor.multipart.CommonsUploadMultipartInterceptor;
import br.com.caelum.vraptor4.interceptor.multipart.MultipartConfig;
import br.com.caelum.vraptor4.interceptor.multipart.ServletFileUploadCreator;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;

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
