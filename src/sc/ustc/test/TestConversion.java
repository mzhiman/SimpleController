package sc.ustc.test;


import java.sql.SQLException;

import javax.sql.RowSet;

import sc.ustc.bean.UserBean;
import sc.ustc.dao.Conversion;

public class TestConversion {
	public static void main(String[] args) {
		UserBean ub = new UserBean();
		ub.setUserID("17225250");
		RowSet rs = (RowSet) Conversion.getObject(ub);
		try {
			rs.next();
			String name = rs.getString("name");
			String pass = rs.getString("password");
			System.out.println(name+"***"+pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
