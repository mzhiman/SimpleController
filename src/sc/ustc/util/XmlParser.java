package sc.ustc.util;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParser {
	private static final String CTRL_NODE_NAME = "controller";
	private static final String INTERCEPTOR_NODE_TYPE = "interceptor";
	private static final String ACTION_NODE_NAME = "action";
	private static final String RESULT_NODE_NAME = "result";
	private static final String ATTR_NAME = "name";
	private static final String NONE_STR = "";
    private String XmlFilePath;
    
	public XmlParser(String XmlFilePath) {
		this.XmlFilePath = XmlFilePath;
	}

	public Element matchInterceptor (String interceptorName) throws DocumentException {
		//得到Document对象
		Document document = getDocument(XmlFilePath);
		//获取XML根节点
		Element root = document.getRootElement();
		return matchElement(INTERCEPTOR_NODE_TYPE ,interceptorName, root);
	}
	
	/** 
	  * matchAction TODO :基于DOM4j解析XML文件,匹配指定的actionName
	  * @param actionName xml文档中action name = "actionName"
	  * @return actionName所在节点
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 上午11:45:24 
	  */
	public Element matchAction(String actionName) throws DocumentException {
		//得到Document对象
		Document document = getDocument(XmlFilePath);
		//获取XML根节点
		Element root = document.getRootElement();
		//得到controller节点
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		
		return matchElement(ACTION_NODE_NAME ,actionName, elementCtrl);
	}
	
	
	/** 
	  * matchAction TODO :基于DOM4j解析XML文件,匹配指定的parentActionName结点下result name = resultName的result结点 并返回该节点
	  * @param parentActionName xml文档中action name = "actionName"
	  * @param resultName result name = resultName
	  * @return result name = resultName的result节点
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 上午11:45:24 
	  */
	public Element matchResult(String parentActionName, String resultName) throws DocumentException {
		//得到Document对象
		Document document = getDocument(XmlFilePath);
		//获取XML根节点
		Element root = document.getRootElement();
		//得到controller节点
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		//得到特定的action节点
		Element elementAction = matchElement( ACTION_NODE_NAME ,parentActionName, elementCtrl);
		
		return matchElement(RESULT_NODE_NAME ,resultName, elementAction);
	}
	
	
	/** 
	  * matchElement TODO :在parentElementType类型节点中，匹配名为elementName的elementType类型的element节点
	  * @param elementType 元素结点类型，比如sc-configuration action result 等
	  * @param elementName 元素结点的名字  《action name = "elementName"/》
	  * @param parentElementType 若想匹配result action是result的直接外层结点，parentElementType就是action
	  * @return 在parentElementType类型节点中，匹配名为elementName的elementType类型的element节点
	  * @author zhiman
	  * @date 2017/12/05 下午5:21:10 
	  */
	public Element matchElement (String elementType, String elementName, Element parentElement ) {
		return matchElement(elementType,ATTR_NAME,elementName,parentElement);
	}
	
	/** 
	  * matchElement TODO :重载方法
	  * @param elementType
	  * @param attrType
	  * @param elementName
	  * @param parentElement
	  * @return
	  * @author zhiman
	  * @date 2017/12/27 下午10:26:06 
	  */
	public static Element matchElement (String elementType,String attrType, String attrName, Element parentElement ) {
		Element matchedElement = null;
		//获取parentElement节点下所以直接结点列表
		List<Element> elementList = getElementNodeList(parentElement,elementType);
		//判断parentElement节点下是否有节点
		if ( elementList.size() == 0 ) {
			throw new RuntimeException(parentElement.getName()+"节点下面没有节点");
		}
		//遍历整个elementList
		for (Element element : elementList) {
			//去掉传入的elementName前后空格
			attrName = attrName.trim();
			//得到name属性对应的值
			String attrValue = getAttributeValue(element,attrType);
			//匹配
			if ( NONE_STR.equals( attrName ) ) {
				throw new RuntimeException("attrName不能为全空格");
			} else if ( attrName.equals( attrValue ) ) {
				matchedElement = element;
				break;
			}
		}
		//遍历完整个elementList,没有找到匹配elementName的element
		if (matchedElement == null) {
			throw new RuntimeException( attrName + "不能匹配到合适的" + elementType );
		}
		return matchedElement;
	}
	
	/** 
	  * getAttributeValue TODO :根据属性名，得到对应的属性值，如《action name = “actionName” type = "actionType"》《/action》
	  * @param element 元素结点,如：action
	  * @param attrName 属性名，如name，type
	  * @return 属性值 如：“actionName”，"actionType"
	  * @author zhiman
	  * @date 2017/12/03 上午9:58:45 
	  */
	public static String getAttributeValue( Element element , String attrName) {
		Attribute attribute = element.attribute(attrName.trim());
		if(attribute == null) {
			throw new RuntimeException("元素节点"+element.getName()+"中找不到");
		}
		return attribute.getValue().trim();
	}
	
	/** 
	  * getNodeContent TODO :返回《action》text《/action》中的text
	  * @param element 《action》节点
	  * @return 《action》text《/action》中的text
	  * @author zhiman
	  * @date 2017/12/03 下午12:40:56 
	  */
	public static String getNodeContent( Element element ) {
		return element.getTextTrim();
	}

	/** 
	  * getElementNodeList TODO : 获取nodeName指定类型的list 如《outter》《inner》 ...《/inner》《inner》 ...《/inner》...《/outter》
	  * @param root 当前节点的上一层节点,如《outter》
	  * @param elementType 元素结点类型 如：inner型
	  * @return elementType指定类型的list，包含《outter》...《/outter》中所有inner型节点
	  * @author zhiman
	  * @date 2017/12/03 上午12:57:26 
	  */
	@SuppressWarnings("unchecked")
	private static List<Element> getElementNodeList(Element root, String elementType) {
		return  root.elements(elementType);
	}

	/** 
	  * getDocument TODO :得到Document对象
	  * @param url XML文件路径
	  * @return Document对象
	  * @throws DocumentException
	  * @author zhiman
	  * @date 2017/12/03 上午12:40:24 
	  */
	private Document getDocument(String url) throws DocumentException {
		 SAXReader reader = new SAXReader();
	     Document document = reader.read(url);
	     return document;
	}
}
