package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public  class BaseDAO {

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

	// 增删改查
	public Object query(String sql) {
		CachedRowSet rowSet = null;
		try (	
				Connection c = getDBConnection(); 
				PreparedStatement ps = c.prepareStatement(sql);
			) {
			
			ResultSet rs = ps.executeQuery();
			
			//ResultSet随着数据库连接断开而无法使用，rowSet可以离线使用
			RowSetFactory factory = RowSetProvider.newFactory();  
			rowSet = factory.createCachedRowSet(); 
			rowSet.populate(rs);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return rowSet;
	};

	public boolean insert(String sql) {
		return false;
	};

	public boolean update(String sql) {
		return false;
	};

	public boolean delete(String sql) {
		return false;
	};

	// 增删改查重写
	public Object query(PreparedStatement ps) {
		return ps;
	};

	public boolean insert(PreparedStatement ps) {
		return false;
	};

	public boolean update(PreparedStatement ps) {
		return false;
	};

	public boolean delete(PreparedStatement ps) {
		return false;
	};

	/**
	 * openDBConnection TODO :连接指定数据库
	 * 
	 * @return connection连接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/23 下午3:46:27
	 */
	public Connection openDBConnection() throws ClassNotFoundException, SQLException {
		try {
			// 通过反射机制获得jdbc驱动
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver + "对应的JDBC驱动没有找到！");
		}
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * openDBConnection TODO :***从配置文件读取数库信息连接数据库
	 * 
	 * @param configMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/26 下午3:08:46
	 */
	public Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Map<String, String> configMap = new Configuration().getDBConfigMap();
		String deriver = configMap.get("driver_class");
		String url = configMap.get("url_path");
		String userName = configMap.get("db_username");
		String password = configMap.get("db_userpassword");
		try {
			// 通过反射机制获得jdbc驱动
			Class.forName(deriver);
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(deriver + "对应的JDBC驱动没有找到！");
		}
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * closeDBConnection TODO :关闭数据库连接
	 * 
	 * @param connection
	 *            connection连接
	 * @return boolean true or false
	 * @throws SQLException
	 * @author zhiman
	 * @date 2017/12/23 下午3:59:26
	 */
	public boolean closeDBConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
		return connection.isClosed();
	}

}
