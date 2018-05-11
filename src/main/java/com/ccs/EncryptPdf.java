package com.ccs;

import java.io.File;

import org.junit.experimental.categories.Category;

/**
* Example written by Bruno Lowagie in answer to:
* http://stackoverflow.com/questions/27867868/how-can-i-decrypt-a-pdf-document-with-the-owner-password
*/
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class EncryptPdf extends GenericTest {
	public static final String DEST = "./target/test/resources/sandbox/security/encrypt_pdf.pdf";
	public static final String SRC = "./src/test/resources/pdfs/hello_encrypted.pdf";

	public static void main(String[] args) throws Exception {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new EncryptPdf().manipulatePdf(DEST);
	}

	@Override
	protected void manipulatePdf(String dest) throws Exception {
		PdfReader reader = new PdfReader(SRC, new ReaderProperties().setPassword("World".getBytes()));
		PdfWriter writer = new PdfWriter(DEST,
				// new WriterProperties().setStandardEncryption("Hello".getBytes(),
				// "World".getBytes(),
				new WriterProperties().setStandardEncryption(null, "World".getBytes(),
						EncryptionConstants.ALLOW_PRINTING | EncryptionConstants.ALLOW_COPY
								| EncryptionConstants.ALLOW_DEGRADED_PRINTING,
						EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
		PdfDocument pdfDoc = new PdfDocument(reader, writer);
		pdfDoc.close();
	}
}