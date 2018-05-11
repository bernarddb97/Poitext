package com.dis.poi.baidu;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class AlignParagraph {
	public static void main(String[] args) throws Exception {
		// Blank Document
		XWPFDocument document = new XWPFDocument();
		// Write the Document in file system
		FileOutputStream out = new FileOutputStream(new File("alignparagraph.docx"));

		// create paragraph
		XWPFParagraph paragraph = document.createParagraph();
		// Set alignment paragraph to RIGHT
		paragraph.setAlignment(ParagraphAlignment.RIGHT);

		XWPFRun run = paragraph.createRun();
		run.setText("At tutorialspoint.com, we strive hard to " + "provide quality tutorials for self-learning "
				+ "purpose in the domains of Academics, Information "
				+ "Technology, Management and Computer Programming " + "Languages.");

		// Create Another paragraph
		paragraph = document.createParagraph();
		// Set alignment paragraph to CENTER
		paragraph.setAlignment(ParagraphAlignment.CENTER);

		run = paragraph.createRun();
		run.setText("The endeavour started by Mohtashim, an AMU "
				+ "alumni, who is the founder and the managing director "
				+ "of Tutorials Point (I) Pvt. Ltd. He came up with the "
				+ "website tutorialspoint.com in year 2006 with the help"
				+ "of handpicked freelancers, with an array of tutorials" + " for computer programming languages. ");

		document.write(out);
		out.close();
		System.out.println("alignparagraph.docx written successfully");
	}
}