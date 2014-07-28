package com.cn.naive.library.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Column annotation, if a field has been marked with this annotation, it
 * should also be marked with {@link Column}. Attention please, this annotation
 * can be used at most once in one class and column labeled with this annotation
 * must be type of integer.
 * 
 * @author Steven.Luo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Column {
	public String name();

	public DataType type();

	public boolean unique() default false;

	public boolean nullable() default true;

	public String constrain() default "";

	/**
	 * If the field is not DataType.Varchar or DataType.Char, this config will
	 * not work.
	 * 
	 * @see DataType
	 * @return The length of the field
	 */
	public int length() default 10;
}
