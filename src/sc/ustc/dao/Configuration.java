package sc.ustc.dao;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sc.ustc.bean.ClassBean;
import sc.ustc.bean.PropertyBean;

/**
 * @description Configuration.java用来解析or_mapping.xml获取特定信息
 * @author Administrator
 * @date 2017/12/26
 * @version
 */
public class Configuration {
	private static final String CONFIG_FILE_NAME = "or_mapping.xml";
	// 配置文件路径
	private String defaultPath;
	private Document document;
	private Element rootElement;

	public Configuration() {
		this.defaultPath = Configuration.class.getClassLoader().getResource(CONFIG_FILE_NAME).getPath();
		File file = new File(defaultPath);
		init(file);
	}

	public Configuration(String path) {
		File file = new File(path);
		init(file);
	}

	public Configuration(File file) {
		init(file);
	};

	private void init(File file) {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(file);
			rootElement = document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getElements TODO :解析or_mapping.xml得到数据库配置信息
	 * 
	 * @return 数据库配置信息Map
	 * @author zhiman
	 * @date 2017/12/26 上午10:54:29
	 */
	public Map<String, String> getDBConfigMap() {

		Element JDBCElement = rootElement.element("jdbc");
		@SuppressWarnings("unchecked")
		List<Element> propertyElementList = JDBCElement.elements();
		Map<String, String> JDBCConfig = new HashMap<String, String>();
		for (Element property : propertyElementList) {
			// property.attribute("name").getValue();
			String name = property.element("name").getTextTrim();
			String value = property.element("value").getTextTrim();
			JDBCConfig.put(name, value);
		}
		return JDBCConfig;
	}

	/**
	 * getClassBeanSet TODO :解析or_mapping.xml得到，得到元素《class》结点下的信息
	 * @return 解析or_mapping.xml得到ClassBean对象组成的List
	 * @author zhiman
	 * @date 2017/12/26 下午1:00:04
	 */
	public List<ClassBean> getClassBeanList() {
		// 存储解析or_mapping.xml得到ClassBean对象
		List<ClassBean> classBeanList = new LinkedList<ClassBean>();
		// 获取根节点下的所有class节点
		@SuppressWarnings("unchecked")
		List<Element> allClassList = rootElement.elements("class");
		for (Element clsNode : allClassList) {
			ClassBean classBean = new ClassBean();
			classBean.setBeanName(clsNode.element("name").getTextTrim());
			classBean.setTableName(clsNode.element("table").getTextTrim());
			// 存储class节点下解析出来所有PropertyBean对象
			List<PropertyBean> propertyList = new LinkedList<PropertyBean>();
			// 得到class节点下所有property节点
			@SuppressWarnings("unchecked")
			List<Element> allPropertyList = clsNode.elements("property");
			// 得到property节点下的信息
			for (Element propertyNode : allPropertyList) {
				PropertyBean propertyBean = new PropertyBean();
				propertyBean.setFiledName(propertyNode.element("name").getTextTrim());
				propertyBean.setColumnName(propertyNode.element("column").getTextTrim());
				propertyBean.setType(propertyNode.element("type").getTextTrim());
				propertyBean.setLazy(propertyNode.element("lazy").getTextTrim());
				propertyList.add(propertyBean);
			}
			classBean.setPropertyList(propertyList);
			classBeanList.add(classBean);
		}
		return classBeanList;
	}
}
