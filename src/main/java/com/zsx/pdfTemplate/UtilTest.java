package com.zsx.pdfTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UtilTest {
	
	public static void main(String[] args) throws Exception {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("title", "你的名字");
		
		FileOutputStream out = new FileOutputStream(new File("5.pdf"));
		
//		PDFTemplateUtil pdfUtil = new PDFTemplateUtil();
//		pdfUtil.createPDF(data, out);
		
		MyPdfUtil myPdf = new MyPdfUtil();
		myPdf.createPDF(data, out);
		
	}

}
