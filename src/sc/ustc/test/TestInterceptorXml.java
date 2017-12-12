/*package sc.ustc.test;

import org.dom4j.Element;

import sc.ustc.util.InterceptorNodeXmlParser;
import sc.ustc.util.XmlParser;

public class TestInterceptorXml {
	private static String XmlFilePath = "src/controller.xml";
	public static void main(String[] args) {
		XmlParser xmlParser = new XmlParser(XmlFilePath);
		InterceptorNodeXmlParser interceptor = new InterceptorNodeXmlParser(xmlParser,"login");
		Element element = interceptor.getInterceptorElement("log");
		String str = XmlParser.getAttributeValue(element, "predo");
		System.out.println(str);
	}
}
*/