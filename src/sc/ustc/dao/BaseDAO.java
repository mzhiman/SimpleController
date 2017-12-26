package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	
	protected String deriver;
	protected String url;
	protected String userName;
	protected String password;
	
	protected void setDeriver(String deriver) {
		this.deriver = deriver;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
	protected void setUserName(String userName) {
		this.userName = userName;
	}
	
	protected void setPassword(String password) {
		this.password = password;
	}
	
	//增删改查
	public abstract Object query(String sql);
	
	public abstract boolean insert(String sql);
	
	public abstract boolean update(String sql);
	
	public abstract boolean delete(String sql);
	
	/** 
	  * openDBConnection TODO :连接指定数据库
	  * @return connection连接
	  * @throws ClassNotFoundException
	  * @throws SQLException
	  * @author zhiman
	  * @date 2017/12/23 下午3:46:27 
	  */
	public Connection openDBConnection() throws ClassNotFoundException, SQLException{
		try {
			//通过反射机制获得jdbc驱动
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver+"对应的JDBC驱动没有找到！");
		}
		return DriverManager.getConnection(url,userName,password);
	}
	
	/** 
	  * closeDBConnection TODO :关闭数据库连接
	  * @param connection connection连接
	  * @return boolean true or false
	  * @throws SQLException
	  * @author zhiman
	  * @date 2017/12/23 下午3:59:26 
	  */
	public boolean closeDBConnection(Connection connection) throws SQLException{
		if ( connection != null ){
			connection.close();
		}
		return connection.isClosed();
	}


}
