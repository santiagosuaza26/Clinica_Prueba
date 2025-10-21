package app.clinic.medicalhistory.infrastructure.document;

public class DiagnosticAidRecordDocument {
    private String orderNumber;
    private String aidId;
    private int quantity;
    private boolean requiresSpecialist;
    private String specialistTypeId;
    private int item;

    public DiagnosticAidRecordDocument() {}

    public DiagnosticAidRecordDocument(
        String orderNumber, String aidId, int quantity,
        boolean requiresSpecialist, String specialistTypeId, int item
    ) {
        this.orderNumber = orderNumber;
        this.aidId = aidId;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistTypeId = specialistTypeId;
        this.item = item;
    }

    public String getOrderNumber() { return orderNumber; }
    public String getAidId() { return aidId; }
    public int getQuantity() { return quantity; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public String getSpecialistTypeId() { return specialistTypeId; }
    public int getItem() { return item; }

    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setAidId(String aidId) { this.aidId = aidId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistTypeId(String specialistTypeId) { this.specialistTypeId = specialistTypeId; }
    public void setItem(int item) { this.item = item; }
}
