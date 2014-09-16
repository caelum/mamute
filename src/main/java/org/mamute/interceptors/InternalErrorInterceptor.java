package org.mamute.interceptors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.mamute.providers.GlobalInterceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.FlashInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts(before=GlobalInterceptor.class, after=FlashInterceptor.class)
public class InternalErrorInterceptor implements Interceptor {
	
	@Inject private Result result;
	private static Logger log = Logger.getLogger(InternalErrorInterceptor.class);

	@Override
	public boolean accepts(ControllerMethod arg0) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object instance) throws InterceptionException {
		try {
			stack.next(method, instance);
		}catch (Exception e) {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			Throwable cause = e.getCause();
			if(cause != null){
				if (cause instanceof ConstraintViolationException) {
					Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
					pw.printf("\nConstraint Violations: \n");
					for (ConstraintViolation<?> constraintViolation : constraintViolations) {
						pw.printf("\t" +constraintViolation.getConstraintDescriptor().getAnnotation()+"\n");
					}
					pw.printf("\n");
					log.error(sw.toString());
				}
				cause.printStackTrace(pw);
			}else{
				e.printStackTrace(pw);
			}
			
			pw.close();
			result.include("stacktrace", sw.toString());
			throw e;
		}
	}
}