package invoice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

class InvoiceData {
    static class  Metadata {
        String customerName;
        String customerPhoneNumber;
        String address;
        Date date;

        public Metadata (String customerName, String customerPhoneNumber,
                         String address, Date date) {
            this.customerName = customerName;
            this.customerPhoneNumber = customerPhoneNumber;
            this.address = address;
            this.date = date;
        }
    }
    static class Row {
        String workDescription;
        String materialCost;
        String laborCost;

        public Row (String workDescription, String materialCost, String laborCost) {
            this.workDescription = workDescription;
            this.materialCost = materialCost;
            this.laborCost = laborCost;
        }
    }
    static class Cost {
        String material;
        String labor;
        String total;

        public Cost(String material, String labor, String total) {
            this.material = material;
            this.labor = labor;
            this.total = total;
        }
    }

    private Metadata metadata;
    private List<Row> rows;
    private Cost cost;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat filenameDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    public InvoiceData(Metadata metadata, List<Row> rows, Cost cost) {
        this.metadata = metadata;
        this.rows = rows;
        this.cost = cost;
    }

    public String getCustomerName() {
        return this.metadata.customerName;
    }
    public String getCustomerPhoneNumber() {
        return this.metadata.customerPhoneNumber;
    }
    public String getAddress() {
        return this.metadata.address;
    }
    public String getDateString() {
        return dateFormat.format(this.metadata.date);
    }
    public String getDateForFilename() {
        return filenameDateFormat.format(this.metadata.date);
    }
    public List<Row> getRows() {
        return this.rows;
    }
    public String getTotalMaterialCost() {
        return this.cost.material;
    }
    public String getTotalLaborCost() {
        return this.cost.labor;
    }
    public String getTotalCost() {
        return this.cost.total;
    }
}
