package invoice;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class InvoiceElementFactory {
    public static Cell getNoBorderCell(String text, int size) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(size)
                .setBorder(Border.NO_BORDER);
    }

    public static Cell getCenteredNoBorderCell(String text, int size) {
        return getNoBorderCell(text, size)
                .setTextAlignment(TextAlignment.CENTER);
    }

    public static Cell getNoBorderEmptyCell() {
        return new Cell().setBorder(Border.NO_BORDER);
    }

    public static IBlockElement getEmptySpace(int size) {
        return new Table(UnitValue.createPercentArray(1))
                .addCell(getNoBorderEmptyCell().setHeight(size))
                .setBorder(Border.NO_BORDER)
                .setWidth(UnitValue.createPercentValue(100));
    }
}
