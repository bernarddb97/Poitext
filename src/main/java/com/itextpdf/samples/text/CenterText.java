package com.itextpdf.samples.text;
/*
 * Example written in answer to a question on StackOverflow.
 * http://stackoverflow.com/questions/39437838
 */

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bruno
 */
public class CenterText {
	class MyLine implements ILineDrawer {
		private float lineWidth = 1;
		private float offset = 5;
		private Color color = ColorConstants.BLACK;

		@Override
		public void draw(PdfCanvas canvas, Rectangle drawArea) {
			canvas.saveState().setStrokeColor(color).setLineWidth(lineWidth)
					.moveTo(drawArea.getX(), drawArea.getY() + lineWidth / 2 + offset)
					.lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY() + lineWidth / 2 + offset).stroke()
					.restoreState();
		}

		@Override
		public float getLineWidth() {
			return lineWidth;
		}

		@Override
		public void setLineWidth(float lineWidth) {
			this.lineWidth = lineWidth;
		}

		@Override
		public Color getColor() {
			return color;
		}

		@Override
		public void setColor(Color color) {
			this.color = color;
		}

		public float getOffset() {
			return offset;
		}

		public void setOffset(float offset) {
			this.offset = offset;
		}

	}

	public static final String DEST = "results/text/center_text.pdf";

	public static void main(String[] args) throws IOException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new CenterText().createPdf(DEST);
	}

	public void createPdf(String dest) throws IOException {
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		PageSize pagesize = PageSize.A4;
		Document document = new Document(pdf, pagesize);
		float w = pagesize.getWidth() - document.getLeftMargin() - document.getRightMargin();
		MyLine line = new MyLine();
		List<TabStop> tabstops = new ArrayList<TabStop>();
		tabstops.add(new TabStop(w / 2, TabAlignment.CENTER, line));
		tabstops.add(new TabStop(w, TabAlignment.LEFT, line));
		Paragraph p = new Paragraph();
		p.addTabStops(tabstops);
		p.add(new Tab()).add("Text in the middle").add(new Tab());
		document.add(p);
		document.close();
	}
}