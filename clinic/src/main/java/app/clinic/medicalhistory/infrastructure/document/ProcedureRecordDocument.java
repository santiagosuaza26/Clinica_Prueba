package app.clinic.medicalhistory.infrastructure.document;

public class ProcedureRecordDocument {
    private String orderNumber;
    private String procedureId;
    private int repetitions;
    private String frequency;
    private boolean requiresSpecialist;
    private String specialistTypeId;
    private int item;

    public ProcedureRecordDocument() {}

    public ProcedureRecordDocument(
        String orderNumber, String procedureId, int repetitions,
        String frequency, boolean requiresSpecialist, String specialistTypeId, int item
    ) {
        this.orderNumber = orderNumber;
        this.procedureId = procedureId;
        this.repetitions = repetitions;
        this.frequency = frequency;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistTypeId = specialistTypeId;
        this.item = item;
    }

    public String getOrderNumber() { return orderNumber; }
    public String getProcedureId() { return procedureId; }
    public int getRepetitions() { return repetitions; }
    public String getFrequency() { return frequency; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public String getSpecialistTypeId() { return specialistTypeId; }
    public int getItem() { return item; }

    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setProcedureId(String procedureId) { this.procedureId = procedureId; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistTypeId(String specialistTypeId) { this.specialistTypeId = specialistTypeId; }
    public void setItem(int item) { this.item = item; }
}
