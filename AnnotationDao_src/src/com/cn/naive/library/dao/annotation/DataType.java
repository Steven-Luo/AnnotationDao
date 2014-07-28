package com.cn.naive.library.dao.annotation;

public enum DataType {
	Integer, Short, Long, Float, Double, Calendar, Char, Varchar, Boolean;

	public String convert() {
		String result = null;
		switch (this) {
		case Integer:
			result = "integer";
			break;

		case Short:
			result = "short";
			break;

		case Long:
			result = "long";
			break;

		case Float:
			result = "float";
			break;

		case Double:
			result = "double";
			break;

		case Calendar:
			result = "long";
			break;

		case Char:
			result = "char({0})";
			break;

		case Varchar:
			result = "varchar({0})";
			break;

		case Boolean:
			result = "integer";
			break;

		default:
			break;
		}
		return result;
	}
}