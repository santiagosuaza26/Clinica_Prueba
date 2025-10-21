package app.clinic.inventory.domain.model;

public class Medication {
    private Long id;
    private String name;
    private String dosage;
    private int durationDays;
    private double cost;
    private boolean requiresPrescription;

    public Medication(Long id, String name, String dosage, int durationDays, double cost, boolean requiresPrescription) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.durationDays = durationDays;
        this.cost = cost;
        this.requiresPrescription = requiresPrescription;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public int getDurationDays() { return durationDays; }
    public double getCost() { return cost; }
    public boolean isRequiresPrescription() { return requiresPrescription; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public void setCost(double cost) { this.cost = cost; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }
}
