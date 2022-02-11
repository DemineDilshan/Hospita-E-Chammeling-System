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
import com.project.demo.model.Doctor;

public class DoctorPDFExporter {
	
	private List<Doctor> listDoctor;
	
	public DoctorPDFExporter(List<Doctor> listDoctor) {
		this.listDoctor = listDoctor;
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Doctor ID",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Specialty",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Telephone",font));
		table.addCell(cell);
	}
	
	private void writeTableDate(PdfPTable table) {
		
		for(Doctor doctor : listDoctor) {
			table.addCell(String.valueOf(doctor.getId()));
			table.addCell(doctor.getName());
			table.addCell(doctor.getSpecialty());
			table.addCell(doctor.getEmail());
			table.addCell(doctor.getTelephone());
		}
		
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException{
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLUE);
		font.setSize(18);
		
		Paragraph title = new Paragraph("List of Doctors",font);
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
