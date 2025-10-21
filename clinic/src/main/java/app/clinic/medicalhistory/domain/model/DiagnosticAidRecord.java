package app.clinic.medicalhistory.domain.model;

public class DiagnosticAidRecord {
    private String orderNumber;
    private String aidId;
    private int quantity;
    private boolean requiresSpecialist;
    private String specialistTypeId;
    private int item;

    public DiagnosticAidRecord(String orderNumber, String aidId, int quantity, boolean requiresSpecialist,
                               String specialistTypeId, int item) {
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
}
