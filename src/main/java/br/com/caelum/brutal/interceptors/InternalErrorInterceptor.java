package br.com.caelum.brutal.interceptors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.providers.GlobalInterceptor;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
@Intercepts(before=GlobalInterceptor.class)
public class InternalErrorInterceptor implements Interceptor{
	
	private final Result result;
	private static Logger log = Logger.getLogger(InternalErrorInterceptor.class);

	public InternalErrorInterceptor(Result result) {
		this.result = result;
	}

	@Override
	public boolean accepts(ResourceMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		try {
			stack.next(method, instance);
		}catch (Exception e) {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			Throwable cause = e.getCause();
			if (cause instanceof ConstraintViolationException) {
				Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
				pw.printf("\nConstraint Violations: \n");
				for (ConstraintViolation<?> constraintViolation : constraintViolations) {
					pw.printf("\t" +constraintViolation.getConstraintDescriptor().getAnnotation()+"\n");
				}
				pw.printf("\n");
				log.error(sw.toString());
			}
			
			e.printStackTrace(pw);
			
			pw.close();
			result.include("stacktrace", sw.toString());
			throw e;
		}
	}
}