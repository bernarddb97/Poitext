package com.ccs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.categories.Category;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.test.annotations.type.SampleTest;

@Category(SampleTest.class)
public class PDFCreator4Doc {
	public static final String INDIVIDUAL_FILE = "./target/doc/Individual";
	public static final String SINGLE_FILE = "./target/doc/Single.pdf";
	public static final String LOGO_IMAGE = "./src/main/resources/img/SonyLogo.png";
	public static final String FONT_FREESANS = "./src/main/resources/fonts/FreeSans.ttf";
	public static final String FONT_ARIAL = "./src/main/resources/fonts/arial.ttf";
	public static final String FONT_ARIAL_I = "./src/main/resources/fonts/ariali.ttf";
	public static final String FONT_ARIAL_B = "./src/main/resources/fonts/arialbd.ttf";
	public static final String FONT_ARIAL_BI = "./src/main/resources/fonts/arialbi.ttf";

	private static int STYLE_STATIC = 1;
	private static int STYLE_DYNAMIC = 2;
	private static float FONT_SIZE = 9;

	private PageSize pageSize = new PageSize(PageSize.A4);
	private ReaderProperties rProperties = new ReaderProperties().setPassword("ccs2018".getBytes());
	private WriterProperties wProperties = new WriterProperties().setStandardEncryption(null, "ccs2018".getBytes(),
			EncryptionConstants.ALLOW_PRINTING | EncryptionConstants.ALLOW_COPY
					| EncryptionConstants.ALLOW_DEGRADED_PRINTING,
			EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA);

	/**
	 * the Font for Individual Doc
	 */
	PdfFont indivFont = null;

	public static void main(String[] args) throws Exception {

		File file = new File(INDIVIDUAL_FILE);
		file.getParentFile().mkdirs();

		String[] langArray = new String[] {
				"BG", "CS", "DA", "SW", "ET", "EL", "EN", "ES", "FR", "FI", "HR", "IT",
				"LV", "LT", "HU", "MT", "NL", "NO", "PL", "PT", "RO", "SK", "SL", "SV" };

		// Create Individual Doc
		PDFCreator4Doc pdfCreator = new PDFCreator4Doc();
		for (String lang : langArray) {
			pdfCreator.createIndividualDoc(lang);
		}

		// Create Single Doc
		pdfCreator.createSingleDoc(langArray);
	}

	/**
	 * 
	 * @param langArray
	 * @throws Exception
	 */
	public void createSingleDoc(String[] langArray) throws Exception {

		PdfDocument singlePdfDoc = new PdfDocument(new PdfWriter(SINGLE_FILE, wProperties));
		Document document = new Document(singlePdfDoc, pageSize);
		singlePdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new DocEventHandler());

		// EU DECLARATION OF CONFORMITY
		document.showTextAligned(new Paragraph("EU DECLARATION OF CONFORMITY"), pageSize.getWidth() / 2,
				pageSize.getHeight() - 140, 1, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);

//		document.add(new Paragraph("EU DECLARATION OF CONFORMITY").setTextAlignment(TextAlignment.CENTER));
		Map<String, String> docMap = this.getSingleDocMap();

		Table catalog = new Table(1);
		catalog.setHorizontalAlignment(HorizontalAlignment.CENTER);
		catalog.setWidth(UnitValue.createPercentValue(80));
		catalog.setBorder(new DashedBorder(0.5f));
		catalog.setMarginTop(130f);

		PdfDocument srcPdfDoc = null;
		PdfPage page = null;

