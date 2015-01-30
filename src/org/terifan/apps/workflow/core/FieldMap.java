package org.terifan.apps.workflow.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Deprecated
public class FieldMap implements Iterable<String>
{
	private final static int DEFAULT_EXCLUDES = Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL;
	private final static int PACKAGE_MODIFIER_CHECK = Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC;

	private Object mValueObject;
	private int mExcludeModifiers;
	private ArrayList<Class> mExcludeAnnotations;
	private ArrayList<Class> mIncludeAnnotations;


	/**
	 * Create a new FieldMap that will exclude static, transient and final fields.
	 *
	 * @param aValueObject
	 *   the object whose fields will be mapped.
	 */
	public FieldMap(Object aValueObject)
	{
		this(aValueObject, true);
	}


	/**
	 * Create a new FieldMap.
	 *
	 * @param aValueObject
	 *   the object whose fields will be mapped.
	 * @param aExcludeTransientFields
	 *   if true FieldMap will exclude static, transient and final fields.
	 */
	public FieldMap(Object aValueObject, boolean aExcludeTransientFields)
	{
		if (aValueObject == null)
		{
			throw new IllegalArgumentException("aValueObject is null.");
		}

		mIncludeAnnotations = new ArrayList<>();
		mExcludeAnnotations = new ArrayList<>();

		mValueObject = aValueObject;
		mExcludeModifiers = aExcludeTransientFields ? DEFAULT_EXCLUDES : 0;
	}


	public Object getValueObject()
	{
		return mValueObject;
	}


	public FieldMap setValueObject(Object aValueObject)
	{
		mValueObject = aValueObject;
		return this;
	}


	public int getExcludeModifiers()
	{
		return mExcludeModifiers;
	}


	/**
	 * Sets the field modifiers to be excluded.
	 *
	 * @param aExcludeModifiers
	 *   modifiers to exclude such as Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL
	 */
	public FieldMap setExcludeModifiers(int aExcludeModifiers)
	{
		mExcludeModifiers = aExcludeModifiers;
		return this;
	}


	public Class[] getExcludeAnnotations()
	{
		return mExcludeAnnotations.toArray(new Class[mExcludeAnnotations.size()]);
	}


	public FieldMap setExcludeAnnotations(Class... aExcludeAnnotations)
	{
		mExcludeAnnotations.clear();
		mExcludeAnnotations.addAll(Arrays.asList(aExcludeAnnotations));
		return this;
	}


	public void clearExcludeAnnotations()
	{
		mExcludeAnnotations.clear();
	}


	public Class[] getIncludeAnnotations()
	{
		return mIncludeAnnotations.toArray(new Class[mIncludeAnnotations.size()]);
	}


	public FieldMap setIncludeAnnotations(Class... aIncludeAnnotations)
	{
		mIncludeAnnotations.clear();
		mIncludeAnnotations.addAll(Arrays.asList(aIncludeAnnotations));
		return this;
	}


