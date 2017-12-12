package sc.ustc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassReflector {
	/** 
	  * gainClass TODO :根据类名得到Class对象
	  * @param className 类名
	  * @return Class对象
	  * @throws ClassNotFoundException
	  * @author zhiman
	  * @date 2017/12/07 下午11:40:31 
	  */
	public static Class<?> gainClass(String className) throws ClassNotFoundException {
		Class<?> cls = Class.forName(className);
		return cls;
	}
	
	/** 
	  * executeMethod TODO : 利用Reflect执行无参且返回字符串的方法
	  * @param cls Class对象
	  * @param method 方法
	  * @return 方法返回的字符串
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 下午11:41:17 
	  */
	public static String executeMethod(Class<?> cls, Method method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		String result = null;
		Object obj = method.invoke(cls.newInstance());
		if (obj instanceof String) {
			result = (String)obj;
		} 
		return result;
	}
	/** 
	  * executeMethod TODO :利用Reflect执行无参且返回字符串的方法
	  * @param cls Class对象
	  * @param methodName 方法名
	  * @return 被执行的方法返回的字符串
	  * @throws NoSuchMethodException
	  * @throws SecurityException
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 下午11:43:43 
	  */
	public static String executeMethod(Class<?> cls,String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method method = cls.getMethod(methodName);
		return executeMethod(cls, method);
	}
	/** 
	  * executeMethod TODO :
	  * @param className
	  * @param methodName
	  * @return 被执行的方法返回的字符串
	  * @throws ClassNotFoundException
	  * @throws NoSuchMethodException
	  * @throws SecurityException
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 下午11:44:49 
	  */
	public static String executeMethod(String className,String methodName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> cls = gainClass(className);
//		//测试*************start
//		Field[] field = cls.getDeclaredFields();
//		for (Field f : field) {
//			System.out.println( f.getName() +"**********"+ f.getType().getName() );
//		}
//		//测试*************end
		
		return executeMethod(cls,methodName);
	}
	
	public static Field[] matchFields (String className) throws ClassNotFoundException {
		Class<?> cls = gainClass(className);
		return cls.getDeclaredFields();
	}
	public static Field[] matchFields (Class<?> cls) {
		return cls.getDeclaredFields();
	}
}
