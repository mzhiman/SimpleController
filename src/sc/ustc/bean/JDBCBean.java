package sc.ustc.bean;

/** 
  * @description JDBCBean.java
  * @author Administrator
  * @date 2017/12/26
  * @version
  */
public class JDBCBean {
	//数据库基本配置信息
	private String driver_class;
	private String url_path;
	private String db_username;
	private String db_userpassword;
	
	public String getDriver_class() {
		return driver_class;
	}
	public void setDriver_class(String driver_class) {
		this.driver_class = driver_class;
	}
	public String getUrl_path() {
		return url_path;
	}
	public void setUrl_path(String url_path) {
		this.url_path = url_path;
	}
	public String getDb_username() {
		return db_username;
	}
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}
	public String getDb_userpassword() {
		return db_userpassword;
	}
	public void setDb_userpassword(String db_userpassword) {
		this.db_userpassword = db_userpassword;
	}
	
}
