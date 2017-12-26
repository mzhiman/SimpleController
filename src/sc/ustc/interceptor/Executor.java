package sc.ustc.interceptor;

public interface Executor {
	/** 
	  * executeAction TODO : 抽象角色（动态代理只能代理接口）  ,所以创建接口
	  * @return
	  * @author zhiman
	  * @date 2017/12/12 上午9:48:09 
	  */
	public String executeAction(Object...args);
}