	public void clearIncludeAnnotations()
	{
		mIncludeAnnotations.clear();
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


	public Class getType(String aField)
	{
		return getField(aField).getType();
	}


	public int getModifiers(String aField)
	{
		return getField(aField).getModifiers();
	}


	public String getModifiersDescription(String aField)
	{
		int i = getField(aField).getModifiers();
		String s = "";
		if (Modifier.isAbstract(i))
		{
			s += "abstract ";
		}
		if (Modifier.isFinal(i))
		{
			s += "final ";
		}
		if (Modifier.isInterface(i))
		{
			s += "interface ";
		}
		if (Modifier.isNative(i))
		{
			s += "native ";
		}
		if (Modifier.isPrivate(i))
		{
			s += "private ";
		}
		if (Modifier.isProtected(i))
		{
			s += "private ";
		}
		if (Modifier.isPublic(i))
		{
			s += "public ";
		}
		if (Modifier.isStatic(i))
		{
			s += "static ";
		}
		if (Modifier.isStrict(i))
		{
			s += "strict ";
		}
		if (Modifier.isSynchronized(i))
		{
			s += "synchronized ";
		}
		if (Modifier.isTransient(i))
		{
			s += "transient ";
		}
		if (Modifier.isVolatile(i))
		{
			s += "volatile ";
		}
		return s.trim();
	}


	public boolean isPrimitive(String aField)
	{
		return getField(aField).getType().isPrimitive();
	}


	public boolean getBoolean(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return method.invoke(mValueObject) == Boolean.TRUE;
			}
			else if (field.getType().isPrimitive())
			{
				return field.getBoolean(mValueObject);
			}
			else
			{
				return field.get(mValueObject) == Boolean.TRUE;
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putBoolean(String aField, boolean aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setBoolean(mValueObject, aValue);
			}
			else
			{
				field.set(mValueObject, aValue ? Boolean.TRUE : Boolean.FALSE);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public byte getByte(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Byte) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getByte(mValueObject);
			}
			else
			{
				return (Byte) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putByte(String aField, int aValue)
	{
		putByte(aField, (byte) aValue);
	}


	public void putByte(String aField, byte aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setByte(mValueObject, aValue);
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


	public short getShort(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Short) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getShort(mValueObject);
			}
			else
			{
				return (Short) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putShort(String aField, int aValue)
	{
		putShort(aField, (short) aValue);
	}


	public void putShort(String aField, short aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setShort(mValueObject, aValue);
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


	public char getChar(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Character) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getChar(mValueObject);
			}
			else
			{
				return (Character) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putChar(String aField, int aValue)
	{
		putChar(aField, (char) aValue);
	}


	public void putChar(String aField, char aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setChar(mValueObject, aValue);
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


	public int getInt(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Integer) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getInt(mValueObject);
			}
			else
			{
				return (Integer) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putInt(String aField, int aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setInt(mValueObject, aValue);
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


	public long getLong(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Long) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getLong(mValueObject);
			}
			else
			{
				return (Long) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putLong(String aField, long aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setLong(mValueObject, aValue);
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


	public float getFloat(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Float) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getFloat(mValueObject);
			}
			else
			{
				return (Float) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putFloat(String aField, double aValue)
	{
		putFloat(aField, (float) aValue);
	}


	public void putFloat(String aField, float aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setFloat(mValueObject, aValue);
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


	public double getDouble(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (Double) method.invoke(mValueObject);
			}
			else if (field.getType().isPrimitive())
			{
				return field.getDouble(mValueObject);
			}
			else
			{
				return (Double) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putDouble(String aField, double aValue)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, aValue);
			}
			else if (field.getType().isPrimitive())
			{
				field.setDouble(mValueObject, aValue);
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


	public String getString(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return (String) method.invoke(mValueObject);
			}
			else
			{
				return (String) field.get(mValueObject);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void putString(String aField, String aValue)
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


	public void putNull(String aField)
	{
		Field field = getField(aField);
		if (field.getType().isPrimitive())
		{
			throw new IllegalArgumentException("Field " + aField + " is a primitive field and can not contain null value.");
		}
		try
		{
			Method method = getAccessor(field, "set");
			if (method != null)
			{
				method.invoke(mValueObject, new Object[]
						{
							null
						});
			}
			else
			{
				field.set(mValueObject, null);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}


	public boolean isNull(String aField)
	{
		Field field = getField(aField);
		try
		{
			Method method = getAccessor(field, "get");
			if (method != null)
			{
				return method.invoke(mValueObject) == null;
			}
			else
			{
				return field.get(mValueObject) == null;
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
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


	@Override
	public Iterator<String> iterator()
	{
		return Arrays.asList(getFieldNames()).iterator();
	}


	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append(mValueObject.getClass() + "\r\n{\r\n");
		for (String field : this)
		{
			s.append("\t" + toString(field) + "\r\n");
		}
		s.append("}");
		return s.toString();
	}


	public String toString(String aField)
	{
		String modifiers = getModifiersDescription(aField);
		String type = getType(aField).getSimpleName();
		String value = serializeValue(getObject(aField));

		return (modifiers + " " + type + " " + aField + " = " + value).trim() + ";";
	}


	private String serializeValue(Object aValue)
	{
		if (aValue == null)
		{
			return "null";
		}
		else if (aValue.getClass().isArray())
		{
			StringBuilder s = new StringBuilder();
			s.append("{");
			for (int i = 0, sz = Array.getLength(aValue); i < sz; i++)
			{
				if (i > 0)
				{
					s.append(", ");
				}
				s.append(serializeValue(Array.get(aValue, i)));
			}
			s.append("}");
			return s.toString();
		}
		else
		{
			String pre = "", suf = "";
			String type = aValue.getClass().getSimpleName();
			switch (type)
			{
				case "char":
				case "Character":
					pre = suf = "\'";
					break;
				case "String":
					pre = suf = "\"";
					break;
				case "float":
				case "Float":
					suf = "f";
					break;
				case "long":
				case "Long":
					suf = "L";
					break;
			}

			return pre + aValue + suf;
		}
	}


	protected boolean acceptField(Field aField)
	{
		int mod = aField.getModifiers();
		if ((mod & PACKAGE_MODIFIER_CHECK) == 0)
		{
			mod |= Modifier.PROTECTED;
		}
		if ((mod & mExcludeModifiers) != 0)
		{
			return false;
		}

		if (!mIncludeAnnotations.isEmpty())
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
//				Log.out.println("Failed to find in an 'include' annotation on field " + aField + ", " + mIncludeAnnotations);
				return false;
			}
		}

		for (Class an : mExcludeAnnotations)
		{
			if (aField.getAnnotation(an) != null)
			{
//				Log.out.println("Found an 'exclude' annotation on field " + aField + ", " + mExcludeAnnotations);
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


	public Method getAccessor(String aField, String aPrefix)
	{
		return getAccessor(getField(aField), aPrefix);
	}


	public Method getAccessor(Field aField, String aPrefix)
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


	public boolean isArray(String aField)
	{
		return getField(aField).getType().isArray();
	}


	public Class getComponentType(String aField)
	{
		if (isArray(aField))
		{
			return getType(aField).getComponentType();
		}
		else
		{
			return getType(aField);
		}
	}


	public Class getComponentBaseType(Field aField)
	{
		Class type = aField.getType();

		while (type.isArray())
		{
			type = type.getComponentType();
		}

		return type;
	}


	public String getTypeName(String aFieldName)
	{
		return getType(aFieldName).getSimpleName().replace("[]", "").replace("class [Ljava.lang.Object;","object");
	}


	public void put(Field aField, Object aValue)
	{
		put(aField.getName(), aValue);
	}


	public void put(String aField, Object aValue)
	{
		if (aValue == null || aValue.getClass().isArray())
		{
			putObject(aField, aValue);
		}
		else
		{
			Type type;

			try
			{
				type = Type.valueOf(getType(aField));
			}
			catch (IllegalArgumentException e)
			{
				try
				{
					Field field = getField(aField);
					field.set(mValueObject, aValue);
				}
				catch (IllegalAccessException | IllegalArgumentException ee)
				{
					throw new RuntimeException(ee);
				}

				return;
			}

			switch (type)
			{
				case BOOLEAN:
					putBoolean(aField, (Boolean)aValue);
					break;
				case BYTE:
					putByte(aField, (Byte)aValue);
					break;
				case SHORT:
					putShort(aField, (Short)aValue);
					break;
				case CHARACTER:
					putChar(aField, (Character)aValue);
					break;
				case INTEGER:
					putInt(aField, (Integer)aValue);
					break;
				case LONG:
					putLong(aField, (Long)aValue);
					break;
				case FLOAT:
					putFloat(aField, (Float)aValue);
					break;
				case DOUBLE:
					putDouble(aField, (Double)aValue);
					break;
			}
		}
	}


	public ArrayList<Field> getFields()
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


	public ArrayList<Method> getMethods()
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


	public enum Type
	{
		BOOLEAN(Boolean.TYPE),
		BYTE(Byte.TYPE),
		SHORT(Short.TYPE),
		CHARACTER(Character.TYPE),
		INTEGER(Integer.TYPE),
		LONG(Long.TYPE),
		FLOAT(Float.TYPE),
		DOUBLE(Double.TYPE),
		STRING(String.class);


		private final Class componentType;


		private Type(Class aComponentType)
		{
			componentType = aComponentType;
		}


		public Class getComponentType()
		{
			return componentType;
		}


		public static Type valueOf(Class aType)
		{
			if (aType == boolean.class || aType == Boolean.class)
			{
				return Type.BOOLEAN;
			}
			if (aType == byte.class || aType == Byte.class)
			{
				return Type.BYTE;
			}
			if (aType == short.class || aType == Short.class)
			{
				return Type.SHORT;
			}
			if (aType == char.class || aType == Character.class)
			{
				return Type.CHARACTER;
			}
			if (aType == int.class || aType == Integer.class)
			{
				return Type.INTEGER;
			}
			if (aType == long.class || aType == Long.class)
			{
				return Type.LONG;
			}
			if (aType == float.class || aType == Float.class)
			{
				return Type.FLOAT;
			}
			if (aType == double.class || aType == Double.class)
			{
				return Type.DOUBLE;
			}

			throw new IllegalArgumentException("" + aType);
		}
	}


	public String serializeToJava()
	{
		StringBuilder output = new StringBuilder();

		output.append(mValueObject.getClass().getName());
		output.append("\n{");
		serializeToJava(mValueObject, output, 1, new HashSet<>());
		output.append("\n}");

		return output.toString();
	}


	private static String serializeToJava(Object aObject, StringBuilder aOutput, int aIndent, HashSet<Object> aVisited)
	{
		FieldMap map = new FieldMap(aObject);
//		map.setExcludeModifiers(0);

		for (String field : map)
		{
			if (aOutput.length() > 0)
			{
				aOutput.append("\n");
			}

			indent(aOutput, aIndent);

			aOutput.append(map.getComponentBaseType(map.getField(field)).getSimpleName());

			if (map.isArray(field))
			{
				for (int i = getDimensionCount(map.getType(field)); --i >= 0;)
				{
					aOutput.append("[]");
				}
			}

			aOutput.append(" ");
			aOutput.append(field);
			aOutput.append(" = ");

			Object v = map.getObject(field);

			if (v == null || map.isPrimitive(field))
			{
				aOutput.append(v);
				aOutput.append(";");
			}
			else if (v instanceof String)
			{
				aOutput.append("\"");
				aOutput.append(v.toString().replace("\"","\\\""));
				aOutput.append("\";");
			}
			else if (aVisited.add(v))
			{
				serializeToJavaImpl(v, aOutput, aIndent, aVisited);
			}
			else
			{
				aOutput.append("!#circ;");
			}
		}

		return aOutput.toString();
	}


	private static void serializeToJavaImpl(Object aObject, StringBuilder aOutput, int aIndent, HashSet<Object> aVisited)
	{
		if (aObject == null || aObject.getClass().isPrimitive())
		{
			aOutput.append(aObject);
			aOutput.append(";");
		}
		else
		{
//			aOutput.append(aObject.getClass().getName());
			aOutput.append("\n");
			indent(aOutput, aIndent);
			aOutput.append("{");
			if (aObject.getClass().isArray())
			{
				for (int i = 0, sz = Array.getLength(aObject); i < sz; i++)
				{
					if (i > 0)
					{
						aOutput.append("\n");
						indent(aOutput, aIndent);
						aOutput.append("},");
						aOutput.append("\n");
						indent(aOutput, aIndent);
						aOutput.append("{");
					}
					serializeToJava(Array.get(aObject, i), aOutput, aIndent+1, aVisited);
				}
			}
			else if (aObject instanceof List)
			{
				List list = (List)aObject;
				for (int i = 0; i < list.size(); i++)
				{
					if (i > 0)
					{
						aOutput.append(", ");
					}
					serializeToJava(list.get(i), aOutput, aIndent, aVisited);
				}
			}
			else if (aObject instanceof Set)
			{
				Set set = (Set)aObject;
				int i = 0;
				for (Object o : set)
				{
					if (i > 0)
					{
						aOutput.append(", ");
					}
					serializeToJava(o, aOutput, aIndent, aVisited);
					i++;
				}
			}
			else if (aObject instanceof Map)
			{
				Map map = (Map)aObject;
				Object[] keys = map.keySet().toArray();
				for (int i = 0; i < map.size(); i++)
				{
					if (i > 0)
					{
						aOutput.append(", ");
					}
					serializeToJava(keys[i], aOutput, aIndent, aVisited);
					aOutput.append("=");
					serializeToJava(map.get(keys[i]), aOutput, aIndent, aVisited);
				}
			}
			else
			{
				serializeToJava(aObject, aOutput, aIndent+1, aVisited);
			}
			aOutput.append("\n");
			indent(aOutput, aIndent);
			aOutput.append("}");
		}
	}


	private static int getDimensionCount(Class aArray)
	{
		int depth = 0;
		Class nextClass = aArray.getComponentType();
		while (nextClass != null)
		{
			nextClass = nextClass.getComponentType();
			depth++;
		}
		return depth;
	}


	private static void indent(StringBuilder aOutput, int aIndent)
	{
		for (int i = 0; i < aIndent; i++)
		{
			aOutput.append("   ");
		}
	}
}
