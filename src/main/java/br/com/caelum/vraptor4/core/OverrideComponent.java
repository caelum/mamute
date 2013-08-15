package br.com.caelum.vraptor4.core;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;
import javax.interceptor.Interceptor;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@Stereotype
public @interface OverrideComponent {

}
