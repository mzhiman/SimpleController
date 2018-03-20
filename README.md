# SimpleController
## 一、简介
此项目模仿Struts框架，实现了类似struts框架的部分功能

1、框架拦截并解析浏览器URL，得到上下文信息和浏览器请求。框架根据上下文信息读取配置文件（XML文件）得到浏览器请求相关的类及方法，并利用Java反射机制及Java动态代理机制生成该类对象、调用该方法并返回结果。框架再次解析配置文件匹配返回的结果，生成结果视图返回给浏览器或者进一步处理。

2、框架利用Java动态代理实现了日志拦截器功能并将生成的日志输出到磁盘存储，解析来自浏览器请求，根据urL中对应的Action名，解析conteolle.xml文件并匹配对应的conteolle.xml中Action节点，在Action节点下寻找是否有interceptor-ref节点，如果有获得interceptor-ref节点名，并根据interceptor-ref节点名再次解析conteolle.xml文件寻找interceptor节点及对应的类信息及调用的方法。然后再回去解析conteolle.xml中Action节点对应的信息，利用代理将interceptor节点声明的方法及Action节点声明的方法组合在一起。

3、借助JAXP将本框架预定义的视图转换成标准HTML。JAXP中import javax.xml.transform.TransformerFactory;可以将自定义的xml文件通过对应的xsl文件转换成标准的HTML文件。

4、利用对象关系映射简化数据持久化工作，使用O/R maping后，无需再处理JDBC连接数据库以及编写sql语句等任务。SimpleController构造合适的类（数据结构）来存储从or_mapping.xml中解析得到的配置信息。

5、利用java.beans包中提供了内省机制，实现了依赖注入。对象不再用显示的new来创建，而是采用读取di.xml，然后采用依赖注入的方式
## 二、使用步骤
1、导入SimpleController.jar到项目中

2、导入依赖的Jar

3、编写Controller.xml（必须）

4、编写or_mapping.xml和di.xml(如果不需要，可省略这两个文件)

5、可以根据已有的xsl规则编写xml视图（可选）

**详细使用方法参考：SCDemo：https://github.com/SmartManMa/SCDemo**
