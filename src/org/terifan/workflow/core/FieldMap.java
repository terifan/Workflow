package org.terifan.workflow.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;


class FieldMap
{
	private final static int EXCLUDE_MODIFIERS = Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL;
	private final static int PACKAGE_MODIFIER_CHECK = Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC;

	private Object mValueObject;
	private Class[] mIncludeAnnotations;


	public FieldMap(Object aValueObject, Class... aIncludeAnnotations)
	{
		if (aValueObject == null)
		{
			throw new IllegalArgumentException("aValueObject is null.");
		}

		mValueObject = aValueObject;
		mIncludeAnnotations = aIncludeAnnotations;
	}


	public String[] getFieldNames()
	{
		ArrayList<String> names = new ArrayList<>();

		for (Field field : getFields())
		{
			names.add(field.getName());
		}

		return names.toArray(new String[names.size()]);
	}


	public Object getObject(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return method.invoke(mValueObject);
			}
			else
			{
				field.setAccessible(true);
				return field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			return null;
//			throw new RuntimeException(e);
		}
	}


	public void putObject(String aField, Object aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else
			{
				field.set(mValueObject, aValue);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	private boolean acceptField(Field aField)
	{
		int mod = aField.getModifiers();
		if ((mod & PACKAGE_MODIFIER_CHECK) == 0)
		{
			mod |= Modifier.PROTECTED;
		}
		if ((mod & EXCLUDE_MODIFIERS) != 0)
		{
			return false;
		}

		if (mIncludeAnnotations.length != 0)
		{
			boolean found = false;
			for (Class an : mIncludeAnnotations)
			{
				if (aField.getAnnotation(an) != null)
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				return false;
			}
		}

		return true;
	}


	public Field getField(String aField)
	{
		boolean found = false;

		for (Field field : getFields())
		{
			if (field.getName().equals(aField))
			{
				found = true;
				if (acceptField(field))
				{
					field.setAccessible(true);
					return field;
				}
				break;
			}
		}

		if (found)
		{
			throw new IllegalArgumentException("Field was found but made unaccessible by either modifiers or annotation filters: name: " + aField + ", object: " + mValueObject);
		}
		else
		{
			throw new IllegalArgumentException("No '" + aField + "' field in object " + mValueObject);
		}
	}


	private Method getAccessor(Field aField, String aPrefix)
	{
		String name = aField.getName().substring(0, 1).toUpperCase() + aField.getName().substring(1);

		for (Method method : getMethods())
		{
			String methodName = method.getName();

			if (methodName.equals(aPrefix + name))
			{
				method.setAccessible(true);
				return method;
			}
		}

		return null;
	}


	private ArrayList<Field> getFields()
	{
		ArrayList<Field> list = new ArrayList<>();
		Class o = mValueObject.getClass();
		HashSet<String> names = new HashSet<>();
		while (o != null && o != Object.class)
		{
			for (Field f : o.getDeclaredFields())
			{
				if (f.getName().startsWith("this$")) continue;

				if (acceptField(f) && !names.contains(f.getName()))
				{
					list.add(f);
					names.add(f.getName());
				}
			}
			o = o.getSuperclass();
		}
		return list;
	}


	private ArrayList<Method> getMethods()
	{
		ArrayList<Method> list = new ArrayList<>();
		Class o = mValueObject.getClass();
		HashSet<String> names = new HashSet<>();
		while (o != null)
		{
			for (Method f : o.getDeclaredMethods())
			{
				if (!names.contains(f.getName()))
				{
					list.add(f);
					names.add(f.getName());
				}
			}
			o = o.getSuperclass();
		}
		return list;
	}
}
