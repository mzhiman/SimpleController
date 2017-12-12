package sc.ustc.interceptor;

import java.lang.reflect.InvocationTargetException;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.util.ClassReflector;
import sc.ustc.util.XmlParser;

public class ActionsExecutor implements Executor{
	private XmlParser parser;
	private String actionName;
	
	public ActionsExecutor(XmlParser parser,String actionName) {
		this.actionName = actionName;
		this.parser = parser;
	}
	
	public String executeAction() {
		//这一步应该被代理
		String resultName = loadActionClassAndRunMethod(actionName);
		//处理执行结果，这一步也应该被代理
		String result = handleResult(actionName,resultName);
		//*****测试动态代理*****
		System.out.println("executeAction 被执行");
		//*****测试动态代理*****
		return result;
	}
	/** 
	  * loadActionClass TODO :加载actionName对应的Action类,并执行对应的方法
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 下午10:19:29 
	  */
	private String loadActionClassAndRunMethod (String actionName) {
		Element element = null;
		String result = null;
		try {
			element = parser.matchAction(actionName);
			
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("loadActionClass出错");
		}
		if ( element == null ) {
			throw new RuntimeException(actionName+"匹配失败！");
		}
		String className = XmlParser.getAttributeValue(element, "class");
		String methodName = XmlParser.getAttributeValue(element, "method");
		System.out.println(methodName);
		//利用Java反射加载该类并执行对应的方法
		try {
			result = ClassReflector.executeMethod(className, methodName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("找不到类："+className);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String handleResult(String parentActionName,String resultName) {
		Element element = null;
		try {
			element = parser.matchResult(parentActionName, resultName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("handleResult出错");
		}
		return XmlParser.getAttributeValue(element, "value");
	}
}
