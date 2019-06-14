package org.terifan.workflow.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;


public abstract class Executable
{
	private String mIdentity;
	private AbstractActivity mActivity;


	public abstract void run();


	public void setActivity(AbstractActivity aActivity)
	{
		mActivity = aActivity;
	}


	public AbstractActivity getActivity()
	{
		return mActivity;
	}


	public void setIdentity(String aIdentity)
	{
		mIdentity = aIdentity;
	}


	public String getIdentity()
	{
		return mIdentity;
	}


	public void pushState(WorkflowEngine aWorkflowEngine, State aState)
	{
		FieldMap map = new FieldMap(this, In.class, Out.class, InOut.class);

		for (String name : map.getFieldNames())
		{
			Field field = map.getField(name);
			In in = field.getAnnotation(In.class);
			InOut inOut = field.getAnnotation(InOut.class);

			if (in != null || inOut != null)
			{
				String alias = name;
				if (in != null && in.value() != null && !in.value().isEmpty())
				{
					alias = in.value();
				}
				if (inOut != null && inOut.value() != null && !inOut.value().isEmpty())
				{
					alias = inOut.value();
				}

				System.out.println("Setting field \"" + name + "\" to property \"" + alias + "\" = \"" + aState.get(alias) + "\"");

				map.putObject(name, aState.get(alias));
			}
		}
	}


	public void popState(WorkflowEngine aWorkflowEngine, State aState)
	{
		FieldMap map = new FieldMap(this, In.class, Out.class, InOut.class);

		for (String name : map.getFieldNames())
		{
			Field field = map.getField(name);
			Out out = field.getAnnotation(Out.class);
			InOut inOut = field.getAnnotation(InOut.class);

			if (out != null || inOut != null)
			{
				String alias = name;
				if (out != null && out.value() != null && !out.value().isEmpty())
				{
					alias = out.value();
				}
				if (inOut != null && inOut.value() != null && !inOut.value().isEmpty())
				{
					alias = inOut.value();
				}

				System.out.println("Setting property \"" + alias + "\" to field \"" + name + "\" = \"" + map.getObject(name) + "\"");

				aState.put(alias, map.getObject(name));
			}
		}
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Name
	{
		String value();
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface In
	{
		String value() default "";
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Out
	{
		String value() default "";
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface InOut
	{
		String value() default "";
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Scope
	{
		String value() default "";
	}
}