package sc.ustc.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;

import sc.ustc.interceptor.ActionsExecutor;
import sc.ustc.interceptor.ActoinProxy;
import sc.ustc.interceptor.Executor;
import sc.ustc.util.Xml2Html;
//import sc.ustc.util.ClassReflector;
import sc.ustc.util.XmlParser;


/** 
  * @description SimpleController.java
  * @author Administrator
  * @date 2017/11/20
  * @version 1.0
  */
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1303998807532328377L;
	private static final String XML_FILE_NAME = "controller.xml";
	private XmlParser parser;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		parser = new XmlParser(xmlPath);
		String actionName = getActionName(request);
		System.out.println(actionName);
		//这一步应该被代理
//		String resultName = loadActionClassAndRunMethod(actionName);
//		//处理执行结果，这一步也应该被代理
//		String result = handleResult(actionName,resultName);
		//Test start****
		String result = ProxyImplAssistant(actionName);
		//Test end*******
		System.out.println("****************************"+result);
		//判断result是否以"_view.xml"结尾，若是，则转成Html
		String html = translateResult(result);
		if ( html != null) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(html);
		} else {
			response.sendRedirect(result);
		}
	}
	private String translateResult(String result) {
		String rear = "_view.xml";
		result = result.trim();
		if ( result.endsWith( rear ) ) {
			//获得result对应的文件路径
			String path = SimpleController.class.getClassLoader().getResource("../../").getPath();
			String xslFilePath = path + result;
			//创建特殊后缀Html文件
			System.out.println(xslFilePath+"****************************"+path);
			File file = new File(xslFilePath);
			return Xml2Html.translateXml2Html(file);
		} else {
			return null;
		}
		
	}
	/** 
	  * ProxyImplAssistant TODO :
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/12 上午11:11:51 
	  */
	private String ProxyImplAssistant (String actionName) {
		Executor executor = new ActionsExecutor(parser, actionName);
		ActoinProxy h = new ActoinProxy(executor, parser, actionName);
		Class<?> cls = executor.getClass();
		Executor proxy = (Executor) Proxy.newProxyInstance(cls.getClassLoader(), 
				cls.getInterfaces(), h);
		String result = proxy.executeAction();
		return result;
	}
	
	/** 
	  * getActionName TODO :根据浏览器请求获取对应的Action
	  * @param request
	  * @return
	  * @author zhiman
	  * @date 2017/12/07 下午11:21:08 
	  */
	private String getActionName(HttpServletRequest request) {
		String urlString=request.getRequestURI();
		//测试getParameterMap() start
//		Map<String, String[]> map = request.getParameterMap();
//		if (map.size() != 0) {
//			Set<Entry<String, String[]>> entrySet = map.entrySet();
//			for(Entry<String, String[]> entry : entrySet){
//				System.out.println(entry.getKey()+"***"+entry.getValue()[0]);
//			}
//		}
		//测试getParameterMap() end
		return urlString.substring(urlString.lastIndexOf("/")+1,urlString.indexOf(".sc"));
	}
//	private void setFieldValue( HttpServletRequest request, Class<?> cls ) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
//		//得到请求中参数名-参数值
//		Map<String, String[]> map = request.getParameterMap();
//		//得到Action中声明的所有成员变量
//		Field[] fields = cls.getDeclaredFields();
//		//如果请求中有参数名-参数值
//		if (map.size() != 0) {
//			Set<Entry<String, String[]>> entrySet = map.entrySet();
//			//遍历请求中参数名-参数值的Map
//			for(Entry<String, String[]> entry : entrySet){
//				//得到请求中的参数名
//				String paraName = entry.getKey().trim();
//				//按约定的格式解析object.filed解析参数名
//				int position = paraName.indexOf(".");
//				if (position != paraName.lastIndexOf(".") || position == -1 ) {
//					throw new RuntimeException("URL请求携带的参数名称不符合“object.filed”的规范");
//				}
//				String objName = paraName.substring(0,position);
//				String fieldName = paraName.substring(position+1);
//				System.out.println(paraName +"***"+entry.getValue()[0]+"---"+fieldName);
//				//在得到的Action中声明的所有成员变量中寻找与请求匹配的成员变量
//				for (Field field : fields) {
//					//得到成员变量类型
//					Class<?> beanClass = field.getType();
//					//得到成员变量类型名字
//					String QualifyObjType = beanClass.getName().trim();
//					String objType = QualifyObjType.substring(QualifyObjType.lastIndexOf(".") + 1);
//					//判断Action中声明的所有成员变量与请求中参数名 及类型是否匹配
//					if ( fieldName.equals( field.getName() ) && objName.equalsIgnoreCase(objType)) {
//						//如果匹配
//						field.set(cls.newInstance(), beanClass.newInstance());
//					}
//				}
//			}
//		}
//	}
	
//	private String handleResult(String parentActionName,String resultName) {
//		Element element = null;
//		try {
//			element = parser.matchResult(parentActionName, resultName);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			throw new RuntimeException("handleResult出错");
//		}
//		return parser.getAttributeValue(element, "value");
//	}

	/** 
	  * loadActionClass TODO :加载actionName对应的Action类,并执行对应的方法
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 下午10:19:29 
	  */
//	private String loadActionClassAndRunMethod (String actionName) {
//		Element element = null;
//		String result = null;
//		try {
//			element = parser.matchAction(actionName);
//			
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			throw new RuntimeException("loadActionClass出错");
//		}
//		if ( element == null ) {
//			throw new RuntimeException(actionName+"匹配失败！");
//		}
//		String className = parser.getAttributeValue(element, "class");
//		String methodName = parser.getAttributeValue(element, "method");
//		System.out.println(methodName);
//		//利用Java反射加载该类并执行对应的方法
//		try {
//			result = ClassReflector.executeMethod(className, methodName);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("找不到类："+className);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