		for (String lang : langArray) {
			String pdfFile = INDIVIDUAL_FILE + "_" + lang + ".pdf";
			srcPdfDoc = new PdfDocument(new PdfReader(pdfFile, rProperties));

			page = srcPdfDoc.getPage(1).copyTo(singlePdfDoc);
			singlePdfDoc.addPage(page);

			// Add Destination
			String destKey = lang;
			PdfArray desArray = new PdfArray();
			desArray.add(page.getPdfObject());
			desArray.add(PdfName.XYZ);
			desArray.add(new PdfNumber(0));
			desArray.add(new PdfNumber(page.getMediaBox().getHeight()));
			desArray.add(new PdfNumber(1));
			singlePdfDoc.addNamedDestination(destKey, desArray);

			PdfFont pdfFont = this.getProperFont(lang);

			// Add Link
			Paragraph linkPara = new Paragraph();
//			linkPara.setFont(pdfFont).setFontSize(FONT_SIZE + 2);
//			if ("EN".equals(lang) || "FR".equals(lang)) {
//				linkPara.setFontColor(ColorConstants.BLUE);
//			}
			linkPara.add(docMap.get(lang));
			linkPara.setProperty(Property.ACTION, PdfAction.createGoTo(destKey));

			Cell cataCell = new Cell().setFont(pdfFont).setFontSize(FONT_SIZE + 2);
			if ("EN".equals(lang) || "FR".equals(lang)) {
				cataCell.setFontColor(ColorConstants.BLUE);
			}
			cataCell.setTextAlignment(TextAlignment.CENTER);
			cataCell.setBorder(new DashedBorder(0.5f));
			cataCell.add(linkPara);
			catalog.addCell(cataCell);

			srcPdfDoc.close();
		}
		document.add(catalog);

		// EU DECLARATION OF CONFORMITY
		document.showTextAligned(new Paragraph("EU DECLARATION OF CONFORMITY"), pageSize.getWidth() / 2, 50, 1,
				TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
		document.showTextAligned(new Paragraph("(EN)"), pageSize.getWidth() - 50, 50, 1, TextAlignment.CENTER,
				VerticalAlignment.BOTTOM, 0);

		document.close();
	}

	/**
	 * 
	 * @param lang
	 * @throws Exception
	 */
	public void createIndividualDoc(String lang) throws Exception {

		String pdfFile = INDIVIDUAL_FILE + "_" + lang + ".pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfFile, wProperties));
		pdfDoc.setDefaultPageSize(PageSize.A4);

		Document doc = new Document(pdfDoc);
		pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new DocEventHandler());
		indivFont = this.getProperFont(lang);

		// 項番１～４
		doc.add(createFixedTable());

		// 項番５
		doc.add(createDynamicTable5());

		// 項番６
		doc.add(createDynamicTable6());

		// 項番７
		doc.add(createDynamicTable7());

		// 項番８
		doc.add(createDynamicTable8());

		// 項番９
		doc.add(createDynamicTable9());

		// 署名情報
		doc.add(createSignatureTable());
		
