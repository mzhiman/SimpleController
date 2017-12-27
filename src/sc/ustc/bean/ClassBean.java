package sc.ustc.bean;

import java.util.List;

public class ClassBean {
	private String beanName;
	private String tableName;
	//private String IdName;
	private List<PropertyBean> propertyList;
	
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
//	public String getIdName() {
//		return IdName;
//	}
//	public void setIdName(String idName) {
//		IdName = idName;
//	}
	public List<PropertyBean> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<PropertyBean> propertyList) {
		this.propertyList = propertyList;
	}

	
}
