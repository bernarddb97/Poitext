package com.itextpdf.samples.tables;

import java.io.File;

import org.junit.experimental.categories.Category;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class ColumnWidthExample extends GenericTest {
	public static final String DEST = "./target/test/resources/sandbox/tables/column_width_example.pdf";

	public static void main(String[] args) throws Exception {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ColumnWidthExample().manipulatePdf(DEST);
	}

	@Override
	protected void manipulatePdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		// Note that it is not necessary to create new PageSize object,
		// but for testing reasons (connected to parallelization) we call constructor
		// here
		Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

		float[] columnWidths = { 1, 5, 5 };
		Table table = new Table(columnWidths);
		// table.setWidthPercent(100);
		PdfFont f = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		Cell cell = new Cell(1, 3).add(new Paragraph("This is a header")).setFont(f).setFontSize(13)
				.setFontColor(DeviceGray.WHITE).setBackgroundColor(DeviceGray.BLACK)
				.setTextAlignment(TextAlignment.CENTER);
		table.addHeaderCell(cell);
		for (int i = 0; i < 2; i++) {
			Cell[] headerFooter = new Cell[] {
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("#")),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Key")),
					new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Value")) };
			for (Cell hfCell : headerFooter) {
				if (i == 0) {
					table.addHeaderCell(hfCell);
				} else {
					table.addFooterCell(hfCell);
				}
			}
		}
		for (int counter = 1; counter < 101; counter++) {
			table.addCell(
					new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter))));
			table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("key " + counter)));
			table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("value " + counter)));
		}
		doc.add(table);
		doc.close();
	}
}