package br.com.caelum.brutal.infra;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.http.MutableRequest;
import br.com.caelum.vraptor4.interceptor.multipart.CommonsUploadMultipartInterceptor;
import br.com.caelum.vraptor4.interceptor.multipart.ServletFileUploadCreator;

@Intercepts(before = ResourceLookupInterceptor.class, after = {})
@Component
public class BrutalMultipartInterceptor extends CommonsUploadMultipartInterceptor {
    
    private static final Logger LOG = Logger.getLogger(BrutalMultipartInterceptor.class);

    public BrutalMultipartInterceptor(HttpServletRequest request, MutableRequest parameters,
            MultipartConfig cfg, Validator validator, ServletFileUploadCreator fileUploadCreator) {
        super(request, parameters, cfg, validator, fileUploadCreator);
    }
    
    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object instance)
            throws InterceptionException {
        LOG.info("using own multipart interceptor");
        super.intercept(stack, method, instance);
    }

}
