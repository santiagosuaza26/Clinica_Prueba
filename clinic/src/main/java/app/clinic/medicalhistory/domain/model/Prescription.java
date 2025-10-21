package app.clinic.medicalhistory.domain.model;

public class Prescription {
    private String orderNumber;
    private String medicationId;
    private String dosage;
    private String duration;
    private int item;

    public Prescription(String orderNumber, String medicationId, String dosage, String duration, int item) {
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
}
