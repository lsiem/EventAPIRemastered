package com.darkmagician6.eventapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.darkmagician6.eventapi.types.Priority;

/**
 * Marks a method so that the registry knows that it should be registered.
 * The priority of the method is also set with this. 
 * @see Priority
 * 
 * @author DarkMagician6
 * @since July 30, 2013
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
	byte value() default Priority.MEDIUM;
}
