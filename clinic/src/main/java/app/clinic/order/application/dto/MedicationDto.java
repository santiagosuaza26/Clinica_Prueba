package app.clinic.order.application.dto;

/**
 * DTO para transferencia de datos de medicamentos.
 */
public class MedicationDto {

    private String id;
    private String name;
    private double cost;
    private String dosage;
    private int durationDays;

    // Constructor vacío
    public MedicationDto() {
    }

    // Constructor con parámetros
    public MedicationDto(String id, String name, double cost, String dosage, int durationDays) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public String toString() {
        return "MedicationDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", dosage='" + dosage + '\'' +
                ", durationDays=" + durationDays +
                '}';
    }
}
