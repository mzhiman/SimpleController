package sc.ustc.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sc.ustc.interceptor.ActionsExecutor;
import sc.ustc.interceptor.ActoinProxy;
import sc.ustc.interceptor.Executor;
import sc.ustc.util.Xml2Html;
import sc.ustc.util.XmlParser;

/**
 * Controller，SimpleController的核心
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		parser = new XmlParser(xmlPath);
		String actionName = getActionName(request);
		System.out.println(actionName);
		String result = ProxyImplAssistant(actionName, request, response);
		String html = translateResult(result);
		if (html != null) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(html);
		} else {
			response.sendRedirect(result);
		}
	}

	private String translateResult(String result) {
		String rear = "_view.xml";
		result = result.trim();
		if (result.endsWith(rear)) {
			// 获得result对应的文件路径
			String path = SimpleController.class.getClassLoader().getResource("../../").getPath();
			String xslFilePath = path + result;
			// 创建特殊后缀Html文件
			File file = new File(xslFilePath);
			return Xml2Html.translateXml2Html(file);
		} else {
			return null;
		}
	}

	/**
	 * ProxyImplAssistant TODO : Java动态代理
	 * @param actionName
	 * @return
	 * @author zhiman
	 * @date 2017/12/12 上午11:11:51
	 */
	private String ProxyImplAssistant(String actionName, Object... args) {
		Executor executor = new ActionsExecutor(parser, actionName);
		ActoinProxy h = new ActoinProxy(executor, parser, actionName);
		Class<?> cls = executor.getClass();
		Executor proxy = (Executor) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), h);
		String result = proxy.executeAction(args);
		return result;
	}

	/**
	 * getActionName TODO :根据浏览器请求获取对应的Action
	 * 
	 * @param request
	 * @return
	 * @author zhiman
	 * @date 2017/12/07 下午11:21:08
	 */
	private String getActionName(HttpServletRequest request) {
		String urlString = request.getRequestURI();
		return urlString.substring(urlString.lastIndexOf("/") + 1, urlString.indexOf(".sc"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
