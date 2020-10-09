package com.efuture.wechat.common;



import org.springframework.beans.factory.BeanFactory;

public class SpringBeanFactory
{
	public static Object getBean(String name)
	{
		return SpringContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> clazz)
	{
		return (T) SpringContext.getBean(name, clazz);
	}
	
	public static BeanFactory getApplicationContext() {
		return SpringContext.getContext();
	}
}
