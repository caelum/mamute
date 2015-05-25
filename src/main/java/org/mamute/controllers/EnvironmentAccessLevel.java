package org.mamute.controllers;

import org.mamute.auth.rules.PermissionRules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentAccessLevel {
	PermissionRules value();
}
