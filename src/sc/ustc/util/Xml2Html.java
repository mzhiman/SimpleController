package sc.ustc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sc.ustc.controller.SimpleController;

public class Xml2Html {
	/** 
	  * translateXml2Html TODO :
	  * @param file
	  * @return
	  * @author zhiman
	  * @date 2017/12/19 下午8:02:34 
	  */
	public static String translateXml2Html(File file) {
		String path = SimpleController.class.getClassLoader().getResource("../../").getPath() + "Pages/translator.xsl";
		File xslFile = new File(path);
		Document document = null;
		Document doc = null;
		try {
			document = new SAXReader().read(file);
			doc = styleDocument(document,xslFile);
		} catch (Exception e) {
			if (document == null)
				throw new RuntimeException(file.toString() + "不存在");
		}
		String html = writeHtml(doc);
		System.out.println(html);
		//writeXml(doc, file);
		return html;
		
	}

	/** 
	  * writeXml TODO :
	  * @param document
	  * @param file
	  * @author zhiman
	  * @date 2017/12/19 下午8:02:29 
	  */
	public static void writeXml(Document document, File file) {
		OutputFormat format = OutputFormat.createPrettyPrint();// 格式
		format.setEncoding(document.getXMLEncoding());
		// 解决声明下空行问题
		format.setNewLineAfterDeclaration(false);
		// 4.设置编码格式
		// format.setEncoding("utf-8");
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(file), format);
			xmlWriter.write(document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	 /** 
	  * writeHtml TODO :将HTML document转换成 字符串
	  * @param transformDoc
	  * @return
	  * @author zhiman
	  * @date 2017/12/19 下午8:02:16 
	  */
	private static String writeHtml(Document transformDoc){
		 	//将HTML写成字符串流
	        StringWriter strWriter = new StringWriter();
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding(transformDoc.getXMLEncoding());
	        format.setXHTML(true);
	        HTMLWriter htmlWriter = new HTMLWriter(strWriter,format);
	        format.setExpandEmptyElements(false);
	        try {
	            htmlWriter.write(transformDoc);
	            htmlWriter.flush();
	        } catch (IOException e) {
	        }
	        return strWriter.toString();
	    }
	/** 
	  * styleDocument TODO :将document按照xsl格式转换成HTML document
	  * @param document
	  * @param file
	  * @return
	  * @throws Exception
	  * @author zhiman
	  * @date 2017/12/19 下午8:02:22 
	  */
	public static Document styleDocument(Document document,File file) throws Exception {
		//使用JAXP加载xstl
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(file));
		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);
		//返回转换过的文档
		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}
}
