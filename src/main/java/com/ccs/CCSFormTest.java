package com.ccs;
/*
 * This example is part of the iText 7 tutorial.
 */

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.WrapToTest;

/**
 * Simple filling out form example.
 */
@WrapToTest
public class CCSFormTest {

	public static final String SRC = "src/main/resources/pdf/ccs_form.pdf";
	public static final String DEST = "results/chapter04/ccs_form_flatten.pdf";

	public static void main(String args[]) throws IOException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new CCSFormTest().manipulatePdf(SRC, DEST);
	}

	public void manipulatePdf(String src, String dest) throws IOException {

		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

		PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
		Map<String, PdfFormField> fields = form.getFormFields();
		for (int i = 1; i <= 10; i++) {
			fields.get("item" + i).setValue("摘要" + i);
			fields.get("quantity" + i).setValue(String.valueOf(10 + i));
			fields.get("unitprice" + i).setValue(String.valueOf(100f + i));
			fields.get("price" + i).setValue(String.valueOf((10 + i) * 100.0f + i));
		}
		
		StringBuffer noteStr = new StringBuffer();
		int i = 25;
		while (i-- > 0) {
			noteStr.append("備考欄に入力した内容が制限の文字数を超えたらどうなるの？<" + i + ">");
		}
		fields.get("note").setValue(noteStr.toString());
		form.flattenFields();

		pdf.close();

	}
}