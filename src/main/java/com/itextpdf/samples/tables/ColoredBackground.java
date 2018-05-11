package com.itextpdf.samples.tables;
/*
 
    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
 
*/

import java.io.File;

import org.junit.experimental.categories.Category;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class ColoredBackground extends GenericTest {
	public static final String DEST = "./target/test/resources/sandbox/tables/colored_background.pdf";

	public static void main(String[] args) throws Exception {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ColoredBackground().manipulatePdf(DEST);
	}

	@Override
	protected void manipulatePdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Table table;
		Cell cell;
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		table = new Table(16);
		for (int aw = 0; aw < 16; aw++) {
			cell = new Cell().add(new Paragraph("hi").setFont(font).setFontColor(ColorConstants.WHITE));
			cell.setBackgroundColor(ColorConstants.BLUE);
			cell.setBorder(Border.NO_BORDER);
			cell.setTextAlignment(TextAlignment.CENTER);
			table.addCell(cell);
		}
		doc.add(table);

		doc.close();
	}
}