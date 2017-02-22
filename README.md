## 使用freemarker模板生成pdf

> 1、使用freemarker编写html模板
> 2、利用flyingSaucer生成pdf
> 3、[GitHub 源码地址](https://github.com/zhaoshuxue/PdfTemplateExport)

### 添加maven依赖

```
<dependency>	
	<groupId>org.xhtmlrenderer</groupId>
	<artifactId>flying-saucer-pdf</artifactId>
	<version>9.0.9</version>
</dependency>

<dependency>
	<groupId>org.freemarker</groupId>
	<artifactId>freemarker</artifactId>
	<version>2.3.23</version>
</dependency>

```

### 项目文件结构树

```

src
├─main
│  ├─java
│  │  └─com
│  │      └─zsx
│  │          └─pdfTemplate
│  │                  PDFTemplateUtil.java
│  │                  UtilTest.java
│  │
│  ├─resources
│  │  ├─pdf
│  │  │  │  pdf.ftl
│  │  │  │
│  │  │  └─font
│  │  │          simhei.ttf
│  │  │          simsun.ttc
│  │  │
│  │  └─static
│  │          style.css
│  │
│  └─webapp
│      │  index.jsp
│      │
│      └─WEB-INF
│              web.xml
│
└─test
    └─java 

```


### 主要工具类 PDFTemplateUtil.java

主要的配置项都设置了默认值，比如，freemarker模板的存放路径，模板名称，字体文件等。
提供了set方法，方便自定义各个配置项。


```

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
			
			String html = writer.toString();
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


```


### 调用方法  UtilTest.java

传入的out输出流可以是一个文件流，也可以是servlet中的response的输出流

```

package com.zsx.pdfTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UtilTest {
	
	public static void main(String[] args) throws Exception {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("title", "你的名字");
		
		FileOutputStream out = new FileOutputStream(new File("c://1.pdf"));
		
		PDFTemplateUtil pdfUtil = new PDFTemplateUtil();
		pdfUtil.createPDF(data, out);
		
	}

}




```


###  freemarker模板文件   pdf.ftl

```

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="content-type" content="text/html;charset=utf-8"></meta>
<style type="text/css"> 
    body {
        font-family: SimSun;
    }
</style>
</head>
<body>
${title}
<img src="http://search.maven.org/ajaxsolr/images/SON_banners_300x60v6.png" ></img>
</body>
</html>

```



### 注

- 字体文件可以去 `C:\Windows\Fonts`路径下查找





