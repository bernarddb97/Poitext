package com.dis.poi.yibai;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FontStyle {
	public static void main(String[] args) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Fontstyle");
		XSSFRow row = spreadsheet.createRow(2);
		
		// Create a new font and alter it.
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 30);
		font.setFontName("IMPACT");
		font.setItalic(true);
		font.setColor(HSSFColor.BRIGHT_GREEN.index);
		
		// Set font into style
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		
		// Create a cell with a value and set style to it.
		XSSFCell cell = row.createCell(1);
		cell.setCellValue("Font Style");
		cell.setCellStyle(style);
		
		FileOutputStream out = new FileOutputStream(new File("fontstyle.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("fontstyle.xlsx written successfully");
	}
}