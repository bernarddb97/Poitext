package fr.opensagres.xdocreport.samples.docxandvelocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.samples.docxandvelocity.model.Project;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class DocxProjectWithVelocity {

	public static void main(String[] args) {
		try {
			// 1) Load Docx file by filling Velocity template engine and cache it to the registry
			InputStream in = DocxProjectWithVelocity.class.getResourceAsStream("DocxProjectWithVelocity.docx");
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

			// 2) Create context Java model
			IContext context = report.createContext();
			Project project = new Project("XDocReport");
			context.put("project", project);

			// 3) Generate report by merging Java model with the Docx
			OutputStream out = new FileOutputStream(new File("DocxProjectWithVelocity_Out.docx"));
			report.process(context, out);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
	}
}