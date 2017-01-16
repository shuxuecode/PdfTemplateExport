package com.zsx.pdfTemplate;

import java.io.OutputStream;

public class MyPdfUtil extends PDFTemplateUtil {
	
	
	@Override
	public void createPDF(Object data, OutputStream out) {
		
		setTemplateFileName("222.ftl");
		
		setClasspath("D:\\eclipseMarsWorkspace2\\PdfTemplateExport\\target\\classes\\");
		
		super.createPDF(data, out);
	}
	
}
