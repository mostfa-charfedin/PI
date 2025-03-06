package Services;

import Models.Projet;
import Models.Tache;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static void generateProjectPdf(Projet projet, List<Tache> taches, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Font.UNDERLINE, BaseColor.BLUE);
            Paragraph title = new Paragraph("Rapport du Projet", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));

            // Project Information
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            document.add(new Paragraph("Titre du Projet: ", headerFont));
            document.add(new Paragraph(projet.getTitre(), contentFont));
            document.add(new Paragraph("Description: ", headerFont));
            document.add(new Paragraph(projet.getDescription(), contentFont));
            document.add(new Paragraph("Chef de Projet: ", headerFont));
            document.add(new Paragraph(projet.getNom() + " " + projet.getPrenom(), contentFont));
            document.add(new Paragraph("\n"));

            // Table for Tasks
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);
            table.setSpacingAfter(15f);

            // Headers with better styling
            PdfPCell header1 = new PdfPCell(new Phrase("Titre de la Tâche", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Description", headerFont));
            PdfPCell header3 = new PdfPCell(new Phrase("Assigné à", headerFont));

            BaseColor headerColor = new BaseColor(200, 200, 200);
            header1.setBackgroundColor(headerColor);
            header2.setBackgroundColor(headerColor);
            header3.setBackgroundColor(headerColor);

            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            // Task Data with better padding
            for (Tache tache : taches) {
                PdfPCell cell1 = new PdfPCell(new Phrase(tache.getTitre(), contentFont));
                PdfPCell cell2 = new PdfPCell(new Phrase(tache.getDescription(), contentFont));
                PdfPCell cell3 = new PdfPCell(new Phrase(tache.getNom() + " " + tache.getPrenom(), contentFont));

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
            }

            document.add(table);
            document.close();
            System.out.println("✅ Rapport du projet généré avec succès : " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateTaskPdf(List<Tache> taches, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Font.UNDERLINE, BaseColor.BLUE);
            Paragraph title = new Paragraph("task report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));

            // Table for Tasks
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);
            table.setSpacingAfter(15f);

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            PdfPCell header1 = new PdfPCell(new Phrase("task title", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Description", headerFont));
            PdfPCell header3 = new PdfPCell(new Phrase("attributed to", headerFont));

            BaseColor headerColor = new BaseColor(200, 200, 200);
            header1.setBackgroundColor(headerColor);
            header2.setBackgroundColor(headerColor);
            header3.setBackgroundColor(headerColor);

            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            // Task Data
            for (Tache tache : taches) {
                PdfPCell cell1 = new PdfPCell(new Phrase(tache.getTitre(), contentFont));
                PdfPCell cell2 = new PdfPCell(new Phrase(tache.getDescription(), contentFont));
                PdfPCell cell3 = new PdfPCell(new Phrase(tache.getNom() + " " + tache.getPrenom(), contentFont));

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
            }

            document.add(table);
            document.close();
            System.out.println("✅ Rapport des tâches généré avec succès : " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}