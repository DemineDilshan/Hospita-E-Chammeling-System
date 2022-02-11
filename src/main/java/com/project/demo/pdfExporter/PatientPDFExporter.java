package com.project.demo.pdfExporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.project.demo.model.Patient;

public class PatientPDFExporter {
	
	private List<Patient> listPatient;
	
	public PatientPDFExporter(List<Patient> listPatient) {
		this.listPatient = listPatient;
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Patient ID",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Address",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Telephone",font));
		table.addCell(cell);
	}
	
	private void writeTableDate(PdfPTable table) {
		
		for(Patient patient : listPatient) {
			table.addCell(String.valueOf(patient.getId()));
			table.addCell(patient.getName());
			table.addCell(patient.getEmail());
			table.addCell(patient.getAddress());
			table.addCell(patient.getTelephone());
		}
		
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException{
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLUE);
		font.setSize(18);
		
		Paragraph title = new Paragraph("List of Patients",font);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(title);
		
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,1.5f});
		
		writeTableHeader(table);
		writeTableDate(table);
		document.add(table);
		
		document.close();
	}

}
