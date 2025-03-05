package Services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class PdfGeneratorService {

    /**
     * Génère un rapport PDF générique
     * @param fileName Nom du fichier PDF
     * @param title Titre du document
     * @param sections Sections à ajouter au document
     * @return Chemin complet du fichier PDF généré
     * @throws DocumentException
     * @throws IOException
     */
    public String generatePDF(String fileName, String title, List<PDFSection> sections) throws DocumentException, IOException {
        // Créer un nom de fichier unique avec timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fullFileName = fileName + "_" + timestamp + ".pdf";
        String filePath = System.getProperty("user.home") + File.separator + fullFileName;

        // Créer le document PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Polices
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.DARK_GRAY);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        // Titre du document
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        // Date du rapport
        Paragraph dateParagraph = new Paragraph("Généré le : " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), subtitleFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(dateParagraph);

        // Ajouter un espace
        document.add(Chunk.NEWLINE);

        // Ajouter chaque section
        for (PDFSection section : sections) {
            addSection(document, section, sectionFont, normalFont);
        }

        document.close();

        return filePath;
    }

    /**
     * Ajoute une section au document PDF
     * @param document Document PDF
     * @param section Section à ajouter
     * @param sectionFont Police pour le titre de section
     * @param normalFont Police pour le contenu
     * @throws DocumentException
     */
    private void addSection(Document document, PDFSection section, Font sectionFont, Font normalFont) throws DocumentException {
        // Titre de section
        Paragraph sectionTitle = new Paragraph(section.getSectionTitle(), sectionFont);
        sectionTitle.setSpacingBefore(10);
        sectionTitle.setSpacingAfter(5);
        document.add(sectionTitle);

        // Paragraphes de texte
        if (section.getParagraphs() != null) {
            for (String para : section.getParagraphs()) {
                Paragraph paragraph = new Paragraph(para, normalFont);
                document.add(paragraph);
            }
        }

        // Tableau
        if (section.getTableData() != null) {
            PdfPTable table = createTable(section.getTableData());
            document.add(table);
        }

        // Liste
        if (section.getListItems() != null) {
            com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            for (String item : section.getListItems()) {
                list.add(new ListItem(item));
            }
            document.add(list);
        }

        // Espace après la section
        document.add(Chunk.NEWLINE);
    }

    /**
     * Crée un tableau à partir de données
     * @param tableData Données du tableau
     * @return Tableau PDF
     */
    private PdfPTable createTable(Map<String, List<String>> tableData) throws DocumentException {
        // Déterminer le nombre de colonnes
        int columnCount = tableData.size();
        PdfPTable table = new PdfPTable(columnCount);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        // Police pour l'en-tête et le contenu
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        // En-têtes
        for (String header : tableData.keySet()) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        // Trouver la longueur maximale des colonnes
        int maxRowCount = tableData.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // Remplir les données
        for (int row = 0; row < maxRowCount; row++) {
            for (List<String> column : tableData.values()) {
                String cellValue = row < column.size() ? column.get(row) : "";
                PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }
        }

        return table;
    }

    /**
     * Classe interne pour représenter une section de PDF
     */
    public static class PDFSection {
        private String sectionTitle;
        private List<String> paragraphs;
        private Map<String, List<String>> tableData;
        private List<String> listItems;

        public PDFSection(String sectionTitle) {
            this.sectionTitle = sectionTitle;
        }

        // Getters et setters
        public String getSectionTitle() { return sectionTitle; }
        public PDFSection setParagraphs(List<String> paragraphs) {
            this.paragraphs = paragraphs;
            return this;
        }
        public PDFSection setTableData(Map<String, List<String>> tableData) {
            this.tableData = tableData;
            return this;
        }
        public PDFSection setListItems(List<String> listItems) {
            this.listItems = listItems;
            return this;
        }

        public List<String> getParagraphs() { return paragraphs; }
        public Map<String, List<String>> getTableData() { return tableData; }
        public List<String> getListItems() { return listItems; }
    }
}