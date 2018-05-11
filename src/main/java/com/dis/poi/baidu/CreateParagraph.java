package com.dis.poi.baidu;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class CreateParagraph {
	public static void main(String[] args) throws Exception {
		// Blank Document
		XWPFDocument document = new XWPFDocument();
		// create Paragraph
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("At tutorialspoint.com, we strive hard to " + "provide quality tutorials for self-learning "
				+ "purpose in the domains of Academics, Information "
				+ "Technology, Management and Computer Programming Languages.");

		// Write the Document in file system
		FileOutputStream out = new FileOutputStream(new File("createparagraph.docx"));
		document.write(out);
		out.close();

		System.out.println("createparagraph.docx written successfully");
	}
}