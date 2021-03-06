package com.itextpdf.tutorial.jump.start.chatper4;
/*
 * This example is part of the iText 7 tutorial.
 */

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.test.annotations.WrapToTest;

import java.io.File;
import java.io.IOException;

/**
 * Simple link annotation example.
 */
@WrapToTest
public class C04E01_02_LinkAnnotation {

	public static final String DEST = "results/chapter04/link_annotation.pdf";

	public static void main(String args[]) throws IOException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new C04E01_02_LinkAnnotation().createPdf(DEST);
	}

	public void createPdf(String dest) throws IOException {

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

		// Initialize document
		Document document = new Document(pdf);

		// Create link annotation
		PdfLinkAnnotation annotation = new PdfLinkAnnotation(new Rectangle(0, 0))
				.setAction(PdfAction.createURI("http://itextpdf.com/"));
		Link link = new Link("here", annotation);
		link.setUnderline();
		Paragraph p = new Paragraph("The example of link annotation. Click ").add(link).add(" to learn more...");
		document.add(p);

		// Close document
		document.close();

	}
}