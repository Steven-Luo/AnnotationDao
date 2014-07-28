package com.cn.naive.library.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Id annotation, if a field has been marked with this annotation, it should
 * also be marked with {@link Column}. Attention please, this annotation can be
 * used at most once in one class and column labeled with this annotation must
 * be type of integer.
 * 
 * @author Steven.Luo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Id {

	/**
	 * The name of the primary key column, this value should be consistent with
	 * {@link Column#name()}
	 * 
	 * @return
	 * @see Column#name()
	 */
	public String name();

	public boolean autoIncrement() default true;

	public String constrain() default "";
}
