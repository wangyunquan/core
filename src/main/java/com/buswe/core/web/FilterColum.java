package com.buswe.core.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterColum {

	String [] value() default {};
	String [] type() default {};
	String start() default PropertyFilter.FILTER_STRING;
	
	
}
