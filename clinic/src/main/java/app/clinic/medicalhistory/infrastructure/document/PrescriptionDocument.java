package app.clinic.medicalhistory.infrastructure.document;

public class PrescriptionDocument {
    private String orderNumber;
    private String medicationId;
    private String dosage;
    private String duration;
    private int item;

    public PrescriptionDocument() {}

    public PrescriptionDocument(String orderNumber, String medicationId, String dosage, String duration, int item) {
        this.orderNumber = orderNumber;
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.duration = duration;
        this.item = item;
    }

    public String getOrderNumber() { return orderNumber; }
    public String getMedicationId() { return medicationId; }
    public String getDosage() { return dosage; }
    public String getDuration() { return duration; }
    public int getItem() { return item; }

    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setMedicationId(String medicationId) { this.medicationId = medicationId; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setItem(int item) { this.item = item; }
}
