package sc.ustc.scinterface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	//定义字符串常量
    public static final String SUCCESS="success";
    public static final String NONE="none";
    public static final String FAILUER="failuer";
    public static final String INPUT="input";
    public static final String LOGIN="login";
    //准备一个方法，用于获取数据
    public String execute(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException;   
}
