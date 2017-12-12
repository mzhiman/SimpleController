package sc.ustc.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.dom4j.Element;
import sc.ustc.util.ClassReflector;
import sc.ustc.util.InterceptorNodeXmlParser;
import sc.ustc.util.XmlParser;

public class ActoinProxy implements InvocationHandler {
	// 要代理的对象，这里代理action对象，应传入ActionsExecutor对象
	private Object object;
	private String actionName;
	private XmlParser parser;
	private String className;
	private Element element;

	public ActoinProxy(Object object, XmlParser parser, String actionName) {
		this.actionName = actionName;
		this.parser = parser;
		this.object = object;
		init();
	}

	private void init() {
		InterceptorNodeXmlParser interceptor = new InterceptorNodeXmlParser(parser, actionName);
		// 《interceptor name = "log"/》结点
		element = interceptor.getInterceptorElement("log");
		if (element != null) {
			className = XmlParser.getAttributeValue(element, "class");
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		preAction();
		Object result = method.invoke(object, args);
		afterAction();
		return result;
	}

	// *****测试动态代理*****
	private void preAction() {
		if (element != null && className != null) {
			String perDoMethod = XmlParser.getAttributeValue(element, "predo");
			try {
				ClassReflector.executeMethod(className, perDoMethod);
			} catch (Exception e) {
				System.out.println("ActoinProxy类出错");
				e.printStackTrace();
			}
		}
		//System.out.println("preAction 被执行");
	}

	private void afterAction() {
		if (element != null && className != null) {
			String afterDoMethod = XmlParser.getAttributeValue(element, "afterdo");
			try {
				//测试 start
				Thread.sleep(1000);
				//测试 end
				ClassReflector.executeMethod(className, afterDoMethod);
			} catch (Exception e) {
				System.out.println("ActoinProxy类出错");
				e.printStackTrace();
			}
		}
		//System.out.println("afterAction 被执行");
	}
	// *****测试动态代理*****

}
