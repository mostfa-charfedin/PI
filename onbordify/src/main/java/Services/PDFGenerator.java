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
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph title = new Paragraph("Rapport du Projet", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Project Information
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            document.add(new Paragraph("Titre du Projet: " + projet.getTitre(), headerFont));
            document.add(new Paragraph("Description: " + projet.getDescription()));
            document.add(new Paragraph("Chef de Projet: " + projet.getNom() + " " + projet.getPrenom()));
            document.add(new Paragraph("\n"));

            // Table for Tasks
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Headers
            PdfPCell header1 = new PdfPCell(new Phrase("Titre de la Tâche", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Description", headerFont));
            PdfPCell header3 = new PdfPCell(new Phrase("Assigné à", headerFont));

            header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header3.setBackgroundColor(BaseColor.LIGHT_GRAY);

            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            // Task Data
            for (Tache tache : taches) {
                table.addCell(tache.getTitre());
                table.addCell(tache.getDescription());
                table.addCell(tache.getNom() + " " + tache.getPrenom());
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

            document.add(new Paragraph("Task Report\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            PdfPCell header1 = new PdfPCell(new Phrase("Task ID", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Title", headerFont));
            PdfPCell header3 = new PdfPCell(new Phrase("Description", headerFont));

            header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header3.setBackgroundColor(BaseColor.LIGHT_GRAY);

            table.addCell(header1);
            table.addCell(header2);
            table.addCell(header3);

            // Task Data
            for (Tache tache : taches) {
                table.addCell(String.valueOf(tache.getIdTache()));
                table.addCell(tache.getTitre());
                table.addCell(tache.getDescription());
            }

            document.add(table);
            document.close();
            System.out.println("✅ Task report generated successfully: " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
