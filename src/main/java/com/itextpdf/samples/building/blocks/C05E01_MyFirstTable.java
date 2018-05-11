package com.itextpdf.samples.building.blocks;
/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.
 
    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/*
 * This example was written by Bruno Lowagie
 * in the context of the book: iText 7 building blocks
 */

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.test.annotations.WrapToTest;
import java.io.File;
import java.io.IOException;

/**
 * @author Bruno Lowagie (iText Software)
 */
@WrapToTest
public class C05E01_MyFirstTable {

	public static final String DEST = "results/chapter05/my_first_table.pdf";

	public static void main(String args[]) throws IOException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new C05E01_MyFirstTable().createPdf(DEST);
	}

	public void createPdf(String dest) throws IOException {
		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

		// Initialize document
		Document document = new Document(pdf);
		Table table = new Table(new float[] { 1, 1, 1 });
		table.addCell(new Cell(1, 3).add(new Paragraph("Cell with colspan 3")));
		table.addCell(new Cell(2, 1).add(new Paragraph("Cell with colspan 2")));
		table.addCell("row 1; cell 1");
		table.addCell("row 1; cell 2");
		table.addCell("row 2; cell 1");
		table.addCell("row 2; cell 2");
		document.add(table);
		document.close();
	}
}