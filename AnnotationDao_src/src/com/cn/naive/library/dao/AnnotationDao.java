package com.cn.naive.library.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.cn.naive.library.dao.annotation.Column;
import com.cn.naive.library.dao.annotation.DataType;
import com.cn.naive.library.dao.annotation.Id;
import com.cn.naive.library.dao.annotation.Table;

/**
 * This class implements the Object Relationship Mapping.
 * 
 * @author Steven.Luo
 * @param <T>
 *            The type mapping to relationship
 */
public class AnnotationDao<T> extends AbstractDao<T> {

	public AnnotationDao(Context context, Class<T> entityClass) {
		super(context, entityClass);
		Table table = entityClass.getAnnotation(Table.class);
		if (table != null) {
			super.setTableName(table.name());
		} else {
			throw new RuntimeException("There is no table annotation on the class!");
		}
	}

	@Override
	public int delete(T t) {
		int result = 0;
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		try {
			Method[] methods = getEntityClass().getDeclaredMethods();
			for (Method m : methods) {
				Id id = m.getAnnotation(Id.class);
				if (id != null) {
					result = db.delete(getTableName(), id.name() + "=?",
							new String[] { String.valueOf((Integer) (m.invoke(t))) });
					break;
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}

	@Override
	protected String getCreateSql() {
		Table t = getEntityClass().getAnnotation(Table.class);

		if (t.autoCreate() == false) {
			return t.createScript();
		}

		StringBuilder sql = new StringBuilder("create table ");
		sql.append(t.name()).append(" (");

		Method[] methods = getEntityClass().getDeclaredMethods();
		for (Method m : methods) {
			Column attr = m.getAnnotation(Column.class);
			if (attr != null) {
				Id id = m.getAnnotation(Id.class);
				if (id != null) {
					sql.append(attr.name()).append(" integer primary key")
							.append(id.autoIncrement() ? " autoincrement, " : ", ");
					continue;
				}
				sql.append(attr.name()).append(" ");
				if (attr.type() == DataType.Char || attr.type() == DataType.Varchar) {
					sql.append(MessageFormat.format(attr.type().convert(), attr.length()));
				} else {
					sql.append(attr.type().convert());
				}
				if (attr.unique())
					sql.append(" unique");
				if (!attr.nullable())
					sql.append(" not null");
				if (!attr.constrain().equals(""))
					sql.append(" ").append(attr.constrain());

				sql.append(",");
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return sql.toString();
	}

	@Override
	protected ContentValues newContentValues(T t) {
		ContentValues values = new ContentValues();
		for (Method m : getEntityClass().getDeclaredMethods()) {
			Id id = m.getAnnotation(Id.class);
			if (id != null)
				continue;

			Column attr = m.getAnnotation(Column.class);
			if (attr != null) {
				try {
					switch (attr.type()) {
					case Integer:
						values.put(attr.name(), (Integer) (m.invoke(t)));
						break;

					case Short:
						values.put(attr.name(), (Short) (m.invoke(t)));
						break;

					case Long:
						values.put(attr.name(), (Long) (m.invoke(t)));
						break;

					case Float:
						values.put(attr.name(), (Float) (m.invoke(t)));
						break;
					case Double:
						values.put(attr.name(), (Float) (m.invoke(t)));
						break;

					case Calendar:
						Object tmp = m.invoke(t);
						Long l;
						if (tmp == null) {
							l = null;
						} else {
							l = ((Calendar) tmp).getTimeInMillis();
						}
						values.put(attr.name(), l);
						break;

					case Char:
					case Varchar:
						values.put(attr.name(), (String) (m.invoke(t)));
						break;

					case Boolean:
						values.put(attr.name(), (Boolean) (m.invoke(t)));
						break;

					default:
						break;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return values;
	}

	@Override
	protected T newInstance(Cursor cursor) {
		T t = null;
		try {
			t = getEntityClass().newInstance();
			Method[] methods = getEntityClass().getDeclaredMethods();
			for (Method m : methods) {
				Column attr = m.getAnnotation(Column.class);
				if (attr != null) {
					Class<?> returnType = m.getReturnType();
					String methodHead;
					if (returnType.getSimpleName().equals("boolean")) {
						methodHead = "is";
					} else {
						methodHead = "get";
					}
					Method setMethod = getEntityClass().getDeclaredMethod(m.getName().replaceFirst(methodHead, "set"),
							returnType);
					switch (attr.type()) {
					case Integer:
						setMethod.invoke(t, cursor.getInt(cursor.getColumnIndex(attr.name())));
						break;

					case Short:
						setMethod.invoke(t, cursor.getShort(cursor.getColumnIndex(attr.name())));
						break;

					case Long:
						setMethod.invoke(t, cursor.getLong(cursor.getColumnIndex(attr.name())));
						break;

					case Float:
						setMethod.invoke(t, cursor.getFloat(cursor.getColumnIndex(attr.name())));
						break;

					case Double:
						setMethod.invoke(t, cursor.getDouble(cursor.getColumnIndex(attr.name())));
						break;

					case Calendar:
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(attr.name())));
						setMethod.invoke(t, calendar);
						break;

					case Char:
					case Varchar:
						setMethod.invoke(t, cursor.getString(cursor.getColumnIndex(attr.name())));
						break;

					case Boolean:
						setMethod.invoke(t, cursor.getInt(cursor.getColumnIndex(attr.name())) == 0 ? false : true);
						break;

					default:
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public int update(T t) {
		int result = 0;

		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		try {
			Method[] methods = getEntityClass().getDeclaredMethods();
			for (Method m : methods) {
				Id id = m.getAnnotation(Id.class);
				if (id != null) {
					result = db.update(getTableName(), newContentValues(t), id.name() + "=?",
							new String[] { String.valueOf((Integer) (m.invoke(t))) });
					break;
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return result;
	}
}
