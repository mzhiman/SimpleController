package sc.ustc.dao;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import sc.ustc.bean.ClassBean;
import sc.ustc.bean.PropertyBean;

public class Conversion {
	/** 
	  * getObject TODO :
	  * @param t
	  * @return
	  * @author zhiman
	  * @date 2017/12/26 下午3:44:57 
	  */
	public static  Object getObject(Object obj){
		Class<?> cls = obj.getClass();
		Configuration config = new Configuration();
		ClassBean clsBean = null;
		List<ClassBean> classBeanList = config.getClassBeanList();
		//找到映射的UserBean
		for ( ClassBean cb : classBeanList ) {
			System.out.println(cb.getBeanName());
			System.out.println(cls.getName());
			String str = cls.getName();
			String clsname = str.substring(str.lastIndexOf(".")+1);
			System.out.println(clsname);
			if ( cb.getBeanName().equals( clsname ) ) {
				clsBean = cb;
				break;
			}
		}
		if ( clsBean == null ) {
			System.out.println("映射错误");
			return null;
		}
		//从映射关系中得到使用的表
		String table = clsBean.getTableName();
		System.out.println(table);
		//传入的对象所有的成员变量名都在filedList中
		List<PropertyBean> propertyList = clsBean.getPropertyList();
		//存储对应的成员变量及其值
		Map<String,String> map = new HashMap<String, String>();
		String columnName = null;
		for (PropertyBean property : propertyList) {
			//得到fieldName对应的get方法
			String fieldName =  property.getFiledName();
			System.out.println(fieldName);
			char ch = fieldName.charAt(0);
			String rear = fieldName.replace(ch, Character.toUpperCase(ch));
			String methodName = "get"+rear;
			System.out.println(methodName);
			//得到成员变量类型
			String type = property.getType();
			String result = null;
			//根据方法名及参数找到对应的get方法
			try {
				Method method = cls.getMethod(methodName);
				if ( type instanceof String) {
					//类型转换,得到fieldName成员变量的值
					result = (String) method.invoke(obj);
					//如果变量没有被初始化
					if ( result == null ) {
						continue;
					} else {
						columnName = property.getColumnName();
						map.put(columnName, result);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String querySql = null;
		if (map.size() != 0) {
			Set< Entry< String,String> > ss = map.entrySet();
			for (Entry< String,String> entry : ss) {
				StringBuilder sb = new StringBuilder("select * from ");
				sb.append(table);
				sb.append(" where ");
				sb.append(entry.getKey());
				sb.append("=");
				sb.append("\"");
				sb.append(entry.getValue());
				sb.append("\";");
				querySql = sb.toString();
				System.out.println(querySql);
			}
		}
		return new BaseDAO().query(querySql);
	}
}
