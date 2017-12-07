package sc.ustc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.scinterface.Action;
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
//		String html = getHtml();
//		//设置响应HTML文件的编码格式
//		response.setContentType("text/html; charset=UTF-8");
//		//响应请求并返回html
//		response.getWriter().print(html);
		//测试三
		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		parser = new XmlParser(xmlPath);
//      String xmlPath = this.getServletContext().getRealPath(XML_FILE_PATH);		
//		System.out.println("方法一"+xmlPath);
//		//测试二
//		System.out.println(this.getServletContext().getRealPath("方法er"+"/"));
//		
//		System.out.println(pp);
		
		String urlString=request.getRequestURI();
		String path =  request.getRequestURI() ;
		String actionName = path.substring(urlString.lastIndexOf("/")+1,urlString.indexOf(".sc"));;
		System.out.println(actionName);
		//得到actionName对应的类
		Action action = loadActionClass (actionName);
		//转去执行action.execute
		
		String resultName = action.execute(request, response);
		//处理执行结果
		String result = handleResult(actionName,resultName);
		response.sendRedirect(result);

	}
	private String handleResult(String parentActionName,String resultName) {
		Element element = null;
		try {
			element = parser.matchResult(parentActionName, resultName);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("handleResult出错");
		}
		return parser.getAttributeValue(element, "value");
	}

	/** 
	  * loadActionClass TODO :加载对应的Action类
	  * @param actionName
	  * @return
	  * @author zhiman
	  * @date 2017/12/05 下午10:19:29 
	  */
	private Action loadActionClass (String actionName) {
		Action action = null;
		Element element = null;
		try {
			element = parser.matchAction(actionName);
			
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("loadActionClass出错");
		}
		if ( element == null ) {
			throw new RuntimeException(actionName+"匹配失败！");
		}
		String className = parser.getAttributeValue(element, "class");
		//利用Java反射加载该类
		
		try {
			Class<?> cls = Class.forName(className);
			action = (Action) cls.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("找不到类："+className);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return action;
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	/** 
	  * getHtml TODO :创建并返回的HTML
	  * @return HTML
	  * @author zhiman
	  * @date 2017/11/20 下午8:10:04 
	  */
//	private String getHtml() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<html>");
//		sb.append("<head>");
//		sb.append("<title>SimpleController</title>");
//		sb.append("<body>欢迎使用SimpleController！</body>");
//		sb.append("</head>");
//		sb.append("</html>");
//		return sb.toString();
//	}
}
