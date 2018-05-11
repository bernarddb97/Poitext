package com.itextpdf.samples.form;
/*
 
    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
 
*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21028286/itext-editable-texfield-is-not-clickable
 */

import java.io.File;

import org.junit.experimental.categories.Category;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class CreateFormInTable extends GenericTest {
	public static final String DEST = "./target/test/resources/sandbox/acroforms/create_form_in_table.pdf";

	public static void main(String[] args) throws Exception {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new CreateFormInTable().manipulatePdf(DEST);
	}

	@Override
	protected void manipulatePdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Table table = new Table(2);
		table.setAutoLayout();

		Cell cell;
		cell = new Cell().add(new Paragraph("Name:"));
		table.addCell(cell);
		cell = new Cell();
		cell.setNextRenderer(new MyCellRenderer(cell, "name"));
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Address"));
		table.addCell(cell);
		cell = new Cell();
		cell.setNextRenderer(new MyCellRenderer(cell, "address"));
		table.addCell(cell);
		doc.add(table);

		doc.close();
	}

	private class MyCellRenderer extends CellRenderer {
		protected String fieldName;

		public MyCellRenderer(Cell modelElement, String fieldName) {
			super(modelElement);
			this.fieldName = fieldName;
		}

		@Override
		public void draw(DrawContext drawContext) {
			super.draw(drawContext);
			PdfTextFormField field = PdfFormField.createText(drawContext.getDocument(), getOccupiedAreaBBox(),
					fieldName, "");
			field.setMultiline(true);
			StringBuffer buffer = new  StringBuffer();
			for (int i = 0; i < 20; i++) {
				buffer.append("AAAAAAAAAAAAAAAAAAAAAAAAA").append("何だなんだなんだよ" + i);
			}
			field.setValue(buffer.toString());
			
			PdfAcroForm form = PdfAcroForm.getAcroForm(drawContext.getDocument(), true);
			form.addField(field);
		}
	}
}