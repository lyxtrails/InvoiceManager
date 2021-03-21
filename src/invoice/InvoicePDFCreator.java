package invoice;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;


public class InvoicePDFCreator {
    final String COMPANY_NAME = "Company Name";
    final String COMPANY_PHONE_NUMBERS = "(123)123-1234";
    final String COMPANY_EMAIL = "company@gmail.com";

    private static InvoicePDFCreator instance;

    public static InvoicePDFCreator getInstance() {
        if (instance == null) {
            instance = new InvoicePDFCreator();
        }
        return instance;
    }

    private String getFilepath(String rootPath, InvoiceData invoiceData) {
        String filename = invoiceData.getDateForFilename() + "_"
                + invoiceData.getCustomerName();
        filename = filename.replaceAll(" ", "_");
        String newFilename;
        int counter = 0;
        File temp;
        while (true) {
            newFilename = filename;
            if (counter > 0) {
                newFilename += "_" + String.valueOf(counter);
            }
            temp = new File(rootPath, newFilename + ".pdf");
            if (!temp.exists()) {
                filename = newFilename;
                break;
            }
            counter++;
        }
        return Paths.get(rootPath, filename + ".pdf").toString();
    }

    public static String createFile(InvoiceData invoiceData) {
        Map<String, String> configs = Config.getConfigurations();
        String fileSavePath = configs.get(Config.FILE_SAVE_PATH_KEY);
        String companyName = configs.get(Config.COMPANY_NAME_KEY);
        String companyPhoneNumber = configs.get(Config.COMPANY_PHONE_NUMBER_KEY);
        String companyEmail = configs.get(Config.COMPANY_EMAIL_KEY);

        File directory = new File(fileSavePath);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String filepath = getInstance().getFilepath(fileSavePath, invoiceData);
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filepath));
            Document document = new Document(pdfDoc, PageSize.LETTER);

            // Set document info.
            PdfDocumentInfo docInfo = pdfDoc.getDocumentInfo();
            docInfo.setTitle(companyName);

            // Title
            Table table = new Table(1)
                    .setBorderTop(new SolidBorder(1))
                    .setWidth(UnitValue.createPercentValue(100));
            table.addCell(InvoiceElementFactory.getCenteredNoBorderCell(
                    companyName, 22)
                    .setBold());
            table.addCell(InvoiceElementFactory.getCenteredNoBorderCell(
                    companyPhoneNumber, 12));
            table.addCell(InvoiceElementFactory.getCenteredNoBorderCell(
                    companyEmail, 12));
            document.add(table);

            document.add(InvoiceElementFactory.getEmptySpace(15));

            // Customer Info
            table = new Table(UnitValue.createPercentArray(2))
                    .setBorder(Border.NO_BORDER)
                    .setBorderTop(new SolidBorder(1))
                    .setBorderBottom(new SolidBorder(1))
                    .setWidth(UnitValue.createPercentValue(100));
            table.addCell(new Cell(1,2)
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Customer Info"))
                    .setFontSize(12)
                    .setBold());
            // Add customer Name cells.
            Table innerTable = new Table(2)
                    .setBorder(Border.NO_BORDER);
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    "Customer Name:", 12)
                    .setPaddingLeft(0.0f));
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    invoiceData.getCustomerName(), 12)
                    .setPaddingLeft(5.0f));
            table.addCell(new Cell().add(innerTable).setBorder(Border.NO_BORDER));
            // Add customer phone number cells.
            innerTable = new Table(2)
                    .setBorder(Border.NO_BORDER);
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    "Phone Number:", 12)
                    .setPaddingLeft(0.0f));
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    invoiceData.getCustomerPhoneNumber(), 12)
                    .setPaddingLeft(5.0f));
            table.addCell(new Cell().add(innerTable).setBorder(Border.NO_BORDER));
            innerTable = new Table(2)
                    .setBorder(Border.NO_BORDER);
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    "Address:", 12)
                    .setPaddingLeft(0.0f));
            innerTable.addCell(InvoiceElementFactory.getNoBorderCell(
                    invoiceData.getAddress(), 12)
                    .setPaddingLeft(5.0f));
            table.addCell(new Cell(1, 2).add(innerTable).setBorder(Border.NO_BORDER));
            document.add(table);

            document.add(InvoiceElementFactory.getEmptySpace(25));

            table = new Table(new float[] { 15, 55, 15, 15 })
                    .setWidth(UnitValue.createPercentValue(100));
            table.addCell(new Cell().add(new Paragraph("#")));
            table.addCell(new Cell().add(new Paragraph("Work Description")));
            table.addCell(new Cell().add(new Paragraph("Material Cost")));
            table.addCell(new Cell().add(new Paragraph("Labor Cost")));
            List<InvoiceData.Row> rows = invoiceData.getRows();
            for (int i = 0; i < rows.size(); i++) {
                InvoiceData.Row row = rows.get(i);
                table.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))));
                table.addCell(new Cell().add(new Paragraph(row.workDescription)));
                table.addCell(new Cell().add(new Paragraph(row.materialCost))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(row.laborCost))
                        .setTextAlignment(TextAlignment.CENTER));
            }

            if (invoiceData.getTotalMaterialCost() != "" && invoiceData.getTotalLaborCost() != "") {
                table.addCell(new Cell().add(new Paragraph("")));
                table.addCell(new Cell().add(new Paragraph("Total")));
                table.addCell(new Cell().add(new Paragraph(invoiceData.getTotalMaterialCost()))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(invoiceData.getTotalLaborCost()))
                        .setTextAlignment(TextAlignment.CENTER));
            }
            table.addCell(new Cell().add(new Paragraph("")));
            table.addCell(new Cell().add(new Paragraph("Total (Material + Labor)")));
            table.addCell(new Cell(1,2).add(new Paragraph(invoiceData.getTotalCost()))
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(table);

            document.add(InvoiceElementFactory.getEmptySpace(30));

            table = new Table(new float[] { 55, 10, 35 })
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER);
            table.addCell(InvoiceElementFactory.getNoBorderCell("", 12));
            table.addCell(InvoiceElementFactory.getNoBorderCell("", 12));
            table.addCell(InvoiceElementFactory.getNoBorderCell(invoiceData.getDateString(), 12));
            table.addCell(InvoiceElementFactory.getNoBorderCell("Signature", 8)
                    .setWidth(300)
                    .setBorderTop(new SolidBorder(1))
                    .setVerticalAlignment(VerticalAlignment.TOP));
            table.addCell(InvoiceElementFactory.getNoBorderCell("", 12));
            table.addCell(InvoiceElementFactory.getNoBorderCell("Date", 8)
                    .setBorderTop(new SolidBorder(1))
                    .setVerticalAlignment(VerticalAlignment.TOP));
            table.setFixedPosition(
                    document.getLeftMargin(),
                    document.getBottomMargin(),
                    document.getPdfDocument().getDefaultPageSize().getWidth()
                            - document.getLeftMargin() - document.getRightMargin());
            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filepath;
    }
}
