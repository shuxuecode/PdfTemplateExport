package com.zsx.pdfTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Locale;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker模板生成PDF工具类
 * @author ZSX
 */
public class PDFTemplateUtil {

	/**
	 * classpath路径
	 */
	private String classpath = getClass().getResource("/").getPath();
	
	/**
	 * 指定FreeMarker模板文件的位置
	 */ 
	private String templatePath = "/pdf";
	
	/**
	 * freeMarker模板文件名称
	 */
	private String templateFileName = "pdf.ftl";
	
	/**
	 * 图片路径 —— 默认是classpath下面的images文件夹
	 */
	private String imagePath = "/images/";
	
	/**
	 * 字体资源文件 存放路径
	 */
	private String fontPath = "pdf/font/";
	
	/**
	 * 字体   [宋体][simsun.ttc]   [黑体][simhei.ttf]
	 */
	private String font = "simsun.ttc";
	
	/**
	 * 指定编码
	 */
	private String encoding = "UTF-8";
	

	/**
	 * 生成pdf
	 * @param data  传入到freemarker模板里的数据
	 * @param out   生成的pdf文件流
	 */
	public void createPDF(Object data, OutputStream out) {
		// 创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
		Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		// 指定FreeMarker模板文件的位置
		cfg.setClassForTemplateLoading(getClass(), templatePath);
		
		ITextRenderer renderer = new ITextRenderer();
		try {
			// 设置 css中 的字体样式（暂时仅支持宋体和黑体）
			renderer.getFontResolver().addFont(classpath + fontPath + font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			// 设置模板的编码格式
			cfg.setEncoding(Locale.CHINA, encoding);
			// 获取模板文件 template.ftl
			Template template = cfg.getTemplate(templateFileName, encoding);
			StringWriter writer = new StringWriter();
			// 将数据输出到html中
			template.process(data, writer);
			writer.flush();
			
			
			File file = new File("E:\\[]201702\\Noname2.html");
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder result = new StringBuilder();
			String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close(); 
			    
			
			String html = writer.toString();
			html = result.toString();
			// 把html代码传入渲染器中
			renderer.setDocumentFromString(html);

			// 解决图片的相对路径问题 ##必须在设置document后再设置图片路径，不然不起作用
			// 如果使用绝对路径依然有问题，可以在路径前面加"file:/" 
			renderer.getSharedContext().setBaseURL(classpath + imagePath);
			renderer.layout();

			renderer.createPDF(out, false);
			renderer.finishPDF();
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}


	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}


	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
	}


	public void setFont(String font) {
		this.font = font;
	}


	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	@Override
	public String toString() {
		return   "[templatePath] : " + templatePath + " \r\n "
				+"[templateFileName] : " + templateFileName + " \r\n "
				+"[imagePath] : " + imagePath + " \r\n "
				+"[fontPath] : " + fontPath + " \r\n "
				+"[font] : " + font + " \r\n "
				+"[encoding] : " + encoding;
	}

}
