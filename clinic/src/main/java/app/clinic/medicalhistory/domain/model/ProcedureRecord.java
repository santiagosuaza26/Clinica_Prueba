package app.clinic.medicalhistory.domain.model;

public class ProcedureRecord {
    private String orderNumber;
    private String procedureId;
    private int repetitions;
    private String frequency;
    private boolean requiresSpecialist;
    private String specialistTypeId;
    private int item;

    public ProcedureRecord(String orderNumber, String procedureId, int repetitions, String frequency,
                           boolean requiresSpecialist, String specialistTypeId, int item) {
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
}