		doc.close();
	}

	/**
	 * 項番１～４
	 * @return
	 * @throws Exception
	 */
	private Table createFixedTable() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 8, 21 });
		table.setMarginTop(70f);

		// The First Row
		table.addCell(getFormatedCell("1."));
		table.addCell(getFormatedCell("Model No.:"));
		table.addCell(getFormatedCell("SRS-X88", STYLE_DYNAMIC));

		// Blank Row
		table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));

		// The Second Row
		table.addCell(this.getFormatedCell("2."));
		String content = "Name and address of the manufacturer’s authorised representative:";
		table.addCell(this.getFormatedCell(content));
		content = "Sony Belgium, bijkantoor van Sony Europe Limited, Da Vincilaan 7-D1, 1935 Zaventem, Belgium";
		table.addCell(this.getFormatedCell(content, STYLE_DYNAMIC));

		// Blank Row
		table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));

		// The Third Row
		table.addCell(this.getFormatedCell("3."));
		content = "This declaration of conformity is issued under the sole responsibility of the manufacturer:";
		table.addCell(this.getFormatedCell(content));
		content = "Sony Corporation, 1-7-1 Konan, Minato-ku, Tokyo, 108-0075 Japan";
		table.addCell(this.getFormatedCell(content, STYLE_DYNAMIC));

		// Blank Row
		table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));

		// The 4th Row
		table.addCell(this.getFormatedCell("4."));
		content = "Object of the declaration:";
		table.addCell(this.getFormatedCell(content));
		content = "Personal Audio System";
		table.addCell(this.getFormatedCell(content, STYLE_DYNAMIC));

		// Blank Row
		table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));

		return table;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createDynamicTable5() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 1, 28 });

		table.addCell(this.getFormatedCell("5."));
		String title = "The object of the declaration described above is in conformity with:";
		table.addCell(this.getFormatedCell(1, 2, title, STYLE_STATIC));

		List<String> contents = new ArrayList<String>();
		contents.add("2014/53/EU (Radio Equipment)");
		contents.add("2014/30/EU (EMC)");
		contents.add("2014/35/EU (Low Voltage)");
		contents.add("2011/65/EU (RoHS)");
		contents.add("2009/125/EC, (EC) No 1194/2012, 2009/125/EC, (EC) No 1275/2008 (Ecodesign) ");

		for (String content : contents) {
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(content, STYLE_DYNAMIC));
		}

		return table;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createDynamicTable6() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 1, 5, 23 });

		table.addCell(this.getFormatedCell("6."));
		String title = "Where applicable, references to the relevant harmonised standards used"
				+ " or references to the technical specifications in relation to which conformity is declared:";
		table.addCell(this.getFormatedCell(1, 3, title, STYLE_STATIC));

		Map<String, String> contentMap = null;
		List<Map<String, String>> contents = new ArrayList<Map<String, String>>();

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Radio Equipment:");
		contentMap.put("mapValue",
				"EN 60950-1:2006 + A1:2010 + A2:2013 + A11:2009 + A12:2011, EN 50360:2001 + A1:2012, EN "
						+ "50566:2013, EN 62479:2010, EN 62209-1:2006, EN 62209-2:2010, EN 301489-3 v2.1.0, EN "
						+ "301489-17 v3.1.0, EN 301489-52 v0.0.5, EN 300328 v2.0.20, EN 301893 v0.0.7, EN 300330 "
						+ "v2.1.0, EN 300440 v2.1.0, EN 301908-1 v11.0.1, EN 301511 v12.1.1/3GPP TS 51.010-1 v13.0.0 "
						+ "(2016-03), EN 301908-2 v6.2.1/3GPP TS 34.121-1 v12.3.0 (2016-03), EN 301908-13 v6.2.1/3GPP "
						+ "TS 36.521-1 v13.1.0 (2016-03)");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "EMC:");
		contentMap.put("mapValue", "EN 50581:2012");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Low Voltage:");
		contentMap.put("mapValue", "EN 50564:2011");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "RoHS:");
		contentMap.put("mapValue", "EN 50581:2012");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Ecodesign:");
		contentMap.put("mapValue", "EN 50564:2011");
		contents.add(contentMap);

		for (Map<String, String> content : contents) {
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(content.get("mapKey"), STYLE_DYNAMIC));
			table.addCell(this.getFormatedCell(content.get("mapValue"), STYLE_DYNAMIC));
		}

		return table;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createDynamicTable7() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 1, 5, 23 });

		table.addCell(this.getFormatedCell("7."));
		String title = "Where applicable, the notified body, description of intervention and certificate:";
		table.addCell(this.getFormatedCell(1, 3, title, STYLE_STATIC));

		Map<String, String> contentMap = null;
		List<Map<String, String>> contents = new ArrayList<Map<String, String>>();

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Radio Equipment:");
		contentMap.put("mapValue", "CETECOM ICT SERVICES GMBH (0682), Art 3.2, RED000402");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "EMC:");
		contentMap.put("mapValue", "XXXXXXXXXXXXXXXXXXXXXX");
		contents.add(contentMap);

		for (Map<String, String> content : contents) {
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(content.get("mapKey"), STYLE_DYNAMIC));
			table.addCell(this.getFormatedCell(content.get("mapValue"), STYLE_DYNAMIC));
		}

		return table;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createDynamicTable8() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 1, 5, 23 });

		table.addCell(this.getFormatedCell("8."));
		String title = "Where applicable, description of accessories and components, including software, " 
				+ "which allow the radio equipment to operate as intended and covered by the EU declaration of conformity:";
		table.addCell(this.getFormatedCell(1, 3, title, STYLE_STATIC));

		Map<String, String> contentMap = null;
		List<Map<String, String>> contents = new ArrayList<Map<String, String>>();

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Software:");
		contentMap.put("mapValue", "39._.A._._");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "Accessory(ies):");
		contentMap.put("mapValue", "RMT-CX9 (Remote Commander)");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "");
		contentMap.put("mapValue", "VGP-AC19V75 (AC Adapter)");
		contents.add(contentMap);

		contentMap = new HashMap<String, String>();
		contentMap.put("mapKey", "");
		contentMap.put("mapValue", "");
		contents.add(contentMap);

		for (Map<String, String> content : contents) {
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(content.get("mapKey")));
			table.addCell(this.getFormatedCell(content.get("mapValue"), STYLE_DYNAMIC));
		}

		return table;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createDynamicTable9() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 1, 1, 28 });

		table.addCell(this.getFormatedCell("9."));
		String title = "Additional information:";
		table.addCell(this.getFormatedCell(1, 2, title, STYLE_STATIC));

		List<String> contents = new ArrayList<String>();
		contents.add("");
		contents.add("");

		for (String content : contents) {
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(""));
			table.addCell(this.getFormatedCell(content, STYLE_DYNAMIC));
		}

		return table;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private Table createSignatureTable() throws Exception {

		Table table = this.getFormatedTable(pageSize, new float[] { 4, 3, 14, 9 });

		// The First Row
		table.addCell(getFormatedCell(1, 2, "Signed for and on behalf of:", STYLE_STATIC));
		table.addCell(getFormatedCell(1, 2, "Sony Belgium, bijkantoor van Sony Europe Limited", STYLE_STATIC));

		// The Second Row
		table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));
		table.addCell(this.getSignatureCell("(signature)"));

		// The Third Row
		table.addCell(getFormatedCell("Zaventem,"));
		table.addCell(getFormatedCell(1, 3, "2018-01-12", STYLE_DYNAMIC));

		// Whose Names Row?
		List<String> nameList = new ArrayList<String>();
		nameList.add("Kris De Pauw");
		nameList.add("Director");
		nameList.add("Branch Manager");

		for (String name : nameList) {
			table.addCell(getFormatedCell(1, 3, "", STYLE_STATIC));
			table.addCell(getFormatedCell(name));
		}

		// The Last Row
		table.addCell(getFormatedCell("Reference Number:").setFontSize(FONT_SIZE - 2));
		table.addCell(getFormatedCell(1, 3, "2018EU00001", STYLE_DYNAMIC).setFontSize(FONT_SIZE - 2));

		return table;
	}

	/**
	 * 
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	private PdfFont getProperFont(String lang) throws Exception {

		PdfFont pdfFont = null;

		// BG	保加利亚语		Cp1251	Cyrillic_General
		// CS	捷克语			Cp1250	Czech
		// DA	丹麦语			Cp1252	Danish_Norwegian
		// DE	德语（标准）		Cp1252	Danish_Norwegian
		// ET	爱沙尼亚语		Cp1257	Estonian
		// EL	希腊语			Cp1253	Greek
		// EN	英语				Cp1252	Latin1_General
		// ES	西班牙语			Cp1252	Mexican_Trad_Spanish
		// FR	法语(标准)		Cp1252	French
		// FI	瑞典语(芬兰)		Cp1252	Finnish_Swedish
		// HR	克罗地亚语		Cp1250	Croatian
		// IT	意大利语(标准)	Cp1252	Latin1_General
		// LV	拉脱维亚语		Cp1257	Latvian
		// LT	立陶宛语			Cp1257	Lithuanian
		// HU	匈牙利语			Cp1250	Hungarian
		// MT	马耳他语			Cp1208	??????????????????
		// NL	荷兰语(标准)		Cp1252	Latin1_General
		// NO	挪威语			Cp1252	Danish_Norwegian
		// PL	波兰语			Cp1250	Polish
		// PT	葡萄牙语			Cp1252	Latin1_General
		// RO	罗马尼亚语		Cp1250	Romanian
		// SK	斯洛伐克语		Cp1250	Slovak
		// SL	斯洛文尼亚语		Cp1250	Slovenian
		// SV	瑞典语			Cp1252	Finnish_Swedish

		String Cp1250 = "CS,HR,HU,PL,RO,SK,SL";
		String Cp1251 = "BG";
		String Cp1252 = "DA,DE,EN,ES,FR,FI,IT,NL,NO,PT,SV";
		String Cp1253 = "EL";
		String Cp1257 = "ET,LV,LT";
		
//		String fontProgram = FONT_FREESANS;
		String fontProgram = FONT_ARIAL;

		// TODO Which font and which encode to use?
		// EL, RO, MT

		lang = lang.toUpperCase();
		if (Cp1252.contains(lang)) {
			pdfFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.CP1252, true);
		} else if (Cp1250.contains(lang)) {
			pdfFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.CP1250, true);
		} else if (Cp1257.contains(lang)) {
			pdfFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.CP1257, true);
		} else if (Cp1253.contains(lang)) {
			pdfFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.CP1253, true);
		} else if (Cp1251.contains(lang)) {
			pdfFont = PdfFontFactory.createFont(fontProgram, "Cp1251", true);
		} else {
//			if ("MT".equals(lang)) {
//				pdfFont = PdfFontFactory.createFont(fontProgram, "Cp1208", true);
//			} else {
//				pdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
				pdfFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);
//			}
		}

		return pdfFont;
	}

	/**
	 * 
	 * @param pageSize
	 * @param columnWidths
	 * @return
	 * @throws Exception
	 */
	private Table getFormatedTable(PageSize pageSize, float[] columnWidths) throws Exception {
		
		float unitWidth = pageSize.getWidth() / 30;
		for (int i = 0; i < columnWidths.length; i++) {
			columnWidths[i] = columnWidths[i] * unitWidth;
		}
		Table table = new Table(columnWidths);
		table.setWidth(UnitValue.createPercentValue(100));
		table.setBorder(Border.NO_BORDER);
		table.setMarginTop(10f);
		
		return table;
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private Cell getFormatedCell(String content) throws Exception {
		return this.getFormatedCell(1, 1, content, STYLE_STATIC);
	}

	/**
	 * 
	 * @param content
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private Cell getFormatedCell(String content, int type) throws Exception {
		return this.getFormatedCell(1, 1, content, type);
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private Cell getSignatureCell(String content) throws Exception {
		Cell cell = this.getFormatedCell(content);
		cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 0.5f));
		cell.setTextAlignment(TextAlignment.CENTER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.setFontColor(ColorConstants.ORANGE);
		cell.setHeight(30f);
		return cell;
	}

	/**
	 * 
	 * @param rowSpan
	 * @param colSpan
	 * @param content
	 * @param styleType
	 * @return
	 * @throws Exception
	 */
	private Cell getFormatedCell(int rowSpan, int colSpan, String content, int styleType) throws Exception {

		if (indivFont == null) {
			indivFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		}

		Cell cell = new Cell(rowSpan, colSpan).setFont(indivFont).setFontSize(FONT_SIZE);
		cell.setTextAlignment(TextAlignment.LEFT);
		cell.setPaddings(1, 3, 0, 3);
		cell.setBorder(Border.NO_BORDER);

		if ("".equals(content)) {
			cell.setHeight(9f);
		}

		if (styleType == STYLE_DYNAMIC) {
			cell.setFontColor(ColorConstants.BLUE);
		}

		cell.add(new Paragraph(content).setMultipliedLeading(1));

		return cell;
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, String> getSingleDocMap() {

		Map<String, String> docMap = new HashMap<String, String>();
		docMap.put("BG", "(BG) ДЕКЛАРАЦИЯ ЗА СЪОТВЕТСТВИЕ С ИЗИСКВАНИЯТА НА ЕС");
		docMap.put("CS", "(CS) EU PROHLÁŠENÍ O SHODĚ");
		docMap.put("DA", "(DA) EU-OVERENSSTEMMELSESERKLÆRING");
		docMap.put("SW", "(DE) EU-KONFORMITÄTSERKLÄRUNG");
		docMap.put("ET", "(ET) ELI VASTAVUSDEKLARATSIOON");
		docMap.put("EL", "(EL) ΔΗΛΩΣΗ ΣΥΜΜΟΡΦΩΣΗΣ ΕΕ");
		docMap.put("EN", "(EN) EU DECLARATION OF CONFORMITY");
		docMap.put("ES", "(ES) DECLARACIÓN DE CONFORMIDAD UE");
		docMap.put("FR", "(FR) DÉCLARATION UE DE CONFORMITÉ");
		docMap.put("FI", "(FI) EU-VAATIMUSTENMUKAISUUSVAKUUTUS");
		docMap.put("HR", "(HR) EU IZJAVA O SUKLADNOSTI");
		docMap.put("IT", "(IT) DICHIARAZIONE UE DI CONFORMITÀ");
		docMap.put("LV", "(LV) ES ATBILSTĪBAS DEKLARĀCIJA");
		docMap.put("LT", "(LT) ES ATITIKTIES DEKLARACIJA");
		docMap.put("HU", "(HU) EU–MEGFELELŐSÉGI NYILATKOZAT");
		docMap.put("MT", "(MT) DIKJARAZZJONI TAL-KONFORMITÀ TAL-UE");
		docMap.put("NL", "(NL) EU-CONFORMITEITSVERKLARING");
		docMap.put("NO", "(NO) EU-samsvarserklæring");
		docMap.put("PL", "(PL) DEKLARACJA ZGODNOŚCI UE");
		docMap.put("PT", "(PT) DECLARAÇÃO DE CONFORMIDADE UE");
		docMap.put("RO", "(RO) DECLARAȚIE DE CONFORMITATE UE");
		docMap.put("SK", "(SK) VYHLÁSENIE O ZHODE EÚ");
		docMap.put("SL", "(SL) IZJAVA EU O SKLADNOSTI");
		docMap.put("SV", "(SV) EU-FÖRSÄKRAN OM ÖVERENSSTÄMMELSE");

		return docMap;
	}

	/**
	 * ページ切替ハンドラー
	 */
	class DocEventHandler implements IEventHandler {

		@Override
		public void handleEvent(Event event) {

			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			Rectangle pageSize = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

			try {
				ImageData logoImage = ImageDataFactory.create(LOGO_IMAGE);
				pdfCanvas.addImage(logoImage, 34, pageSize.getTop() - 60, 90, false);

				String headerText = "Sony Belgium, bijkantoor van Sony Europe Limited";
				String footerText = "Sony Europe Limited. A company registered in England and Wales.";

				// Add Header and Footer
				pdfCanvas.beginText()
						.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), FONT_SIZE)
						.moveText(36, pageSize.getTop() - 75)
						.showText(headerText)
						.moveText(0, 110 - pageSize.getTop())
						.showText(footerText)
						.endText();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Add Watermark
			Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
			canvas.setFontColor(ColorConstants.LIGHT_GRAY);
			canvas.setProperty(Property.FONT_SIZE, UnitValue.createPointValue(120));
			canvas.showTextAligned(new Paragraph("DRAFT"), 
					pageSize.getWidth() / 2, 
					pageSize.getHeight() / 2,
					pdfDoc.getPageNumber(page), 
					TextAlignment.CENTER, 
					VerticalAlignment.MIDDLE, 
					(float) Math.PI / 4);

			canvas.close();
		}
	}
}