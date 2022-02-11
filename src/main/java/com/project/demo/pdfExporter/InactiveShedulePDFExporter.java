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
import com.project.demo.model.Schedule;

public class InactiveShedulePDFExporter {
	
	private List<Schedule> listSchedule;
	
	public InactiveShedulePDFExporter(List<Schedule> listSchedule) {
		this.listSchedule = listSchedule;
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Schedule ID",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Doctor Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Date",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Time",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Patient Limit",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Appointment Count",font));
		table.addCell(cell);
	}
	
	private void writeTableDate(PdfPTable table) {
		
		for(Schedule schedule : listSchedule) {
			table.addCell(String.valueOf(schedule.getId()));
			table.addCell("Dr. " + schedule.getDoctor().getName());
			table.addCell(String.valueOf(schedule.getDate()));
			table.addCell(String.valueOf(schedule.getTime()));
			table.addCell(schedule.getPatientCount());
			table.addCell(String.valueOf(Integer.valueOf(schedule.getPatientCount())-Integer.valueOf(schedule.getAvailableNumber())));
		}
		
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException{
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLUE);
		font.setSize(18);
		
		Paragraph title = new Paragraph("List of Inactive Schedule",font);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(title);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,1.5f,1.5f});
		
		writeTableHeader(table);
		writeTableDate(table);
		document.add(table);
		
		document.close();
	}

}
