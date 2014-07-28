package com.cn.naive.library.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Table annotation. This annotation should be marked on the Class.
 * 
 * @author Steven.Luo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Table {
	public String name();

	/**
	 * This method defines how to create the table
	 * 
	 * @return true if the create table SQL script generated automatically,
	 *         false if defined at {@link #createScript()}
	 */
	public boolean autoCreate() default true;

	/**
	 * Define the create table SQL script. This method must be defined if
	 * {@link #autoCreate()} is set to false.
	 * 
	 * @return The complete create SQL script should be defined.
	 */
	public String createScript() default "";
}