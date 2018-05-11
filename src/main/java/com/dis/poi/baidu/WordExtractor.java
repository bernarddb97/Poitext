package com.dis.poi.baidu;
import java.io.FileInputStream;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordExtractor {
	public static void main(String[] args) throws Exception {
		XWPFDocument docx = new XWPFDocument(new FileInputStream("alignparagraph.docx"));
		// using XWPFWordExtractor Class
		XWPFWordExtractor we = new XWPFWordExtractor(docx);
		System.out.println(we.getText());// 保存并阅读
	}
}