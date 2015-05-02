package org.mamute.controllers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentAccessLevel {
	String value();
}
