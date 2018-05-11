package com.itextpdf.samples.tables;

/*
 
    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
 
*/

import java.io.File;

import org.junit.experimental.categories.Category;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class NestedTableProblem extends GenericTest {
	public static final String DEST = "./target/test/resources/sandbox/tables/nested_table_problem.pdf";

	public static void main(String[] args) throws Exception {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new NestedTableProblem().manipulatePdf(DEST);
	}

	@Override
	protected void manipulatePdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc, new PageSize(612, 792));
		doc.setMargins(30, 21, 35, 21);

		// table 2
		Table table2 = new Table(1);
		table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
		table2.addCell(
				new Cell().setBorder(new SolidBorder(ColorConstants.RED, 1)).add(new Paragraph("Goodbye World")));
		table2.setWidth(new UnitValue(UnitValue.PERCENT, 80));
		// table 1
		Table table1 = new Table(1);
		table1.setHorizontalAlignment(HorizontalAlignment.LEFT);
		Cell cell = new Cell();
		cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
		cell.add(new Paragraph("Hello World"));
		cell.add(table2);
		cell.add(new Paragraph("Hello World"));
		table1.addCell(cell);
		doc.add(table1);

		doc.close();
	}
}